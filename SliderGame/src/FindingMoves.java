// Author:
// Tian Zhang(tianz2)
// Hailun Tian(hailunt)

// Use the Scanner to scan data from input stream
import java.util.Scanner;

/**
 * This class can count and show the number of legal moves available to each
 * player in a given board configuration for the Slider Game. The class will
 * take inputs from System.in, calculate the legal moves for Horizontal and
 * vertical players(represented by H and V), and print the corresponding legal
 * moves in two lines.
 */
public class FindingMoves {

	// the dimension of the board
	public int dimension;

	// the board contains all the content
	public String[] board;

	/**
	 * The construct method of the class initialise the dimension and board.
	 */
	public FindingMoves(int dimension, String[] board) {
		this.dimension = dimension;
		this.board = board;
	}

	/**
	 * Print the legal moves for the two players.
	 */
	public void showLegalMoves() {
		System.out.println(allLegalMoves("H"));
		System.out.println(allLegalMoves("V"));
	}

	/**
	 * For a specific player, calculate the player's legal moves.
	 * 
	 * @param str
	 *            can be H or V, represents a specific player.
	 * 
	 * @return all the legal moves or that player.
	 */
	public int allLegalMoves(String str) {
		int moves = 0;

		for (int i = 0; i < board.length; i++) {
			moves += board[i].equals(str) ? legalMoves(i) : 0;
		}

		return moves;
	}

	/**
	 * For a single piece on the board, calculate its legal moves.
	 * 
	 * @param index
	 *            represents a single Horizontal or vertical piece.
	 * 
	 * @return the legal moves of that piece.
	 */
	public int legalMoves(int index) {
		int moves = 0;

		// check the left position
		if (index % dimension != 0 && isLegal(index, index - 1)) {
			moves++;
		}

		// check the right position
		if ((index + 1) % dimension != 0  && isLegal(index, index + 1)) {
			moves++;
		}

		// check the up position
		if (isLegal(index, index - dimension)) {
			moves++;
		}

		// check the down position
		if (isLegal(index, index + dimension)) {
			moves++;
		}

		return moves;
	}

	/**
	 * Given a before and after position, check whether this move is legal.
	 * 
	 * @param before
	 *            the previous position of a specific piece.
	 * @param after
	 *            the after position of a specific piece.
	 * 
	 * @return whether this move is legal.
	 */
	public boolean isLegal(int before, int after) {

		// if after position is out of bound, it is illegal
		if (isOutOfBound(after))
			return false;

		// for horizontal player, the piece can only move to an up, down and
		// right position, for vertical player, the piece can only move to an
		// up, left and right position
		if (board[before].equals("H") && board[after].equals("+")
			&& (after != before - 1)) {
			return true;
		} else if (board[before].equals("V") && board[after].equals("+")
			&& (after != before + dimension)) {
			return true;
		}

		return false;
	}

	/**
	 * Check whether an index is out of bound.
	 * 
	 * @param index
	 *            The index to be checked.
	 * 
	 * @return whether it is out of bound.
	 */
	public boolean isOutOfBound(int index) {
		if (index >= 0 && index < board.length)
			return false;
		return true;
	}

	/**
	 * The main entry method.
	 */
	public static void main(String[] args) {

		// read data from System input stream
		Scanner scan = new Scanner(System.in);

		// the dimension of the board
		int dimension = scan.nextInt();

		// the board contains all the content
		String[] board = new String[dimension * dimension];

		// initialise the board
		for (int i = 0; i < board.length; i++) {
			board[i] = scan.next();
		}

		// calculate and show the legal moves
		FindingMoves findingMoves = new FindingMoves(dimension, board);
		findingMoves.showLegalMoves();
	}
}