package ie.gmit.sw.ai.sprites;

import ie.gmit.sw.ai.Maze;

public abstract class Spider extends Moveable{

	//calculated int to the closeness degree of the spartan to the spider
	private int spartanPos;
	public Spider(Maze model, int x, int y, boolean isAlive, char spriteChar) {
		super(model, x, y, isAlive, spriteChar);
	}


	@Override
	public abstract void run();
}
