package utilities;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JButton;

public class MidiFileInput {
	
	JButton start;
	JButton stop;
	boolean isInputOn;
	
	public MidiFileInput(Container contentPane) {
		isInputOn = false;
		addStartButton(contentPane);
		addStopButton(contentPane);
	}
	
	private void addStartButton(Container contentPane) {
		start = new JButton("Start");
		start.setBounds(300, 150, 90, 60);
		start.setEnabled(!isInputOn);
		start.setForeground(Color.BLUE);
		start.setBackground(Color.BLACK);
		contentPane.add(start);
	}
	
	private void addStopButton(Container contentPane) {
		stop = new JButton("Stop");
		stop.setBounds(300, 250, 90, 60);
		stop.setEnabled(isInputOn);
		stop.setForeground(Color.RED);
		start.setBackground(Color.GRAY);
		contentPane.add(stop);
	}
}
