package test;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Date;

public class quey {

	public static void main(String[] args) {
		Timer t = new Timer();
		TimerTask tt = new TimerTask() {
			@Override
			public void run() {
				System.out.println("Task Timer on Fixed Rate");
			};
		};
		t.scheduleAtFixedRate(tt,113500,1000);
		
	}//main
		    

}
