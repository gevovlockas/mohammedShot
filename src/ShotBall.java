

import java.io.DataInputStream;
import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.ColorSensor.Color;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.RS485;
import lejos.nxt.comm.RS485Connection;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.util.Delay;

public class ShotBall implements Runnable {
	
	private DataInputStream dis = null;
	private boolean running = true;
	private int value = 0;
	
	public ShotBall() {
		RS485Connection connection = RS485.waitForConnection(0, NXTConnection.PACKET);
		if (connection == null){
			System.out.println("Error al conectar");
			Delay.msDelay(5000);
			System.exit(1);
		}
		dis = connection.openDataInputStream();
	}

	@Override
	public void run() {
		while(running){
			try {
				System.out.println("Esperando valor");
				value = dis.readInt();
				System.out.println("Valor leido " + value);
			} catch (IOException e) {
				System.out.println("Error al leer datos");
			}
		}
	}
	
	public void stop() {
		running = false;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
}
