package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class timer {
	int second = 0;
	int minute = 60;
	Timer timer = new Timer(1000, new ActionListener() {
		
		public void actionPerformed(ActionEvent e) {
			second--;
			if(second==-1) {
				second = 59;
				minute--;
			}
			if (minute==0 && second==0) {
				timer.stop();
				//call endgame
			}
			
		}
	});
	
	public void startTimer() {
		timer.start();
	}
	
	public void addTimer() {
		minute += 5;
	}
	
	
}