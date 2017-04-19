package ie.gmit.sw.ai.sprites;

import ie.gmit.sw.ai.Maze;
import ie.gmit.sw.ai.traversal.Node;

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
/*
 * Tomorrow move robs stuff from spartan into here	
 * 
 * 
 */
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
	public Node getRandomCirclePoint(int radius)
	{
		boolean foundValidNode = false;
		Node n = null;
		while(!foundValidNode)
		{
			double angle = Math.random()*Math.PI*2;
			double x = Math.cos(angle)*radius;
			double y = Math.sin(angle)*radius;
			System.out.println(x+"***"+y);
			try{
				if(isValidMove((int)x, (int)y))
				{
					System.out.println((int)x+"***"+(int)y);
					n = new Node((int)x, (int)y);
					foundValidNode = true;
				}
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				//going to hit this a few times
			}
		}
		return n;
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
