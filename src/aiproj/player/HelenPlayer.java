package aiproj.player;

import aiproj.slider.Move;
import aiproj.slider.SliderPlayer;

public class HelenPlayer implements SliderPlayer {

	private int dimension;
	private Piece[][] board;
	private char player;

	public void init(int dimension, String board, char player) {
		this.dimension = dimension;
		this.board = initBoard(dimension, board);
		this.player = player;

	}

	public void update(Move move) {
		if (move != null) {
			int i = dimension - move.j - 1;
			int j = move.i;
			
			switch (move.d) {
			case UP:
				board[i - 1][j] = board[i][j];
				board[i][j] = Piece.BLANK;
				break;
			case DOWN:
				board[i + 1][j] = board[i][j];
				board[i][j] = Piece.BLANK;
				break;
			case RIGHT:
				board[i][j + 1] = board[i][j];
				board[i][j] = Piece.BLANK;
				break;
			case LEFT:
				board[i][j - 1] = board[i][j];
				board[i][j] = Piece.BLANK;
				break;
			}
		}
	}

	public Move move() {
		return null;
	}

	private Piece[][] initBoard(int dimension, String board) {
		Piece[][] grid = new Piece[dimension][dimension];

		int index = 0;
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				while (index < board.length() && toPiece(board.charAt(index)) == null)
					index++;
				grid[i][j] = toPiece(board.charAt(index));
				index++;
			}
		}

		return grid;
	}

	private Piece toPiece(char c) {
		Piece piece;

		switch (c) {
		case '+':
			piece = Piece.BLANK;
			break;
		case 'B':
			piece = Piece.BLOCK;
			break;
		case 'H':
			piece = Piece.HSLIDER;
			break;
		case 'V':
			piece = Piece.VSLIDER;
			break;
		default:
			piece = null;
			break;
		}

		return piece;
	}

	/** Enumeration of all of the possible states of a board position */
	private static enum Piece {
		BLANK, BLOCK, HSLIDER, VSLIDER,
	}

	public static void main(String[] args) {
		HelenPlayer helenPlayer = new HelenPlayer();
		helenPlayer.init(Integer.parseInt(args[0]), args[1], 'H');
	}
}
