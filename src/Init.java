

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.LCD;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.RS485;
import lejos.nxt.comm.RS485Connection;
import lejos.util.Delay;

public class Init {

	
	/* 
	 * 0 - Dejar de disparar 
	 * 1 - Disparo bajo
	 * 2 - Disparo medio
	 * 3 - Disparo alto
	 * 
	 * */
	public static void main(String[] args) {
        int value = 0;
        ShotBall shot = new ShotBall();
		LCD.clear();
		
		DataInputStream dis = null;
		RS485Connection connection = RS485.waitForConnection(0, NXTConnection.PACKET);
		if (connection == null){
			System.out.println("Error al conectar");
			Delay.msDelay(5000);
			System.exit(1);
		}
		dis = connection.openDataInputStream();
        Thread t = new Thread(shot);
        t.start();
        while (true){

	        try{
	            System.out.println("Esperando valor");
	        	value = dis.readInt();
	        	shot.setValue(value);
	            System.out.println("Value = " + value);
	        }catch (IOException ioe){
	            System.out.println("Error al recivir datos");
	        }

        }
	}
	
}
