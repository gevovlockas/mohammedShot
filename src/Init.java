

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.LCD;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.RS485;
import lejos.nxt.comm.RS485Connection;
import lejos.util.Delay;

public class Init {

	
	private static RS485Connection connection;
	/* 
	 * 0 - Dejar de disparar 
	 * 1 - Disparo bajo
	 * 2 - Disparo medio
	 * 3 - Disparo alto
	 * 
	 * */
	public static void main(String[] args) {
		
		byte[] readValue = {0};
        int value = 0;
        ShotBall shot = new ShotBall();
		LCD.clear();
		
//		DataInputStream dis = null;
		connect();
//		dis = connection.openDataInputStream();
//        Thread t = new Thread(shot);
//        t.start();
        while (true){

	        try{
//	            System.out.println("Esperando valor");
//	        	value = dis.readInt();
//	        	shot.setValue(value);
//	            System.out.println("Value = " + value);
	        	if (connection == null){
	        		connect();
	        	}
	        	value = 1;
	        	connection.read(readValue, value, true);
	        	if (value < 0){
	        		System.out.println("Error al leer");
	        		Delay.msDelay(3000);
	        	}else{
		        	System.out.println("Read");
		        	value = byteArrayToInt(readValue);
//		        	shot.setValue(value);
		            System.out.println("Value = " + value);
		            Delay.msDelay(2000);
	        	}
	        }catch (Exception ioe){
	            System.out.println("Error al recibir datos");
	            try{
	            	connection.close();
	            }finally{
	            	connection = null;
	            }
	        }

        }
	}

	private static void connect(){
		System.out.println("Aceptando conexion");
		connection = RS485.waitForConnection(0, NXTConnection.PACKET);
		if (connection == null){
			System.out.println("Error al conectar");
			Delay.msDelay(5000);
			System.exit(1);
		}
	}
	
	public static int byteArrayToInt(byte[] b){
	    int value = 0;
	    try{
		    for (int i = 0; i < b.length; i++) {
		        int shift = (b.length - 1 - i) * 8;
		        value += (b[i] & 0x000000FF) << shift;
		    }
	    }catch(Exception ex){
	    	System.out.println(ex.getMessage());
	    }
	    return value;
	}
}
