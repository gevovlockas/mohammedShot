

import lejos.nxt.ColorSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.ColorSensor.Color;
import lejos.util.Delay;

public class Init {

	
	private static int SPEED_BLUE = 80;
	private static int SLOW_SPEED_ORANGE = 350;
	private static int MEDIUM_SPEED_ORANGE = 430;
	private static int HIGH_SPEED_ORANGE = 490;
	private static int FULL_TURN = 120;
	private static int HALF_TURN = FULL_TURN / 2;
	
	
	
	/* 
	 * 0 - Dejar de disparar 
	 * 1 - Disparo bajo
	 * 2 - Disparo medio
	 * 3 - Disparo alto
	 * 
	 * */
	public static void main(String[] args) {
		
		//Inicio el sistema de tiro
//		Delay.msDelay(2000);
		Constants.SHOOTER_MOTOR.setSpeed(HIGH_SPEED_ORANGE);
		Constants.SHOOTER_MOTOR.rotate(HALF_TURN);
		Constants.SHOOTER_MOTOR.stop();
		
		Constants.SENSOR_MOTOR.setSpeed(Constants.SPEED_SENSOR_MOTOR);
		Constants.SENSOR_MOTOR.rotate(Constants.ANGLE_SENSOR_MOTOR);
		Constants.SENSOR_MOTOR.stop();
		
		ShotBall shot = new ShotBall();
		Thread t = new Thread(shot);
		t.start();

        while (true){
        	action(shot.getValue());
        }
	}

	
	public static void action(int value) {
        if(value == 0){
        	Constants.SHOOTER_MOTOR.stop();
        	return;
        }
        Constants.SHOOTER_MOTOR.resetTachoCount();
		ColorSensor sensor = new ColorSensor(SensorPort.S1);
		int measurement = 0;
		//CAMBIO
		boolean viColor=false;
		//FIN CAMBIO
		int speed = HIGH_SPEED_ORANGE;
		if((value==1) || (value==-1)){
			speed = SLOW_SPEED_ORANGE;
		} else if((value==2) || (value==-2)){
			speed = MEDIUM_SPEED_ORANGE;
		}
		
		for(int i=0; i < 2; i++) {
			
			Color color = sensor.getColor();
			
			int red = color.getRed(); //green - 15 > azul
			int green = color.getGreen();
			int blue = color.getBlue();
			System.out.println(red + " " + green + " " + blue);
	
			switch (color.getColor()){
				case Color.GREEN:
					Motor.A.setSpeed(SPEED_BLUE);
					//CAMBIO
					if(viColor){	
						measurement = ((value < 0)? measurement +1:  measurement -1);
						Constants.SHOOTER_MOTOR.rotateTo(measurement * FULL_TURN);
					}
					viColor=true;
					//FIN CAMBIO
				break;
				case Color.WHITE:
					//Las celestes muy claras
					if(blue > red){
						
						Constants.SHOOTER_MOTOR.setSpeed(speed);
						if(viColor){
							measurement = ((value < 0)? measurement - 1:  measurement + 1);
							Constants.SHOOTER_MOTOR.rotateTo(measurement * FULL_TURN);
						}
					}else{						
						Constants.SHOOTER_MOTOR.setSpeed(SPEED_BLUE);
						if(viColor){
							measurement = ((value < 0)? measurement + 1:  measurement - 1);
							Constants.SHOOTER_MOTOR.rotateTo(measurement * FULL_TURN);
						}
					}//CAMBIO
					
					viColor=true;	
					//FIN CAMBIO
				break;

				case Color.BLACK:
					if((red > 20) && (blue > 20) && (green > 20)){//Vi algo
						if((red > 90) && (blue > 90) && (green > 90)){
							Constants.SHOOTER_MOTOR.setSpeed(speed);
							if(viColor){
								measurement = ((value < 0)? measurement - 1:  measurement + 1);
								Constants.SHOOTER_MOTOR.rotateTo(measurement * FULL_TURN);
							}
						}else{						
							Constants.SHOOTER_MOTOR.setSpeed(SPEED_BLUE);
							if(viColor){
								measurement = ((value < 0)? measurement + 1:  measurement - 1);
								Constants.SHOOTER_MOTOR.rotateTo(measurement * FULL_TURN);
							}
						}//CAMBIO
						
						viColor=true;
						
					}else
						viColor=false;
						//FIN CAMBIO
					//sino es que vio nada y entonces no hace nada
				break;

				default:
					//ES POSIBLE QUE SOLO SACANDO ESTO SE ARREGLARA, O QUEDARA BASTANTE MEJOR
					//Vio algo que no era pelotita azul ni celeste
					Constants.SHOOTER_MOTOR.setSpeed(speed);
					//CAMBIO
					if(viColor){
						measurement = ((value < 0)? measurement - 1:  measurement + 1);
						Constants.SHOOTER_MOTOR.rotateTo(measurement * FULL_TURN);
					}
					viColor=true;
					//FIN CAMBIO
				break;	
						
			}
			
			Delay.msDelay(550);
		}
		//Motor.A.stop();
		
	}

}
