package ie.gmit.sw.ai.sprites;

import ie.gmit.sw.ai.Maze;

public class RedSpider extends Spider{

	public RedSpider(Maze model, int x, int y, boolean isAlive) {
		super(model, x, y, isAlive, '\u003C');
	}

	@Override
	public void run() {
		moveLeft();
	}

}
