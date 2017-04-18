package ie.gmit.sw.ai.sprites;

import ie.gmit.sw.ai.Maze;
import ie.gmit.sw.ai.traversal.Node;

public abstract class Spider extends Moveable{
	private Node goalNode;
	private Node lastGoal;
	//eventually move to classes
	public Spider(Maze model, int x, int y, boolean isAlive, char spriteChar) {
		super(model, x, y, isAlive, spriteChar);
		setHealth(100);
		goalNode = lastGoal = null;
	}
	@Override
	public abstract void run();
}
