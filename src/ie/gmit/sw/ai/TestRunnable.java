package ie.gmit.sw.ai;

public class TestRunnable implements Runnable {

	private final char blank = '\u0020';
	private Maze model;
	private char spriteChar;
	private int x;
	private int y;
	private boolean alive;
	
	public TestRunnable(Maze model, char spriteChar, int x, int y) {
		this.model = model;
		this.spriteChar = spriteChar;
		this.x = x;
		this.y = y;
		alive=true;
	}

	@Override
	public void run() {

		// just doing random movement for now
		int range = (4 - 1) + 1;
		int i =(int) (Math.random() * range) + 1;
		switch (i) {
		case 1:
			doMove(x-1, y);
			break;
		case 2:
			doMove(x, y-1);
			break;
		case 3:
			doMove(x+1, y);
			break;
		case 4:
			doMove(x, y+1);
			break;
		default:
			System.out.println("You screwed up the random number, idiot");
			break;
		}
		
	}
	
	//try to do a move, if not, w/e.
	//this is just temporary anyways...
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
}
