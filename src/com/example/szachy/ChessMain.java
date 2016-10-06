package com.example.szachy;

import figures.*;

public class ChessMain {
	private final Figure[][] chessboard = new Figure[8][8];
	private Boolean[] castlingT = new Boolean[4];
	private Figure temp;
	private Integer[] select = new Integer[3];
	private Integer[] passant = new Integer[2];
	private char playerTurn;
	private Chessboard chessGui;
	private int x_, y_;

	public ChessMain() {
		init();
	}

	public void giveReference(Chessboard c) {
		chessGui = c;
	}

	public char getFigure(int x, int y) {       // returns type of the figure rook, knight etc.
		if (chessboard[x][y] != null)
			return chessboard[x][y].getFigure();
		else
			return 0;
	}

	public char getPlayer(int x, int y) {		//returns color of the figure
		if (chessboard[x][y] != null)
			return chessboard[x][y].getColor();
		else
			return 0;
	}

	void init() {
		
		select[0] = 0;		// figure not selected == 0
		select[1] = 0;
		select[2] = 0;
		passant[0]=10;		// allowed tile for en Passant passant[0] - column; passant[1] - row
		passant[1]=10;
		playerTurn = 'W';
		
		for (int i = 0; i < 8; i++)	
			for (int j = 0; j < 8; j++)	
				chessboard[i][j] = null;
		
		for (int i = 0; i < 4; i++)		// castling is allowed
			castlingT[i] = true;

		for (int x = 0; x < 8; ++x) {
			chessboard[x][6] = new Pawn('B');
		}
		chessboard[0][7] = new Rook('B');
		chessboard[1][7] = new Knight('B');
		chessboard[2][7] = new Bishop('B');
		chessboard[3][7] = new Queen('B');
		chessboard[4][7] = new King('B');
		chessboard[5][7] = new Bishop('B');
		chessboard[6][7] = new Knight('B');
		chessboard[7][7] = new Rook('B');
		for (int x = 0; x < 8; ++x) {
			chessboard[x][1] = new Pawn('W');
		}
		chessboard[0][0] = new Rook('W');
		chessboard[1][0] = new Knight('W');
		chessboard[2][0] = new Bishop('W');
		chessboard[4][0] = new King('W');
		chessboard[3][0] = new Queen('W');
		chessboard[5][0] = new Bishop('W');
		chessboard[6][0] = new Knight('W');
		chessboard[7][0] = new Rook('W');
		
	}

	public void handleTouch(int c, int r) {

		//select tile if not selected
		if (select[0] == 0 && chessboard[c][r] != null && playerTurn == chessboard[c][r].getColor()) {
			select[0] = 1;
			select[1] = c;
			select[2] = r;
			
		//if selected
		} else if (select[0] == 1) {

			if (checkMove(select[1], select[2], c, r)) { //if 'simple' move is possible

				makeMove(select[1], select[2], c, r, 1);
				
				if (isInCheck()) {
					undoMove(select[1], select[2], c, r);
					select[0] = 0;
					return;
				}

				int player = playerTurn == 'W' ? 0 : 2;
				
				// if player moved his King castling is no longer possible for him
				if (getFigure(c, r) == 'K') {
					castlingT[player] = false;
					castlingT[1 + player] = false;
				}
				
				// if rook moved then no longer can be used for castling
				if (getFigure(c, r) == 'R') {
					castlingT[((select[1] == 0) ? 0 : 1) + player] = false;
				}
				
				// checks if Pawn moved 2 tiles ? enPassant is allowed on it : clear allowed tile;
				enPassant(select[1], select[2], c, r);
				
				//in case of promotion wait for player to choose new figure else change turn
				if (!pawnPromotion(c, r))
					changeTurn();
				
				//if player wants to move King on tile occupied by rook check for castling
			} else if (getFigure(select[1], select[2]) == 'K' && getFigure(c, r) == 'R') {
				if (castling(select[1], select[2], c, r)) {
					makeMove(select[1], select[2], c, r, 2);
					changeTurn();
				}
				
				//checks for enPassant possibility
			} else if (enPassPoss(select[1], select[2], c, r)) {
				makeMove(select[1], select[2], c, r, 1);
				if (isInCheck()) {
					undoMove(select[1], select[2], c, r);
					select[0] = 0;
					return;
				}
				int tmp=passant[1]+(playerTurn == 'w' ? 1 : - 1);
				chessboard[c][tmp]=null;
				changeTurn();
			}
			System.out.println("p "+passant[0]+" "+passant[1]);
			select[0] = 0;
		}

	}

	private Boolean castling(int x0, int y0, int x1, int y1) {
		if (!(x1 == 0 || x1 == 7))
			return false;
		if (!(getFigure(select[1], select[2]) == 'K' && getFigure(x1, y1) == 'R'))
			return false;
		if (y0 != y1)
			return false;
		int pT = (playerTurn == 'W') ? 0 : 2;
		if ((x1 == 0 && castlingT[pT]) || (x1 == 7 && castlingT[1 + pT])) {
			if (x1 < x0) {
				int temp = x1;
				x1 = x0;
				x0 = temp;
			}
			for (int i = x0 + 1; i < x1; i++)
				if (chessboard[i][y0] != null || isAttacked(i, y0))
					return false;
			return true;
		}

		return false;
		
	}

	private void enPassant(Integer select2, Integer select3, int c, int r) {
		if(getFigure(c,r)=='P' && Math.abs(r-select3)==2){
			passant[0]=c;
			passant[1]=(select3+r)/2;
		}
		else
		{
			passant[0]=10;
			passant[1]=10;
		}
	}
	
	private Boolean enPassPoss(int x, int y, int c, int r){
		if(getFigure(x, y) == 'P' && passant[0] == c && passant[1] == r
				&& (x == c - 1 || x == c + 1)
				&& y == (playerTurn == 'w' ? passant[1] + 1 : passant[1] - 1))return true;
		return false;
	}

	private Boolean pawnPromotion(int x, int y) {
		if (getFigure(x, y) == 'P' && (y == 0 || y == 7)) {
			chessGui.dialog();
			x_ = x;
			y_ = y;
			return true;
		}
		return false;
	}

	public void promote(char piece) {
		switch (piece) {
		case 'Q':
			chessboard[x_][y_] = new Queen(playerTurn);
			break;
		case 'R':
			chessboard[x_][y_] = new Rook(playerTurn);
			break;
		case 'N':
			chessboard[x_][y_] = new Knight(playerTurn);
			break;
		case 'B':
			chessboard[x_][y_] = new Bishop(playerTurn);
			break;
		}
		changeTurn();
	}

	private Boolean checkMove(int x0, int y0, int x1, int y1) {

		if (chessboard[x0][y0] != null && chessboard[x0][y0].isAvailable(x0, y0, x1, y1, chessboard))
			return true;
		else
			return false;

	}

	private Boolean isAttacked(int x, int y) {

		for (int c = 0; c < 8; c++) {
			for (int r = 0; r < 8; r++) {
				if (chessboard[c][r] != null && getPlayer(c, r) != playerTurn) {
					if (chessboard[c][r].isAvailable(c, r, x, y, chessboard))
						return true;
				}
			}
		}

		return false;
	}

	public Boolean isInCheck() {
		for (int c = 0; c < 8; c++) {
			for (int r = 0; r < 8; r++) {
				if (getPlayer(c, r) == playerTurn && getFigure(c, r) == 'K' && isAttacked(c, r))
					return true;

			}
		}
		return false;
	}

	private void makeMove(int x0, int y0, int x1, int y1, int type) {
		switch (type) {
		//standard move
		case 1:
			temp = chessboard[x1][y1];
			chessboard[x1][y1] = chessboard[x0][y0];
			chessboard[x0][y0] = null;
			break;
		//castling
		case 2:
			int sign = 1;
			if (x1 == 7)
				sign = -1;
			chessboard[x1 + sign][y1] = chessboard[x0][y0];
			chessboard[x0 - sign][y1] = chessboard[x1][y1];
			chessboard[x0][y0] = null;
			chessboard[x1][y1] = null;
			break;
		}
	}

	private void undoMove(int x0, int y0, int x1, int y1) {
		chessboard[x0][y0] = chessboard[x1][y1];
		chessboard[x1][y1] = temp;

	}

	public Boolean isSelected(int c, int r) {
		if (select[0] == 1) {
			if (select[1] == c && select[2] == r)
				return true;
			if (checkMove(select[1], select[2], c, r))
				return true;
			if (castling(select[1], select[2], c, r))
				return true;
			if(enPassPoss(select[1], select[2], c, r))
				return true;
		}
		return false;
	}

	private void changeTurn() {
		playerTurn = playerTurn == 'W' ? 'B' : 'W';
	}

	public String getTurn(){
		return playerTurn == 'W' ? "White" : "Black";
	}
}
