package org.mitz.working.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mitz.working.model.Piece.PieceType;

import utility.CollectionUtility;
import utility.NumberUtility;

public class Board {

	private Tile[][] tiles;
	private final int ROW = 8;
	private final int COLUMN = 8;
	
	public Board(){
		tiles = new Tile[ROW][COLUMN];
		for(int r = 0; r < ROW; r++){
			for(int c = 0; c < COLUMN; c++){
				Piece piece = null;
				if(r == 0 || r == 1 || r == 2){
					if((r + c) % 2 != 0){
						piece = new Piece(PieceType.RED_PIECE);
					} 
				} 
				else if (r == 5 || r == 6 || r == 7){
					if((r + c) % 2 != 0){
						piece = new Piece(PieceType.BLACK_PIECE);
					}
				} else {
					
				}
				tiles[r][c] = new Tile(r, c, piece);
			}
		}
	}
	
	public Piece getPiece(int r, int c){
		return tiles[r][c].piece();
	}
	
	public Set<Cell> getAvailableMove(boolean isRedPieceTurn) {
		
		return getRedPieceMovableCell(isRedPieceTurn);
	}
	
	private Set<Cell> getRedPieceMovableCell(boolean isRedPieceTurn){
		Set<Cell> movableCell = new HashSet<Cell>();
		for(int r = 0; r < ROW; r++){
			for(int c = 0; c < COLUMN; c++){
				if(isRedPieceTurn && isRedPiece(r, c))
					movableCell.addAll(getValidMove(r, c));
			}
		}
		return movableCell;
	}
	
	public boolean isCellEmpty(Cell c){
		 return tiles[c.row][c.column].piece() == null;
	}
	
	public boolean isTileEmpty(int r, int c){
		 return tiles[r][c].piece() == null;
	}
	
	private boolean isRedPiece(int r, int c){
		return (tiles[r][c].piece() != null && tiles[r][c].piece().pieceType() == PieceType.RED_PIECE);
	}
	
	private boolean isBlackPiece(int r, int c){
		return (tiles[r][c].piece() != null && tiles[r][c].piece().pieceType() == PieceType.BLACK_PIECE);
	}
	
	public Tile getTile(Cell cell){
		return tiles[cell.row][cell.column];
	}
	
	public Tile getTile(int r, int c){
		return tiles[r][c];
	}
	
	private Set<Cell> getValidMove(int r, int c){
		Set<Cell> validMove = new HashSet<Cell>();
		for(Cell cell: tiles[r][c].piece().getNextMoves())
			if(isCellEmpty(cell)) validMove.add(cell);
		
		return validMove;
	}
	
	private boolean isPieceMovable(Piece p){
		Set<Cell> availableMoveCells = p.getNextMoves();
		boolean tileEmpty = false;
		for(Cell availableMoveCell: availableMoveCells){
			if(getTile(availableMoveCell).isPieceAbsent()){
				tileEmpty = true;
				break;
			}
		}
		return tileEmpty;	
	}
	
	private List<Tile> getMovableRedPieceTile(){
		return getMovablePiece(PieceType.RED_PIECE, PieceType.RED_KING_PIECE);
	}
	
	private List<Tile> getMovablePiece(PieceType ... pieceTypes){
		List<Tile> redPieceTiles = new ArrayList<Tile>();
		for(PieceType pieceType: pieceTypes)
			redPieceTiles.addAll(getAllTile(pieceType));
		return getMovableTiles(redPieceTiles);
	}
	
	private List<Tile> getMovableBlackPieceTile(){
		return getMovablePiece(PieceType.BLACK_PIECE, PieceType.BLACK_KING_PIECE);
	}
	
	private List<Tile> getMovableTiles(List<Tile> pieceOccupiedTiles){
		List<Tile> movableTiles = new ArrayList<Tile>();
		movableTiles = getJumpTiles(pieceOccupiedTiles);
		if(movableTiles.size() > 0){
			return movableTiles;
		}
		for(Tile pieceOccupiedTile: pieceOccupiedTiles){
			if(isPieceMovable(pieceOccupiedTile.piece()))
				movableTiles.add(pieceOccupiedTile);
		}
		return movableTiles;
	}
	
	private List<Tile> getJumpTiles(List<Tile> pieceOccupiedTiles) {
		List<Tile> jumpTiles = new ArrayList<Tile>();
		for(Tile pieceOccupiedTile: pieceOccupiedTiles){
			if(canJump(pieceOccupiedTile.piece())){
				jumpTiles.add(pieceOccupiedTile);
			}
		}
		return jumpTiles;
	}

	private boolean canJump(Piece piece){
		PieceType pieceType = piece.pieceType();
		Tile tile = piece.tile();
		int pieceRow = tile.row();
		int pieceColumn = tile.column();
		int row1 = -1;
		int col1 = -1;
		int row2 = -1;
		int col2 = -1;
		if(pieceType == PieceType.RED_PIECE){
			row1 = pieceRow + 1;
			col1 = pieceColumn + 1;
			row2 = row1 + 1;
			col2 = col1 + 1;
			if(NumberUtility.isInRange(0, 7, row1, col1) && NumberUtility.isInRange(0, 7, row2, col2) && tiles[row1][col1].isPiecePresent() && tiles[row2][col2].isPieceAbsent()  && (tiles[row1][col1].piece().pieceType() == PieceType.BLACK_PIECE || tiles[row1][col1].piece().pieceType() == PieceType.BLACK_KING_PIECE)){
				return true;
			}
			row1 = pieceRow + 1;
			col1 = pieceColumn - 1;
			row2 = row1 + 1;
			col2 = col1 - 1;
			if(NumberUtility.isInRange(0, 7, row1, col1) && NumberUtility.isInRange(0, 7, row2, col2) && !tiles[row1][col1].isPieceAbsent() && tiles[row2][col2].isPieceAbsent()  && (tiles[row1][col1].piece().pieceType() == PieceType.BLACK_PIECE || tiles[row1][col1].piece().pieceType() == PieceType.BLACK_KING_PIECE)){
				return true;
			}
		} else if(pieceType == PieceType.BLACK_PIECE){
			row1 = pieceRow - 1;
			col1 = pieceColumn + 1;
			row2 = row1 - 1;
			col2 = col1 + 1;
			if(NumberUtility.isInRange(0, 7, row1, col1) && NumberUtility.isInRange(0, 7, row2, col2) && !tiles[row1][col1].isPieceAbsent() && tiles[row2][col2].isPieceAbsent()  && (tiles[row1][col1].piece().pieceType() == PieceType.RED_PIECE || tiles[row1][col1].piece().pieceType() == PieceType.RED_KING_PIECE)){
				return true;
			}
			row1 = pieceRow - 1;
			col1 = pieceColumn - 1;
			row2 = row1 - 1;
			col2 = col1 - 1;
			if(NumberUtility.isInRange(0, 7, row1, col1) && NumberUtility.isInRange(0, 7, row2, col2) && !tiles[row1][col1].isPieceAbsent() && tiles[row2][col2].isPieceAbsent()  && (tiles[row1][col1].piece().pieceType() == PieceType.RED_PIECE || tiles[row1][col1].piece().pieceType() == PieceType.RED_KING_PIECE)){
				return true;
			}
		} else if(pieceType == PieceType.RED_KING_PIECE){
			row1 = pieceRow + 1;
			col1 = pieceColumn + 1;
			row2 = row1 + 1;
			col2 = col1 + 1;
			if(NumberUtility.isInRange(0, 7, row1, col1) && NumberUtility.isInRange(0, 7, row2, col2) && !tiles[row1][col1].isPieceAbsent() && tiles[row2][col2].isPieceAbsent()  && (tiles[row1][col1].piece().pieceType() == PieceType.BLACK_PIECE || tiles[row1][col1].piece().pieceType() == PieceType.BLACK_KING_PIECE)){
				return true;
			}
			row1 = pieceRow + 1;
			col1 = pieceColumn - 1;
			row2 = row1 + 1;
			col2 = col1 - 1;
			if(NumberUtility.isInRange(0, 7, row1, col1) && NumberUtility.isInRange(0, 7, row2, col2) && !tiles[row1][col1].isPieceAbsent() && tiles[row2][col2].isPieceAbsent()  && (tiles[row1][col1].piece().pieceType() == PieceType.BLACK_PIECE || tiles[row1][col1].piece().pieceType() == PieceType.BLACK_KING_PIECE)){
				return true;
			}
			
			row1 = pieceRow - 1;
			col1 = pieceColumn - 1;
			row2 = row1 - 1;
			col2 = col1 - 1;
			if(NumberUtility.isInRange(0, 7, row1, col1) && NumberUtility.isInRange(0, 7, row2, col2) && !tiles[row1][col1].isPieceAbsent() && tiles[row2][col2].isPieceAbsent()  && (tiles[row1][col1].piece().pieceType() == PieceType.BLACK_PIECE || tiles[row1][col1].piece().pieceType() == PieceType.BLACK_KING_PIECE)){
				return true;
			}
			row1 = pieceRow - 1;
			col1 = pieceColumn + 1;
			row2 = row1 - 1;
			col2 = col1 + 1;
			if(NumberUtility.isInRange(0, 7, row1, col1) && NumberUtility.isInRange(0, 7, row2, col2) && !tiles[row1][col1].isPieceAbsent() && tiles[row2][col2].isPieceAbsent()  && (tiles[row1][col1].piece().pieceType() == PieceType.BLACK_PIECE || tiles[row1][col1].piece().pieceType() == PieceType.BLACK_KING_PIECE)){
				return true;
			}
		} else if(pieceType == PieceType.BLACK_KING_PIECE){
			row1 = pieceRow + 1;
			col1 = pieceColumn + 1;
			row2 = row1 + 1;
			col2 = col1 + 1;
			if(NumberUtility.isInRange(0, 7, row1, col1) && NumberUtility.isInRange(0, 7, row2, col2) && !tiles[row1][col1].isPieceAbsent() && tiles[row2][col2].isPieceAbsent()  && (tiles[row1][col1].piece().pieceType() == PieceType.RED_PIECE || tiles[row1][col1].piece().pieceType() == PieceType.RED_KING_PIECE)){
				return true;
			}
			row1 = pieceRow + 1;
			col1 = pieceColumn - 1;
			row2 = row1 + 1;
			col2 = col1 - 1;
			if(NumberUtility.isInRange(0, 7, row1, col1) && NumberUtility.isInRange(0, 7, row2, col2) && !tiles[row1][col1].isPieceAbsent() && tiles[row2][col2].isPieceAbsent()  && (tiles[row1][col1].piece().pieceType() == PieceType.RED_PIECE || tiles[row1][col1].piece().pieceType() == PieceType.RED_KING_PIECE)){
				return true;
			}
			
			row1 = pieceRow - 1;
			col1 = pieceColumn - 1;
			row2 = row1 - 1;
			col2 = col1 - 1;
			if(NumberUtility.isInRange(0, 7, row1, col1) && NumberUtility.isInRange(0, 7, row2, col2) && !tiles[row1][col1].isPieceAbsent() && tiles[row2][col2].isPieceAbsent()  && (tiles[row1][col1].piece().pieceType() == PieceType.RED_PIECE || tiles[row1][col1].piece().pieceType() == PieceType.RED_KING_PIECE)){
				return true;
			}
			row1 = pieceRow - 1;
			col1 = pieceColumn + 1;
			row2 = row1 - 1;
			col2 = col1 + 1;
			if(NumberUtility.isInRange(0, 7, row1, col1) && NumberUtility.isInRange(0, 7, row2, col2) && !tiles[row1][col1].isPieceAbsent() && tiles[row2][col2].isPieceAbsent()  && (tiles[row1][col1].piece().pieceType() == PieceType.RED_PIECE || tiles[row1][col1].piece().pieceType() == PieceType.RED_KING_PIECE)){
				return true;
			}
		} 
		return false;
	}
	
	private List<Tile> getAllBlackPieceTiles() {
		return getAllTile(PieceType.BLACK_PIECE);
	}

	private List<Tile> getAllRedPieceTiles() {
		return getAllTile(PieceType.RED_PIECE);
	}
	
	private List<Piece> getAllRedPieces() {
		return getAllPieces(PieceType.RED_PIECE);
	}
	
	private List<Piece> getAllPieces(PieceType pieceType) {
		List<Piece> pieces = new ArrayList<Piece>();
		for(int i = 0; i < ROW; i++){
			for(int j = 0; j < COLUMN; j++){
				if(tiles[i][j].piece().pieceType() == pieceType){
					pieces.add(tiles[i][j].piece());
				}
			}
		}
		return pieces;
	}
	
	private List<Tile> getAllTile(PieceType pieceType) {
		List<Tile> tileList = new ArrayList<Tile>();
		for(int i = 0; i < ROW; i++){
			for(int j = 0; j < COLUMN; j++){
				if(tiles[i][j].isPiecePresent() && tiles[i][j].piece().pieceType() == pieceType){
					tileList.add(tiles[i][j]);
				}
			}
		}
		return tileList;
	}

	public static void main(String[] args) {
		Board board = new Board();
		CollectionUtility.displayCollection(board.getAvailableMove(true), "\n");
	}

	public Set<Cell> getMovablePieceCells(boolean isRedTurn) {
		if(isRedTurn){
			return convert(getMovableRedPieceTile());
		} else {
			return convert(getMovableBlackPieceTile());
		}
	}

	private Set<Cell> convert(Collection<Tile> col){
		Set<Cell> cells = new HashSet<Cell>();
		for(Tile tile: col){
			cells.add(new Cell(tile.row(), tile.column()));
		}
		return cells;
	}

	public boolean isSelectedPieceMovable(boolean isRedTurn, Cell cell) {
		return getMovablePieceCells(isRedTurn).contains(cell);
	}
	
	public Set<Cell> getMovableTile(boolean isRedTurn, Cell cell) {
		if(isRedTurn){
			return convert(getMovableRedPieceTile(cell));
		}
		return null;
	}

	private Collection<Tile> getMovableRedPieceTile(Cell cell) {
		List<Tile> tiles = new ArrayList<Tile>();
		for(Tile tile: getAllRedPieceTiles()){
			if(isPieceMovable(tile.piece()))
				tiles.add(tile);
		}
		return tiles;
	}

	public Set<Cell> getPieceNextMoveCells(boolean isRedTurn, Cell cell) {
		Set<Cell> pieceNextMoveCells = new HashSet<Cell>();
		Piece piece = tiles[cell.row][cell.column].piece();
		if(canJump(piece)){
			List<Tile> jumpTiles = getJumpTile(piece);
			for(Tile tile: jumpTiles)
				pieceNextMoveCells.add(new Cell(tile.row(), tile.column()));
		}
		else {
			for(Cell movableCell: piece.getNextMoves()){
				if(getTile(movableCell).isPieceAbsent()) pieceNextMoveCells.add(movableCell);
			}
		}
		return pieceNextMoveCells;
	}

	private List<Tile> getJumpTile(Piece piece) {
		PieceType pieceType = piece.pieceType();
		List<Tile> jumpTiles = new ArrayList<Tile>();
		Tile tile = piece.tile();
		int tileRow = tile.row();
		int tileCol = tile.column();
		int row1 = - 1, col1 = -1, row2 = -1, col2 = -1;
		if(pieceType == PieceType.RED_PIECE || pieceType == PieceType.RED_KING_PIECE){
			row1 = tileRow + 1;
			col1 = tileCol + 1;
			row2 = row1 + 1;
			col2 = col1 + 1;
			if(NumberUtility.isInRange(0, 7, row1, col1) && NumberUtility.isInRange(0, 7, row2, col2) && !tiles[row1][col1].isPieceAbsent() && tiles[row2][col2].isPieceAbsent()  && (tiles[row1][col1].piece().pieceType() == PieceType.BLACK_PIECE || tiles[row1][col1].piece().pieceType() == PieceType.BLACK_KING_PIECE)){
				jumpTiles.add(tiles[row2][col2]);
			}
			row1 = tileRow + 1;
			col1 = tileCol - 1;
			row2 = row1 + 1;
			col2 = col1 - 1;
			if(NumberUtility.isInRange(0, 7, row1, col1) && NumberUtility.isInRange(0, 7, row2, col2) && !tiles[row1][col1].isPieceAbsent() && tiles[row2][col2].isPieceAbsent()  && (tiles[row1][col1].piece().pieceType() == PieceType.BLACK_PIECE || tiles[row1][col1].piece().pieceType() == PieceType.BLACK_KING_PIECE)){
				jumpTiles.add(tiles[row2][col2]);
			}
			
		}
		if(pieceType == PieceType.RED_KING_PIECE){
			row1 = tileRow - 1;
			col1 = tileCol + 1;
			row2 = row1 - 1;
			col2 = col1 + 1;
			if(NumberUtility.isInRange(0, 7, row1, col1) && NumberUtility.isInRange(0, 7, row2, col2) && !tiles[row1][col1].isPieceAbsent() && tiles[row2][col2].isPieceAbsent()  && (tiles[row1][col1].piece().pieceType() == PieceType.BLACK_PIECE || tiles[row1][col1].piece().pieceType() == PieceType.BLACK_KING_PIECE)){
				jumpTiles.add(tiles[row2][col2]);
			}
			row1 = tileRow - 1;
			col1 = tileCol - 1;
			row2 = row1 - 1;
			col2 = col1 - 1;
			if(NumberUtility.isInRange(0, 7, row1, col1) && NumberUtility.isInRange(0, 7, row2, col2) && !tiles[row1][col1].isPieceAbsent() && tiles[row2][col2].isPieceAbsent()  && (tiles[row1][col1].piece().pieceType() == PieceType.BLACK_PIECE || tiles[row1][col1].piece().pieceType() == PieceType.BLACK_KING_PIECE)){
				jumpTiles.add(tiles[row2][col2]);
			}
			
		}
		if(pieceType == PieceType.BLACK_PIECE || pieceType == PieceType.BLACK_KING_PIECE){
			row1 = tileRow - 1;
			col1 = tileCol + 1;
			row2 = row1 - 1;
			col2 = col1 + 1;
			if(NumberUtility.isInRange(0, 7, row1, col1) && NumberUtility.isInRange(0, 7, row2, col2) && !tiles[row1][col1].isPieceAbsent() && tiles[row2][col2].isPieceAbsent()  && (tiles[row1][col1].piece().pieceType() == PieceType.RED_PIECE || tiles[row1][col1].piece().pieceType() == PieceType.RED_KING_PIECE)){
				jumpTiles.add(tiles[row2][col2]);
			}
			row1 = tileRow - 1;
			col1 = tileCol - 1;
			row2 = row1 - 1;
			col2 = col1 - 1;
			if(NumberUtility.isInRange(0, 7, row1, col1) && NumberUtility.isInRange(0, 7, row2, col2) && !tiles[row1][col1].isPieceAbsent() && tiles[row2][col2].isPieceAbsent()  && (tiles[row1][col1].piece().pieceType() == PieceType.RED_PIECE || tiles[row1][col1].piece().pieceType() == PieceType.RED_KING_PIECE)){
				jumpTiles.add(tiles[row2][col2]);
			}
		}
		if(pieceType == PieceType.BLACK_KING_PIECE){
			row1 = tileRow + 1;
			col1 = tileCol + 1;
			row2 = row1 + 1;
			col2 = col1 + 1;
			if(NumberUtility.isInRange(0, 7, row1, col1) && NumberUtility.isInRange(0, 7, row2, col2) && !tiles[row1][col1].isPieceAbsent() && tiles[row2][col2].isPieceAbsent()  && (tiles[row1][col1].piece().pieceType() == PieceType.RED_PIECE || tiles[row1][col1].piece().pieceType() == PieceType.RED_KING_PIECE)){
				jumpTiles.add(tiles[row2][col2]);
			}
			row1 = tileRow + 1;
			col1 = tileCol - 1;
			row2 = row1 + 1;
			col2 = col1 - 1;
			if(NumberUtility.isInRange(0, 7, row1, col1) && NumberUtility.isInRange(0, 7, row2, col2) && !tiles[row1][col1].isPieceAbsent() && tiles[row2][col2].isPieceAbsent()  && (tiles[row1][col1].piece().pieceType() == PieceType.RED_PIECE || tiles[row1][col1].piece().pieceType() == PieceType.RED_KING_PIECE)){
				jumpTiles.add(tiles[row2][col2]);
			}
		}
		return jumpTiles;
		
	}
	
	public void movePiece(Cell from, Cell to) {
		Tile fromTile = getTile(from);
		Tile toTile = getTile(to);
		movePiece(fromTile, toTile);
	}

	private void movePiece(Tile from, Tile to) {
		Piece piece = from.piece();
		JUMP_DIRECTION direction = null;
		if(canJump(piece)){
			direction = getJumpDirection(from, to);
			removePieceByJump(piece, direction);
		}
		piece.moveTo(to);
	}
	
	private JUMP_DIRECTION getJumpDirection(Tile from, Tile to) {
		int fromRow = from.row();
		int fromColumn = from.column();
		
		int toRow = to.row();
		int toColumn = to.column();
		
		if(fromRow > toRow && fromColumn > toColumn) return JUMP_DIRECTION.DOWN_LEFT;
		if(fromRow > toRow && fromColumn < toColumn) return JUMP_DIRECTION.DOWN_RIGHT;
		if(fromRow < toRow && fromColumn < toColumn) return JUMP_DIRECTION.UP_RIGHT;
		if(fromRow < toRow && fromColumn > toColumn) return JUMP_DIRECTION.UP_LEFT;
		
		return null;
	}

	private void removePieceByJump(Piece piece, JUMP_DIRECTION direction) {
		int removeRow = -1;
		int removeColumn = -1;
		if(direction == JUMP_DIRECTION.UP_LEFT){
			removeRow = piece.tile().row() + 1;
			removeColumn = piece.tile().column() - 1;
		}
		if(direction == JUMP_DIRECTION.UP_RIGHT){
			removeRow = piece.tile().row() + 1;
			removeColumn = piece.tile().column() + 1;
		}
		if(direction == JUMP_DIRECTION.DOWN_LEFT){
			removeRow = piece.tile().row() - 1;
			removeColumn = piece.tile().column() - 1;
		}
		if(direction == JUMP_DIRECTION.DOWN_RIGHT){
			removeRow = piece.tile().row() - 1;
			removeColumn = piece.tile().column() + 1;
		}
		tiles[removeRow][removeColumn].piece().remove();
	}

	private List<Tile> getPieceNextMoveTiles(Set<Cell> cells){
		List<Tile> tiles = new ArrayList<Tile>();
		for(Cell cell: cells){
			tiles.add(getTile(cell));
		}
		return tiles;
	}
	
	public enum JUMP_DIRECTION{
		UP_RIGHT, UP_LEFT, DOWN_RIGHT, DOWN_LEFT;
	}

	public boolean isJumpPossible(Cell to) {
		return canJump(getTile(to).piece());
	}

	public boolean isPieceBecameKing(Cell to) {
		boolean isPieceKing = false;
		Piece piece = getTile(to).piece();
		if(piece.canBecomeKing()){
			piece.becomeKing();
			isPieceKing = true;
		}
		return isPieceKing;
	}
}
