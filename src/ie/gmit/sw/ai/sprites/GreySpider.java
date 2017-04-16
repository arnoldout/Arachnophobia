package ie.gmit.sw.ai.sprites;

import ie.gmit.sw.ai.Maze;

public class GreySpider extends Spider{

	public GreySpider(Maze model, int x, int y, boolean isAlive) {
		super(model, x, y, isAlive, '\u003A');
	}

	@Override
	public void run() {
		moveUp();	
	}

}
