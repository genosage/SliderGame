package aiproj.player;

import java.util.*;
import aiproj.slider.Move;
import aiproj.slider.Move.Direction;

public class HelenNegamax {

	private static final int DEPTH = 4;

	/** Enumeration of all of the possible states of a board position */
	private static enum Piece {
		BLANK, BLOCK, HSLIDER, VSLIDER
	}

	private static enum Player {
		HPLAYER, VPLAYER
	}

	private final class MoveWrapper {
		Move move;
	}

	private final int dimension;
	private final Piece[][] board;
	private Player player;

	public HelenNegamax(int dimension, String board, char player) {
		this.dimension = dimension;
		this.board = initBoard(board);
		this.player = player == 'H' ? Player.HPLAYER : Player.VPLAYER;
	}

	private Piece[][] initBoard(String board) {
		Piece[][] grid = new Piece[dimension][dimension];

		int index = 0;
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				while (toPiece(board.charAt(index)) == null) {
					index++;
				}
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

	public void update(Move move) {
		if (move != null) {
			move = convertMove(move);
			makeMove(move);
			previousPlayer();
		}
	}

	private Move convertMove(Move move) {
		return new Move(dimension - move.j - 1, move.i, move.d);
	}

	private Move revertMove(Move move) {
		return new Move(move.j, dimension - move.i - 1, move.d);
	}

	public Move move() {
		int d;
		if (player == Player.HPLAYER) {
			d = 1;
		}else {
			d = 1;
		}
		Move move = getBestMove(d);
		makeMove(move);
		previousPlayer();
		return move == null ? null : revertMove(move);
	}

	public Move getBestMove(final int depth) {
		MoveWrapper wrapper = new MoveWrapper();
		negamax(wrapper, depth, -maxEvaluateValue(), maxEvaluateValue());
		return wrapper.move;
	}

	private double negamax(final MoveWrapper wrapper, final int depth, double alpha, double beta) {
		if (depth == 0 || finished()) {
			return evaluate();
		}

		Move bestMove = null;
		Collection<Move> moves = getPossibleMoves();

		if (moves.isEmpty()) {
			nextPlayer();
			double score = -negamax(null, depth - 1, -beta, -alpha);
			previousPlayer();
			return score;
		} else {
			double score;
			for (Move move : moves) {
				makeMove(move);
				score = -negamax(null, depth - 1, -beta, -alpha);
				undoMove(move);
				if (score > alpha) {
					alpha = score;
					bestMove = move;
					if (alpha >= beta) {
						break;
					}
				}
			}

			if (wrapper != null) {
				wrapper.move = bestMove;
			}

			return alpha;
		}
	}

	private Collection<Move> getPossibleMoves() {
		Collection<Move> moves = new ArrayList<Move>();

		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				Piece piece = board[i][j];
				if (player == Player.HPLAYER && piece == Piece.HSLIDER) {
					Move moveUp = new Move(i, j, Direction.UP);
					Move moveDown = new Move(i, j, Direction.DOWN);
					Move moveRight = new Move(i, j, Direction.RIGHT);

					if (canMove(moveUp)) {
						moves.add(moveUp);
					}
					if (canMove(moveDown)) {
						moves.add(moveDown);
					}
					if (canMove(moveRight)) {
						moves.add(moveRight);
					}
				} else if (player == Player.VPLAYER && piece == Piece.VSLIDER) {
					Move moveRight = new Move(i, j, Direction.RIGHT);
					Move moveLeft = new Move(i, j, Direction.LEFT);
					Move moveUp = new Move(i, j, Direction.UP);

					if (canMove(moveRight)) {
						moves.add(moveRight);
					}
					if (canMove(moveLeft)) {
						moves.add(moveLeft);
					}
					if (canMove(moveUp)) {
						moves.add(moveUp);
					}
				}
			}
		}

		return moves;
	}

	private boolean canMove(Move move) {
		switch (move.d) {
		case UP:
			return (board[move.i][move.j] == Piece.HSLIDER && move.i > 0 && board[move.i - 1][move.j] == Piece.BLANK)
					|| (board[move.i][move.j] == Piece.VSLIDER
							&& (move.i == 0 || board[move.i - 1][move.j] == Piece.BLANK));
		case DOWN:
			return (board[move.i][move.j] == Piece.HSLIDER && move.i < dimension - 1
					&& board[move.i + 1][move.j] == Piece.BLANK);
		case RIGHT:
			return (board[move.i][move.j] == Piece.HSLIDER
					&& (move.j == dimension - 1 || board[move.i][move.j + 1] == Piece.BLANK))
					|| (board[move.i][move.j] == Piece.VSLIDER && move.j < dimension - 1
							&& board[move.i][move.j + 1] == Piece.BLANK);
		case LEFT:
			return (board[move.i][move.j] == Piece.VSLIDER && move.j > 0 && board[move.i][move.j - 1] == Piece.BLANK);
		}

		return false;
	}

	private boolean finished() {
		int hsliders = 0, vsliders = 0;

		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				Piece piece = board[i][j];
				if (piece == Piece.HSLIDER) {
					hsliders++;
				} else if (piece == Piece.VSLIDER) {
					vsliders++;
				}
			}
		}

		return hsliders * vsliders == 0 ? true : false;
	}

	private double evaluate() {
		int hsliders = 0, vsliders = 0;
		int hscore = 1, vscore = 1;

		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				Piece piece = board[i][j];
				if (piece == Piece.HSLIDER) {
					hsliders++;
					hscore += j;
				} else if (piece == Piece.VSLIDER) {
					vsliders++;
					vscore += dimension - i - 1;
				}
			}
		}

		hscore += dimension * (dimension - hsliders - 1) * 2;
		vscore += dimension * (dimension - vsliders - 1) * 2;

		return player == Player.HPLAYER ? hscore - vscore : vscore - hscore;
	}

	private double maxEvaluateValue() {
//		return dimension * (dimension - 1) + 2;
		return 10000;
	}

	private void makeMove(Move move) {
		nextPlayer();
		if (move != null) {
			int toi = move.i;
			int toj = move.j;

			switch (move.d) {
			case UP:
				toi--;
				break;
			case DOWN:
				toi++;
				break;
			case RIGHT:
				toj++;
				break;
			case LEFT:
				toj--;
				break;
			}

			if (toj == dimension) {
				board[move.i][move.j] = Piece.BLANK;
				return;
			} else if (toi == -1) {
				board[move.i][move.j] = Piece.BLANK;
				return;
			}

			board[toi][toj] = board[move.i][move.j];
			board[move.i][move.j] = Piece.BLANK;
		}
	}

	private void undoMove(Move move) {
		previousPlayer();
		if (move != null) {
			int toi = move.i;
			int toj = move.j;

			switch (move.d) {
			case UP:
				toi--;
				break;
			case DOWN:
				toi++;
				break;
			case RIGHT:
				toj++;
				break;
			case LEFT:
				toj--;
				break;
			}

			if (toj == dimension) {
				board[move.i][move.j] = Piece.HSLIDER;
				return;
			} else if (toi == -1) {
				board[move.i][move.j] = Piece.VSLIDER;
				return;
			}

			board[move.i][move.j] = board[toi][toj];
			board[toi][toj] = Piece.BLANK;
		}
	}

	private void nextPlayer() {
		player = player == Player.HPLAYER ? Player.VPLAYER : Player.HPLAYER;
	}

	private void previousPlayer() {
		nextPlayer();
	}
}