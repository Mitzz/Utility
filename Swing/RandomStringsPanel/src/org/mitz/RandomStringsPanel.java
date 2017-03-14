package org.mitz;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class RandomStringsPanel extends JPanel{

	public static void main(String[] args) {
		JFrame window = new JFrame("String panel");
		
		JPanel drawingArea = new RandomStringsPanel();
		
		window.setContentPane(drawingArea);
		window.setSize(500, 300);
		window.setLocation(20, 20);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawString("Hello World", 10, 20);
	}
}
