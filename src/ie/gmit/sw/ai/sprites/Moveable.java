package ie.gmit.sw.ai.sprites;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

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
	//each character has a health bar
	private AtomicInteger health;
	//amount of damage character will do on attack
	private AtomicInteger attackLevel;
	//stops other characters attacking character if it's already in the process of dying
	private AtomicBoolean isAlive;
	public Moveable(String id,Maze model, int row, int col, boolean isAlive, char spriteChar, int attackLevel) {
		super();
		this.id = id;
		this.model = model;
		this.col = col;
		this.row = row;
		this.spriteChar = spriteChar;
		this.isAlive = new AtomicBoolean(true);
		//100 is cut off point used in fuzzy
		this.health = new AtomicInteger(100);
		this.attackLevel = new AtomicInteger(attackLevel);
		this.model.set(row, col, spriteChar);
	}
	public void takeDamage(AtomicInteger damage)
	{
		this.health.set(this.getHealth()- damage.get());
		if(this.health.get()<=0)
		{
			//stop other sprites attacking
			this.isAlive.set(false);
			//remove thread
			SpriteService.getInstance().killSprite(this.id);
			getMaze()[row][col] = ' ';
		}
	}
	
	public void healOrAttackScan()
	{
		int x = this.getCol();
		int y = this.getRow();
		
		int startx = x - 1 < 0 ? 0 : x - 1;
		int endx = x + 1 > 99 ? 99 : x + 1;
		int starty = y - 1 < 0 ? 0 : y - 1;
		int endy = y + 1 > 99 ? 99 : y + 1;

		//scan 3x3 area of nearby squares for enemies
		//if found enemy, then attack it, if it's a 
		//friendly spider (same colour (that's how its spelled rob)), then it gets a health buff
		for (int i = starty; i <= endy; i++) {
			for (int j = startx; j <= endx; j++) {
				char c = this.getMaze()[i][j];
				if(c != ' '&&c != '0'&&c != '1'&&c != '2'
						&&c != '3'&&c != '4'&&c != this.getSpriteChar())
				{
					try{						
						Moveable m = SpriteService.getInstance().findSprite(i, j, c);
						if(m.isAlive()){
							m.takeDamage(this.attackLevel);
							if(!m.isAlive())
							{
								doHeal(10);
							}
							break;
						}
					}
					catch(NullPointerException e)
					{
						//spider already ded
					}
				}
				else if(c == this.getSpriteChar())
				{
					doHeal(20);
					break;
				}
			}
		}
	}

	private void doHeal(int buff) {
		//do update lazily, should prioritize the attacks over friendly healing
		if(this.getHealth()+buff<100)
		{
			this.health.lazySet(this.health.get()+buff);
		}
	}

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
		//loops through random points on the perimeter of a circle
		//until it finds a valid point on the map
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

	//move the character to position
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
		return health.get();
	}

	public char[][] getMaze() {
		return model.getMaze();
	}

	public Maze getModel() {
		return model;
	}
}
