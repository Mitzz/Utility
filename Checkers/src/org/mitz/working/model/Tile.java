package org.mitz.working.model;

public class Tile {

	private int r;
	private int c;
	private Piece piece;
	
	public Tile(int r, int c, Piece piece) {
		this.r = r;
		this.c = c;
		this.piece = piece;
		if(piece != null) piece.setTile(this);
	}
	
	public Piece piece(){
		return piece;
	}
	
	public int row(){
		return r;
	}
	
	public int column(){
		return c;
	}
	
	public boolean isPieceAbsent(){
		return !isPiecePresent();
	}
	
	public boolean isPiecePresent(){
		return piece != null;
	}

	public void setPiece(Piece piece) {
		this.piece = piece;
	}
	
	
}
