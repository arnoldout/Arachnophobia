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
		this.model.set(y, x, spriteChar);
	}
	/*
	 * Its late, just slapped these together, 
	 * Do some testing to check directions are correct
	 */
	public void moveRight()
	{
		doMove(x+1, y);
	}
	public void moveLeft()
	{
		doMove(x-1, y);
	}
	public void moveDown()
	{
		doMove(x, y-1);
	}
	public void moveUp()
	{
		doMove(x, y+1);
	}
	public void doMove(int row, int col){
		if (row <= model.size() - 1 && row > 0 &&
				col <= model.size() - 1 && col > 0 &&
				model.get(row, col) == ' '){
			model.set(y, x, blank);
			x=col;
			y=row;
			model.set(row, col, spriteChar);
		}
	}
	public boolean isAlive() {
		return isAlive;
	}
	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}
	public char[][] getMaze(){
		return model.getMaze();
	}
	public Maze getModel(){
		return model;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
}
