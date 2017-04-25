package org.mitz.working;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.mitz.working.model.Board;
import org.mitz.working.model.Cell;
import org.mitz.working.model.Game;
import org.mitz.working.model.Piece;
import org.mitz.working.model.Piece.PieceType;
import org.mitz.working.model.Player;

import david.eck.common.RectangleComponent;

public class CheckersDemo extends JPanel{

	public static void main(String[] args) {
		JFrame window = new JFrame("Checkerboard Game");
		CheckersDemo contentPane = new CheckersDemo();
		
		window.setContentPane(contentPane);
		
		window.setSize(500, 400);
		window.setLocation(20, 20);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);

	}
	
	public CheckersDemo(){
		setLayout(null);
		setBackground(new Color(0, 100, 0));
		Checkerboard board = new Checkerboard();
		board.addMouseListener(board);
		board.setBounds(25, 25, 204, 204);
		add(board);
		
		JButton newGame = new JButton("New Game");
		newGame.setBounds(250, 70, 170, 40);
		newGame.addActionListener(board);
		add(newGame);
		
		JButton resign = new JButton("Resign");
		resign.setBounds(250, 145, 170, 40);
		add(resign);
		
		JLabel label = new JLabel("Click 'New Game' to Begin");
		label.setForeground(Color.GREEN);
		label.setFont(new Font("Serif", Font.BOLD, 16));
		label.setBounds(25, 250, 275, 40);
		add(label);
	}
	
	private static class Checkerboard extends JPanel implements MouseListener, ActionListener{
		private Game game;
		private Cell currentCellSelected;
		private Cell previousCellSelected;
		private int squareSize = 25;
		private int pieceSize = 17;
		private int xOffset = 2;
		private int yOffset = 2;
		
		public Checkerboard() {
			init();
			setPreferredSize(new Dimension(204, 204));
		}
		
		public void init(){
			previousCellSelected = new Cell(-1, -2);
			currentCellSelected = new Cell(-1, -2);
			Board board = new Board();
			Player player1 = new Player("Player 1");
			Player player2 = new Player("Player 2");
			game = new Game(player1, player2, board);
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponents(g);
			drawBoard(g);
			displayMovablePiece(g);
			drawSelectedPieceNextMove(g);
			drawMove(g);
		}
		
		private void drawMove(Graphics g) {
			if(game.isSelectedPieceMovable(previousCellSelected)){
				if(game.getPieceNextMoveCells(previousCellSelected).contains(currentCellSelected)){
					game.move(previousCellSelected, currentCellSelected);
					drawBoard(g);
					displayMovablePiece(g);
				}
			}
		}

		private void drawSelectedPieceNextMove(Graphics g) {
			if(game.isSelectedPieceMovable(currentCellSelected)){
				Set<Cell> pieceNextMoveCells = game.getPieceNextMoveCells(currentCellSelected);
				for(Cell pieceNextMoveCell: pieceNextMoveCells)
					drawBorderedCell(g, Color.WHITE, pieceNextMoveCell);
			}
		}
		
		private void drawBorderedCell(Graphics g, Color borderColor, Cell cell) {
			RectangleComponent rectangleComponent = new RectangleComponent(cell.column * squareSize + xOffset, (7 - cell.row) * squareSize + yOffset, squareSize, squareSize);
			rectangleComponent.borderColor(borderColor);
			rectangleComponent.borderThickness(2);
			rectangleComponent.border(g);
		}

		private void displayMovablePiece(Graphics g) {
			Set<Cell> movablePieceCells = game.getMovablePieceCells();
			
			for(Cell cell: movablePieceCells)
				drawBorderedCell(g, Color.CYAN, cell);
			
		}

		private void drawBoard(Graphics g){
			g.setColor(Color.BLACK);
			
			for(int i = 0; i < xOffset; i++)
				g.drawRect(i, i, getSize().width - (i * 2 + 1), getSize().height - (i * 2 + 1));

            Board board = game.board();
			
			for(int r = 0; r < 8; r++){
				for(int c = 0; c < 8; c++){
					Color color = null;
					if((r + c) % 2 == 0) 	color = Color.GRAY;
					else 					color = Color.DARK_GRAY;
					RectangleComponent rectangleComponent = new RectangleComponent(c * squareSize + xOffset, r * squareSize + yOffset, squareSize, squareSize);
					rectangleComponent.interiorColor(color).draw(g);
					Piece piece = board.getPiece(7 - r, c);
					if(piece != null && piece.isRed())
						rectangleComponent.drawInnerCircle(g, pieceSize, Color.RED);
					else if(piece != null && !piece.isRed())
						rectangleComponent.drawInnerCircle(g, pieceSize, Color.BLACK);
					if(piece != null && piece.isKing())
						rectangleComponent.drawInnerCircle(g, 7, Color.WHITE);
				}
			}
		}
		
		

		@Override
		public void mouseClicked(MouseEvent e) {
			previousCellSelected = currentCellSelected;
			currentCellSelected = new Cell (7 -  e.getPoint().y / squareSize, e.getPoint().x / squareSize);
			repaint();
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			init();
			repaint();
		}
		
	}

}

