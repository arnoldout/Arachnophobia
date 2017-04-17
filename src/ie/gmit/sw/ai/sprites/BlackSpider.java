package ie.gmit.sw.ai.sprites;

import ie.gmit.sw.ai.Maze;

public class BlackSpider extends Spider {

	public BlackSpider(Maze model, int x, int y, boolean isAlive) {
		super(model, x, y, isAlive, '\u0036');
	}

	@Override
	public void run() {
	}

}
