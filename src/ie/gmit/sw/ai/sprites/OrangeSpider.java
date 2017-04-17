package ie.gmit.sw.ai.sprites;

import ie.gmit.sw.ai.Maze;

public class OrangeSpider extends Spider {

	public OrangeSpider(Maze model, int x, int y, boolean isAlive) {
		super(model, x, y, isAlive, '\u003B');
	}

	@Override
	public void run() {
		moveDown();
	}

}
