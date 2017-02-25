package com.rpise;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;

import utility.FileUtility;

public class Program {
	public static final String TITLE = "SWING Text Editor";
	public static File currentFile = null;  
	public static boolean isEditing = false;

	public static void main(String[] args) {
		final JFrame frame = new JFrame(TITLE);
		JTextArea ta = new JTextArea();

		ta.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				if(!Program.isEditing){
					if(Program.currentFile == null)
						frame.setTitle("* Untitled " + frame.getTitle());
					else 
						frame.setTitle("* " + frame.getTitle());
					isEditing = true;
				} 
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
//				System.out.println("Key Released");
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
//				System.out.println("Key Pressed");
			}
		});
		JMenuBar menuBar = new JMenuBar();

		JMenu m = new JMenu("File");

		JMenuItem openFileMenuItem = new JMenuItem("Open");
		JMenuItem newFileMenuItem = new JMenuItem("New");
		JMenuItem saveFileMenuItem = new JMenuItem("Save");
		
		m.add(newFileMenuItem);
		m.add(openFileMenuItem);
		m.add(saveFileMenuItem);
		
		NewFile createFile = new NewFile(ta, frame);
		newFileMenuItem.addActionListener(createFile);
		
		OpenFile openFile = new OpenFile(ta, frame);
		openFileMenuItem.addActionListener(openFile);
		
		SaveFile saveFileListener = new SaveFile(ta, frame);
		saveFileMenuItem.addActionListener(saveFileListener);
		
		menuBar.add(m);
		frame.setJMenuBar(menuBar);
		frame.add(ta);
		frame.setSize(400, 400);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

class NewFile implements ActionListener{
	
	private JTextArea ta;
	private JFrame frame;

	public NewFile(JTextArea ta, JFrame frame) {
		this.ta = ta;
		this.frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		frame.setTitle("* Untitled " + Program.TITLE);
		ta.setText("");
		Program.currentFile = null;
		Program.isEditing = true;
	}
	
}

class OpenFile implements ActionListener {
	private JTextArea ta;
	private JFrame f;

	public OpenFile(JTextArea ta, JFrame f) {
		this.ta = ta;
		this.f = f;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		JFileChooser fileChooser = new JFileChooser();
		//fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showOpenDialog(this.f);

		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			Program.currentFile = selectedFile;
			try {
				ta.setText(FileUtility.readFile(selectedFile.getAbsolutePath()));
				f.setTitle(fileChooser.getSelectedFile() + " " + Program.TITLE);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			Program.isEditing = false;
		}
	}
}

class SaveFile implements ActionListener {
	private JTextArea ta;
	private JFrame frame;

	public SaveFile(JTextArea ta, JFrame f) {
		this.ta = ta;
		this.frame = f;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		File f = Program.currentFile;
		if(f != null){
			try {
				FileUtility.writeToFile(f.getAbsolutePath(), ta.getText());
				frame.setTitle(f.getAbsolutePath() + " " + Program.TITLE);
				Program.isEditing = false;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else {
			JFileChooser fileChooser = new JFileChooser();
			int result = fileChooser.showSaveDialog(frame);
			if (result == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				try {
					FileUtility.writeToFile(selectedFile.getAbsolutePath(), ta.getText());
					frame.setTitle(selectedFile.getAbsolutePath() + " " + Program.TITLE);
					Program.currentFile = selectedFile;
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				Program.isEditing = false;
			}
		}
		
	}
}