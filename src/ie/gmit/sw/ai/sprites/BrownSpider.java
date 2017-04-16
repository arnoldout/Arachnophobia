package ie.gmit.sw.ai.sprites;

import ie.gmit.sw.ai.Maze;

public class BrownSpider extends Spider{

	public BrownSpider(Maze model, int x, int y, boolean isAlive) {
		super(model, x, y, isAlive, '\u0038');
		
	}

	@Override
	public void run() {
		moveLeft();
		
	}

}
