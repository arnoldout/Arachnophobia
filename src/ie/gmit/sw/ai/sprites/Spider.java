package ie.gmit.sw.ai.sprites;

import ie.gmit.sw.ai.Maze;

public abstract class Spider extends Moveable{

	public Spider(Maze model, int x, int y, boolean isAlive, char spriteChar) {
		super(model, x, y, isAlive, spriteChar);
	}


	@Override
	public abstract void run();
}
