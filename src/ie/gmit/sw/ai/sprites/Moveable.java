package ie.gmit.sw.ai.sprites;

import ie.gmit.sw.ai.Maze;

public abstract class Moveable implements Runnable{
	private final char blank = '\u0020';
	private Maze model;
	private char spriteChar;
	private int x;
	private int y;
	private int health;
	private boolean isAlive;
	//private neruralController
	//private fuzzyController
	
	public Moveable(Maze model, int x, int y, boolean isAlive, char spriteChar) {
		super();
		this.model = model;
		this.x = x;
		this.y = y;
		this.spriteChar = spriteChar;
		this.setAlive(isAlive);
	}
	
	public void moveDown()
	{
		doMove(x+1, y);
	}
	public void moveUp()
	{
		doMove(x-1, y);
	}
	public void moveLeft()
	{
		doMove(x, y-1);
	}
	public void moveRight()
	{
		doMove(x, y+1);
	}
	private void doMove(int row, int col){
		if (row <= model.size() - 1 && row > 0 &&
				col <= model.size() - 1 && col > 0 &&
				model.get(row, col) == ' '){
			model.set(x, y, blank);
			x=row;
			y=col;
			model.set(row, col, spriteChar);
		}
	}
	public boolean isAlive() {
		return isAlive;
	}
	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}
}
