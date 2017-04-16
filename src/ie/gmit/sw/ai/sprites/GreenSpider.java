package ie.gmit.sw.ai.sprites;

import ie.gmit.sw.ai.Maze;

public class GreenSpider extends Spider{

	public GreenSpider(Maze model, int x, int y, boolean isAlive) {
		super(model, x, y, isAlive, '\u0039');
		
	}

	@Override
	public void run() {
		moveUp();
	}

}
