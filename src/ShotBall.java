

import lejos.util.Delay;

public class ShotBall implements Runnable {

	private int value;
	private boolean running;
	
	public ShotBall() {
		value = 0;
		running = true;
	}

	@Override
	public void run() {
		while(running) {
			System.out.println("value=" + value);
			Delay.msDelay(5000);
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
