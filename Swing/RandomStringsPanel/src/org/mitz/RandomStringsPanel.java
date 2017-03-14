package org.mitz;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class RandomStringsPanel {

	public static void main(String[] args) {
		JFrame window = new JFrame("String panel");
		
		JPanel drawingArea = new JPanel();
		
		window.setContentPane(drawingArea);
		window.setSize(500, 300);
		window.setLocation(20, 20);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);;
	}
	
}
