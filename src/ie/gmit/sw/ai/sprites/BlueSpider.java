package ie.gmit.sw.ai.sprites;

import ie.gmit.sw.ai.Maze;

public class BlueSpider extends Spider{

	public BlueSpider(Maze model, int x, int y, boolean isAlive) {
		super(model, x, y, isAlive, '\u0037');
	}

	@Override
	public void run() {
		moveUp();
		
	}
	
}
