

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
	
	private void shot(){
		ColorSensor sensor = new ColorSensor(SensorPort.S1);
		int measurement = 0;
		//CAMBIO
		int util=0;
		boolean viColor=false;
		//FIN CAMBIO
		int speed = HIGH_SPEED_ORANGE;
		if((value==1) || (value==-1)){
			speed = SLOW_SPEED_ORANGE;
		} else if((value==2) || (value==-2)){
			speed = MEDIUM_SPEED_ORANGE;
		} else if(value==0){
			Motor.A.stop();
		}
		
		while (value != 0){
			
			Color color = sensor.getColor();
			
			int red = color.getRed(); //green - 15 > azul
			int green = color.getGreen();
			int blue = color.getBlue();
	
			switch (color.getColor()){
				case Color.GREEN:
					Motor.A.setSpeed(SPEED_BLUE);
					//CAMBIO
					if(viColor){	
						measurement = value < 0? measurement - 1:  measurement + 1;
						Motor.A.rotateTo(measurement * FULL_TURN);
					}
					viColor=true;
					//FIN CAMBIO
				break;
				case Color.WHITE:
					//Las celestes muy claras
					if(blue > red)
						Motor.A.setSpeed(speed);
					else						
						Motor.A.setSpeed(SPEED_BLUE);
					//CAMBIO
					if(viColor){
						measurement = value < 0? measurement - 1:  measurement + 1;
						Motor.A.rotateTo(measurement * FULL_TURN);
					}
					viColor=true;	
					//FIN CAMBIO
				break;

				case Color.BLACK:
					if((red > 20) && (blue > 20) && (green > 20)){//Vi algo
						if((red > 90) && (blue > 90) && (green > 90))
							Motor.A.setSpeed(speed);
						else						
							Motor.A.setSpeed(SPEED_BLUE);
						//CAMBIO
						if(viColor){
							measurement = value < 0? measurement - 1:  measurement + 1;
							Motor.A.rotateTo(measurement * FULL_TURN);
						}
						viColor=true;
						
					}else
						viColor=false;
						//FIN CAMBIO
					//sino es que vio nada y entonces no hace nada
				break;

				default:
					//ES POSIBLE QUE SOLO SACANDO ESTO SE ARREGLARA, O QUEDARA BASTANTE MEJOR
					//Vio algo que no era pelotita azul ni celeste
					Motor.A.setSpeed(speed);
					//CAMBIO
					if(viColor){
						measurement = value < 0? measurement - 1:  measurement + 1;
						Motor.A.rotateTo(measurement * FULL_TURN);
					}
					viColor=true;
					//FIN CAMBIO
				break;	
						
			}
			
			//cambiar delay por while -30>tacho-360*medidas
			//CAMBIO
			Delay.msDelay(1800);//Baje la cantidad ya que ahora mira 2 veces
			/*do{ //CREO ASI FUNCA BIEN, CAMBIE LOS 0 POR 5, PERO IGUAL SIGUE SIENDO INESTABLE SI GIRA DEMASIADO, O SI ALGO LO MUEVE 
				util=(Motor.A.getTachoCount() - 360 * measurement);
			}while (((util > -30) && (-5>util)) || ((30 > util)&&(util>5)));*/
			//FIN CAMBIO
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
				shot();
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
