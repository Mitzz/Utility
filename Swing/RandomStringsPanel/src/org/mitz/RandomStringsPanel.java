package org.mitz;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;

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
		int componentWidth = getWidth();
		int componentHeight = getHeight();
		String str = "Hello World";
		Font[] allFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
		
		
		RandomRange colorRange = new RandomRange(0, 255);
		RandomRange fontRange = new RandomRange(0, allFonts.length - 1);
		RandomRange fontSizeRange = new RandomRange(10, 40, 4);
		RandomRange xAxisRange = new RandomRange(0, componentWidth - (str.length() * 6));
		RandomRange yAxisRange = new RandomRange(0, componentHeight);
		
		Font randomFont = allFonts[fontRange.get()];
		int fontSize = fontSizeRange.get();
		
		Color strColor = new Color(colorRange.get(), colorRange.get(), colorRange.get());
		Font strFont =  new Font(randomFont.getFontName(), Font.ITALIC, fontSize);
		
		g.setColor(strColor);
		g.setFont(strFont);
		g.drawString(str, xAxisRange.get(), yAxisRange.get());
	}
}
