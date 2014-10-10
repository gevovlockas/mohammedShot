

import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.ColorSensor.Color;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.util.Delay;

public class ShotBall implements Runnable {
	
	private static int SPEED_BLUE = 80;
	private static int MEDIUM_SPEED_ORANGE = 700;
	private static int SLOW_SPEED_ORANGE = 500;
	private static int HIGH_SPEED_ORANGE = 800;
	private static int FULL_TURN = 120;

	private int value;
	private boolean running;
	
	private void shot(int speedType){
		ColorSensor sensor = new ColorSensor(SensorPort.S1);
		int measurement = 1;
		
		int speed = HIGH_SPEED_ORANGE;
		if(speedType==1){
			speed = SLOW_SPEED_ORANGE;
		} else if(speedType==2){
			speed = MEDIUM_SPEED_ORANGE;
		}

		while (value != 0){
			
			Color color = sensor.getColor();
			
			int red = color.getRed(); //green - 15 > azul
			int green = color.getGreen();
			int blue = color.getBlue();
	
			switch (color.getColor()){
				case Color.GREEN:
					Motor.A.setSpeed(SPEED_BLUE);
					Motor.A.rotateTo(measurement * FULL_TURN);
					measurement++;
				break;
				case Color.WHITE:
					//Las celestes muy claras
					if(blue > red)
							Motor.A.setSpeed(speed);
						else						
							Motor.A.setSpeed(SPEED_BLUE);
						Motor.A.rotateTo(measurement * FULL_TURN);
					measurement++;
				break;

				case Color.BLACK:
					if((red > 20) && (blue > 20) && (green > 20)){//Vi algo
						if((red > 90) && (blue > 90) && (green > 90))
							Motor.A.setSpeed(speed);
						else						
							Motor.A.setSpeed(SPEED_BLUE);
						Motor.A.rotateTo(measurement * FULL_TURN);
						measurement++;
					}
					//sino es que vio nada y entonces no hace nada
				break;

				default:
					//Vio algo que no era pelotita azul ni celeste
					Motor.A.setSpeed(speed);
					Motor.A.rotateTo(measurement * FULL_TURN);
					measurement++;
				break;	
						
			}
			//cambiar delay por while -30>tacho-360*medidas
			Delay.msDelay(2200);
		}
		
	}
	
	
	public ShotBall() {
		value = 0;
		running = true;
	}

	@Override
	public void run() {
		while(running) {
			if(value != 0){
				shot(value);
			} else {
				Motor.A.stop();
			}
			//Delay.msDelay(2000);
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
