package org.mitz.working.model;

import java.util.HashSet;
import java.util.Set;

import utility.NumberUtility;

public class Piece {

	private PieceType pieceType;
	private Tile tile;
	
	public Piece(PieceType pieceType) {
		this.pieceType = pieceType;
	}

	public enum PieceType{
		BLACK_PIECE, RED_PIECE, BLACK_KING_PIECE, RED_KING_PIECE;
	}

	@Override
	public String toString() {
		return "Piece [pieceType=" + pieceType + "]";
	}

	public PieceType pieceType() {
		return pieceType;
	}

	public Set<Cell> getNextMoves(){
		Set<Cell> availableCell = new HashSet<Cell>();
		int pieceRow = tile.row();
		int pieceColumn = tile.column();
		int availableRow  = -1;
		int availableColumn = -1;
		boolean isPieceKing = pieceType == PieceType.BLACK_KING_PIECE || pieceType == PieceType.RED_KING_PIECE;
		if(pieceType == PieceType.RED_PIECE || isPieceKing){
			availableRow = pieceRow + 1;
			availableColumn = pieceColumn + 1;
			if(NumberUtility.isInRange(0, 7, availableRow, availableColumn))
				availableCell.add(new Cell(availableRow, availableColumn));
			
			availableRow = pieceRow + 1;
			availableColumn = pieceColumn - 1;
			if(NumberUtility.isInRange(0, 7, availableRow, availableColumn))
				availableCell.add(new Cell(availableRow, availableColumn));
			
		} 
		if(pieceType == PieceType.BLACK_PIECE || isPieceKing){
			availableRow = pieceRow - 1;
			availableColumn = pieceColumn + 1;
			if(NumberUtility.isInRange(0, 7, availableRow, availableColumn))
				availableCell.add(new Cell(availableRow, availableColumn));
			
			availableRow = pieceRow - 1;
			availableColumn = pieceColumn - 1;
			if(NumberUtility.isInRange(0, 7, availableRow, availableColumn))
				availableCell.add(new Cell(availableRow, availableColumn));
			
		}
		return availableCell;
	}

	public Piece setTile(Tile tile) {
		this.tile = tile;
		return this;
	}
	
	public Tile tile() {
		return this.tile;
	}

	public void moveTo(Tile tile) {
		this.tile.setPiece(null);
		setTile(tile);
		tile.setPiece(this);
	}

	public void remove() {
		this.tile.setPiece(null);
		this.tile = null;
	}
	
	public boolean isKing(){
		return pieceType() == PieceType.BLACK_KING_PIECE || pieceType() == PieceType.RED_KING_PIECE;
	}
	
	public boolean isRed(){
		return pieceType() == PieceType.RED_PIECE || pieceType() == PieceType.RED_KING_PIECE;
	}
	
	

	public boolean canBecomeKing() {
		if(pieceType() == PieceType.RED_PIECE) 		return tile.row() == 7;
		if(pieceType() == PieceType.BLACK_PIECE) 	return tile.row() == 0;
		
		return false;
	}

	public void becomeKing() {
		if(pieceType() == PieceType.RED_PIECE) 		pieceType = PieceType.RED_KING_PIECE;
		if(pieceType() == PieceType.BLACK_PIECE) 	pieceType = PieceType.BLACK_KING_PIECE;
	}
}
