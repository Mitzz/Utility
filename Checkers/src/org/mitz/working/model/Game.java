package org.mitz.working.model;

import java.util.Set;

public class Game {

	private Player player1;
	private Player player2;
	private Board board;
	private boolean isRedTurn;
	
	public Game(Player player1, Player player2, Board board) {
		super();
		this.player1 = player1;
		this.player2 = player2;
		this.board = board;
		isRedTurn = true;
	}
	
	public Board board() {
		return board;
	}
	
	public Set<Cell> getAvailableMove(){
		return board.getAvailableMove(isRedTurn);
	}
	
	public Set<Cell> getMovablePieceCells(){
		return board.getMovablePieceCells(isRedTurn);
	}
	
	public boolean isSelectedPieceMovable(Cell cell) {
		return board.isSelectedPieceMovable(isRedTurn, cell);
	}

	public Set<Cell> getPieceNextMoveCells(Cell cell) {
		return board.getPieceNextMoveCells(isRedTurn, cell);
	}

	public void move(Cell from, Cell to) {
		boolean previousJump = board.isJumpPossible(from);
		board.movePiece(from, to);
		boolean isPieceBecameKing = board.isPieceBecameKing(to); 
		if(isPieceBecameKing){
			isRedTurn = !isRedTurn;
			return;
		}
		if(!(previousJump && board.isJumpPossible(to))){
			isRedTurn = !isRedTurn;
		}
	}
	
}
