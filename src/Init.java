

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
        int read = 0;
        ShotBall shot = new ShotBall();
		LCD.clear();
		
		RS485Connection connection = RS485.waitForConnection(0, NXTConnection.PACKET);
		if (connection == null){
			LCD.drawString("Error al", 0, 0);
			LCD.drawString("Conectar", 0, 1);
			Delay.msDelay(5000);
			System.exit(1);
		}
		DataInputStream dis = connection.openDataInputStream();
        DataOutputStream dos = connection.openDataOutputStream();
        Thread t = new Thread(shot);
        t.start();
        boolean running = true;
        while (running){

	        try{
	        	read = dis.readInt();
	        	shot.setValue(read);
	            LCD.drawString("Read: ", 0, 2);
	            LCD.drawString("" + read, 0, 3);
	        }catch (IOException ioe){
	            LCD.drawString("Read Exception ", 0, 5);
	        }

        }
        
        try{
            LCD.drawString("Closing...    ", 0, 3);
            dis.close();
            dos.close();
            connection.close();
        }catch (IOException ioe){
            LCD.drawString("Close Exception", 0, 5);
            LCD.refresh();
        }
        
        LCD.drawString("Finished        ", 0, 3);
        Delay.msDelay(5000);
    }
	
}
