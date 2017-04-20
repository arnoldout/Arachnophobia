package ie.gmit.sw.ai.sprites;

import java.util.concurrent.atomic.AtomicBoolean;

import ie.gmit.sw.ai.Maze;
import ie.gmit.sw.ai.SpriteService;
import ie.gmit.sw.ai.traversal.Coord;

public abstract class Moveable implements Runnable{
	private String id;
	private final char blank = '\u0020';
	private Maze model;
	
	private char spriteChar;
	private int col;
	private int row;
	private int health;
	private int attackLevel;
	private AtomicBoolean isAlive;

	public Moveable(String id,Maze model, int row, int col, boolean isAlive, char spriteChar, int attackLevel) {
		super();
		this.id = id;
		this.model = model;
		this.col = col;
		this.row = row;
		this.spriteChar = spriteChar;
		this.isAlive = new AtomicBoolean(true);
		this.health = 100;
		this.attackLevel = attackLevel;
		this.model.set(row, col, spriteChar);
	}

	public void takeDamage(int damage)
	{
		this.health = this.health - damage;
		if(health<0)
		{
			//stop other sprites attacking
			setAlive(false);
			//remove thread
			SpriteService.getInstance().killSprite(this.id);
			getMaze()[row][col] = ' ';
		}
	}
	
	public void attackScan()
	{
		int x = this.getCol();
		int y = this.getRow();
		
		int startx = x - 1 < 0 ? 0 : x - 1;
		int endx = x + 1 > 99 ? 99 : x + 1;
		int starty = y - 1 < 0 ? 0 : y - 1;
		int endy = y + 1 > 99 ? 99 : y + 1;

		for (int i = starty; i <= endy; i++) {
			for (int j = startx; j <= endx; j++) {
				if(this.getMaze()[i][j] != ' '||this.getMaze()[i][j] != '0'||this.getMaze()[i][j] != '1'||this.getMaze()[i][j] != '2'||this.getMaze()[i][j] != '3'||this.getMaze()[i][j] != '4'||this.getMaze()[i][j] != this.getSpriteChar())
				{
					try{
						
//						Moveable m = SpriteService.getInstance().findSprite(i, j);
//						if(m.isAlive())
//							m.takeDamage(this.attackLevel);
						SpriteService.getInstance().getSprite(0).takeDamage(this.attackLevel);
						break;
					}
					catch(NullPointerException e)
					{
						//sprite not found
						e.printStackTrace();
					}
				}
			}
		}
	}
	public int getAttackLevel() {
		return attackLevel;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public void setAttackLevel(int attackLevel) {
		this.attackLevel = attackLevel;
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
	public Coord getRandomCirclePoint(int radius)
	{
		boolean foundValidNode = false;
		Coord n = null;
		while(!foundValidNode)
		{
			double angle = Math.random()*Math.PI*2;
			double x = Math.cos(angle)*radius;
			double y = Math.sin(angle)*radius;
//			System.out.println(x+"***"+y);
			try{
				if(isValidMove((int)x, (int)y))
				{
//					System.out.println((int)x+"***"+(int)y);
					n = new Coord((int)x, (int)y);
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

	
	public void doMove(int row, int col){
		if (row <= model.size() - 1 && row > 0 &&
				col <= model.size() - 1 && col > 0 &&
				model.get(row, col) == ' '){
			model.set(this.row, this.col, blank);
			this.row=row;
			this.col=col;
			model.set(row, col, spriteChar);
		}
	}
	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public void setY(int y) {
		this.row = y;
	}

	public boolean isAlive() {
		return isAlive.get();
	}

	public void setAlive(boolean isAlive) {
		this.isAlive.set(isAlive);
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

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
