package ie.gmit.sw.ai.sprites;

import ie.gmit.sw.ai.Maze;

public abstract class Moveable implements Runnable {
	private final char blank = '\u0020';
	private Maze model;
	private char spriteChar;
	private int x;
	private int y;
	private int health;
	private boolean isAlive;
	// private neruralController
	// private fuzzyController

	public Moveable(Maze model, int x, int y, boolean isAlive, char spriteChar) {
		super();
		this.model = model;
		this.x = x;
		this.y = y;
		this.spriteChar = spriteChar;
		this.setAlive(isAlive);
		this.model.set(y, x, spriteChar);
	}

	public char getSpriteChar() {
		return spriteChar;
	}

	public char getSquare(int a, int b) {
		return model.get(a, b);
	}
	public boolean isValidMove(int row, int col){
		if (row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == ' '){
			return true;
		}else{
			return false; //Can't move
		}
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
	public int getRow(){
		return this.getY();
	}
	public int getCol(){
		return this.getX();
	}
	
	public void doMove(int row, int col){
		if (row <= model.size() - 1 && row > 0 &&
				col <= model.size() - 1 && col > 0 &&
				model.get(row, col) == ' '){
			model.set(y, x, blank);
			y=row;
			x=col;
			model.set(row, col, spriteChar);
		}
	}
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}
	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public char[][] getMaze() {
		return model.getMaze();
	}

	public Maze getModel() {
		return model;
	}

}
