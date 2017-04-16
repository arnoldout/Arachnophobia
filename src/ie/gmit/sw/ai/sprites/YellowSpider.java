package ie.gmit.sw.ai.sprites;

import ie.gmit.sw.ai.Maze;

public class YellowSpider extends Spider{

	public YellowSpider(Maze model, int x, int y, boolean isAlive) {
		super(model, x, y, isAlive, '\u003D');
	}

	public void run() {
		moveRight();
	}

}
