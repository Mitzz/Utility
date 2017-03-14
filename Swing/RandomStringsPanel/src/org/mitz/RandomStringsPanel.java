package org.mitz;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class RandomStringsPanel extends JPanel{
	
	private final static int CHARACTER_HEIGHT_PIXEL = 8;
	private final static int CHARACTER_WIDTH_PIXEL = 6;

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
		int width = getWidth();
		int height = getHeight();
		String str = "Hello World";
		int strX = (width / 2) - (str.length() * CHARACTER_WIDTH_PIXEL / 2);
		int strY = (height / 2) + CHARACTER_HEIGHT_PIXEL / 2;

		g.drawString("Hello World", strX, strY);
	}
}
