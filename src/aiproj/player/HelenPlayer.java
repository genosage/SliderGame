package aiproj.player;

import aiproj.slider.Move;
import aiproj.slider.SliderPlayer;

public class HelenPlayer implements SliderPlayer {
	
	private HelenNegamax helenNegamax;

	public void init(int dimension, String board, char player) {
		helenNegamax = new HelenNegamax(dimension, board, player);
	}

	public void update(Move move) {
		helenNegamax.update(move);
	}

	public Move move() {
		return helenNegamax.move();
	}

}
