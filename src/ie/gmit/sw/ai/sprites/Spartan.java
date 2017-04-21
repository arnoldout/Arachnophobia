package ie.gmit.sw.ai.sprites;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import ie.gmit.sw.ai.Maze;
import ie.gmit.sw.ai.NeuralNetworkService;
import ie.gmit.sw.ai.SpriteService;
import ie.gmit.sw.ai.nn.NeuralNetwork;
import ie.gmit.sw.ai.nn.Utils;
import ie.gmit.sw.ai.traversal.BestFirstCharSearch;
import ie.gmit.sw.ai.traversal.Coord;

public class Spartan extends Moveable {
	// move this guy out of here, maybe. it's okay to leave one in the spartan
	// maybe, theres just one of him
	private NeuralNetwork nn;
	// *********************************************************************
	// If reworking algorithms works, make this a traversator again
	// I was just playing with them and got tired of changing the interface
	// whenever i wanted to try another algorithm
	private BestFirstCharSearch t;
	// ********************************************************************
	private int goalNode;
	private int lastGoal;
	private Deque<Coord> path;

	// these are temporary, do the comments above the scan method later and
	// these should change
	private int swrdNearby = 0;
	private int hlpNearby = 0;
	private int bmbNearby = 0;
	private int hbmbNearby = 0;
	private int hasmap = 0;
	private double[] inputs = new double[5];
	private List<Coord> exits = new ArrayList<Coord>();
	// careful with this, if pathfinging error occur later one, remember this
	// could be an issue with goal nodes

	public Spartan(String id, Maze model, int col, int row, boolean isAlive) {
		super(id, model, col, row, isAlive, '\u0035', 250);
		nn = NeuralNetworkService.getInstance().getSpartanNeuralNetwork();
		goalNode = lastGoal = 0;
		t = new BestFirstCharSearch(this.getMaze());
		Coord start = new Coord(row, col);
		// find exits for later
		for (Coord c : getExits()) {
			// System.out.println(c + "is an exit");
			if (!t.traverse(start, c).isEmpty())
				exits.add(c);
		}
	}

	@Override
	public void run() {
		double[] actions = {};
		scan();
		inputs[0] = swrdNearby > 0 ? 1 : 0;
		inputs[1] = hlpNearby > 0 ? 1 : 0;
		inputs[2] = bmbNearby > 0 ? 1 : 0;
		inputs[3] = hbmbNearby > 0 ? 1 : 0;
		inputs[4] = hasmap == 4 ? 1 : 0;
		try {
			actions = nn.process(inputs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int resultsIndx = Utils.getMaxIndex(actions);

		// the index determines what his goal will be, cases should be what
		// index each output node is
		// maybe move to a better code structure for this if we get around to it
		switch (resultsIndx) {
		case 0:
//			if (goalNode != swrdNearby)
//				System.out.println("going for the sword@" + swrdNearby);
			goalNode = swrdNearby;
			break;
		case 1:
//			if (goalNode != hlpNearby)
//				System.out.println("going for the help@" + hlpNearby);
			goalNode = hlpNearby;
			break;
		case 2:
//			if (goalNode != bmbNearby)
//				System.out.println("going for the bomb@" + bmbNearby);
			goalNode = bmbNearby;
			break;
		case 3:
//			if (goalNode != hbmbNearby)
//				System.out.println("going for the hbomb" + hbmbNearby);
			goalNode = hbmbNearby;
			break;
		case 4:
			goalNode = 0;
//			System.out.println("wandering");
			break;
		case 5:
			// this is kind of dumb, but whatever
//			System.out.println("running to exit");
			goalNode = encodePos(exits.get(0).getRow(), exits.get(0).getCol());
			break;
		default:
			break;
		}

		// If he has a goal, and if it is different from the last,
		// he needs to find a path.
		// first set the new goal node as the goal and set the last goal to
		// false, then save the new goal as the last goal for the next decision
		// process
		// If he hasnt changed his mind on where to go, just continue on the
		// path he already set out
		if (goalNode != 0) {

			if (lastGoal != goalNode) {

				// System.out.println("goal nodes" + lastGoal + " " + goalNode);
				if (goalNode != 0) {
					lastGoal = goalNode;
				}

				Coord start = new Coord(this.getRow(), this.getCol());
				Coord end = new Coord(decodeYPos(goalNode), decodeXPos(goalNode));
				path = new LinkedList<Coord>((t.traverse(start, end)));

				// System.out.println("have path: " + path);

			}

			if (!path.isEmpty()) {

				Coord n = path.peek();
				// System.out.println(n);
				if (isValidMove(n.getRow(), n.getCol())) {
					n = path.poll();
					doMove(n.getRow(), n.getCol());

				} else {
					if (this.getMaze()[n.getRow()][n.getCol()] != '0')
						try {
							doPickup(n.getRow(), n.getCol());

						} catch (Exception e) {
							e.printStackTrace();
						}

					else {
						Coord start = new Coord(this.getRow(), this.getCol());
						Coord end = new Coord(decodeYPos(goalNode), decodeXPos(goalNode));
						// System.out.println("tried going to " +n.getRow() +
						// n.getCol());
						path = new LinkedList<Coord>((t.traverse(start, end)));
					}

				}
			}
		}

		// he doesnt have a goal, which means he is just going to wander around
		// make sure to set the last goal to null just in case he reached his
		// last goal or something
		else {

			if (lastGoal != 0)
				lastGoal = 0;
			System.out.println("deciding where to wander");
			Coord pos = getRandomCirclePoint(15);
			Coord start = new Coord(this.getRow(), this.getCol());

			goalNode = encodeFromCoord(pos);
			path = new LinkedList<Coord>((t.traverse(start, pos)));
			System.out.println("Wandering to " + pos);
		}
	}

	// for the spartan, replaces the pickup he grabbed with a wall
	// the t/f is there for safety, if he sees this method return true
	// he triggers wh/e it was supposed to do.
	private boolean doPickup(int row, int col) {
		char target = this.getMaze()[row][col];
		if (target < '5' && target > '0') {
			char temp = this.getMaze()[row][col];

			// 1(int=49) is a sword, 0 is a hedge
			// 2(int=50) is help, 0 is a hedge
			// 3(int=51) is a bomb, 0 is a hedge
			// 4(int=52) is a hydrogen bomb, 0 is a hedge
			switch (temp) {
			case '1':
				getSword();
				break;
			case '2':
				getHelp();
				break;
			case '3':
				smallBoom(row, col);
				break;
			case '4':
				bigBoom(row, col);
				break;
			default:
				break;
			}
			this.getModel().set(row, col, '0');
			return true;
		}
		return false;
	}

	private int[][] getBounds(int row, int col, int radius) {
		int max = this.getMaze().length;
		row = row - radius < 0 ? 0 : row - radius;
		int rowMax = row + radius > max ? max : row + radius;
		col = col - radius < 0 ? 0 : col - radius;
		int colMax = col + radius > max ? max : row + radius;
		int[][] arr = { { row, rowMax }, { col, colMax } };
		return arr;
	}

	// small bomb, kill spiders in a radius
	// unfinished, needs to kill the threads not just remove the char from the
	// array
	private void boom(int row, int col, int radius) {
		int[][] bounds = getBounds(row, col, radius);
		SpriteService s = SpriteService.getInstance();
		for (int i = bounds[0][0]; i < bounds[0][1]; i++) {
			for (int j = bounds[1][0]; j < bounds[1][1]; j++) {
				if (this.getMaze()[i][j] > '5'){
				System.out.println("killing a "+this.getMaze()[i][j]);
					s.killSprite(s.findSprite(j, j, this.getMaze()[i][j]).getId());
				}
			}
		}
	}

	// big bomb, kill spiders in a big radius
	private void smallBoom(int row, int col) {
		boom(row, col, 2);
	}

	// big bomb, kill spiders in a big radius
	private void bigBoom(int row, int col) {
		boom(row, col, 4);
	}

	// get a sword, for now it just buffs health.
	private void getSword() {
		
	}

	// get help, provides a map to the exit
	private void getHelp() {
		hasmap++;
	}

	private void printPOS() {
		System.out.println(this.getRow() + " " + this.getCol());
	}


	// to get x : num % 1000
	// to get y : (int)num / 1000
	// faster than dealing with collections
	private int encodePos(int row, int col) {
		return col + 1000 * row;
	}

	private int encodeFromCoord(Coord c) {
		return encodePos(c.getRow(), c.getCol());
	}

	private int decodeXPos(int pos) {
		return pos % 1000;
	}

	private int decodeYPos(int pos) {
		return (int) pos / 1000;
	}

	private List<Coord> getExits() {
		List<Coord> exits = new ArrayList<Coord>();
		char[][] maze = this.getMaze();
		char c = this.getSpriteChar();

		for (int r = 0; r < maze[0].length; r++) {
			if (maze[0][r] == ' ') {
//				maze[0][r] = '\u0038';
				exits.add(new Coord(0, r));
			}
			if (maze[maze.length - 1][r] == ' ') {
//				maze[maze.length - 1][r] = '\u0038';
//				System.out.println(maze[maze.length - 1][r]);
				exits.add(new Coord(maze.length - 1, r));
			}
			if (maze[r][0] == ' ') {
//				maze[r][0] = '\u0038';
				System.out.println(maze[r][0]);
				exits.add(new Coord(r, 0));
			}
			if (maze[r][maze.length - 1] == ' ') {
//				maze[0][maze.length-1] = '\u0038';
//				System.out.println(maze[0][maze.length-1]);
				exits.add(new Coord(r, maze.length - 1));
			}
		}
		System.out.println(exits.size());
		return exits;
	}

	// scan for features nearby
	// consider storing all the positions of POIs and rescan depending on
	// movement instead of looping every interation?
	// cant store spiders through, since they move...
	private void scan() {
		char[][] maze = this.getMaze();
		int col = this.getCol();
		int row = this.getRow();
		swrdNearby = hlpNearby = hbmbNearby = bmbNearby = 0;
		this.healOrAttackScan();
		// start @ current pos, x-5 to x+5
		// y-5 to y+5
		// Might up this to 10 or 7 at the least.
		int startx = col - 10 < 0 ? 0 : col - 10;
		int endx = col + 10 > 99 ? 99 : col + 10;
		int starty = row - 10 < 0 ? 0 : row - 10;
		int endy = row + 10 > 99 ? 99 : row + 10;

		for (int i = starty; i < endy; i++) {
			for (int j = startx; j < endx; j++) {
				// \u0031 is a sword, 0 is a hedge
				// \u0032 is help, 0 is a hedge
				// \u0033 is a bomb
				// \u0034 is a hydrogen bomb

				if (maze[i][j] != '0') {
					// there is a free space on the edge of the board
					// time to run for the exit, just cheat and say he has a map
					// because he DOES know where the exit is.
					if (i == 99 || i == 0 || j == 99 || j == 0)
						hasmap = 4;

					int pos = encodePos(i, j);
					switch (maze[i][j]) {
					case '\u0031':// sword
						swrdNearby = pos;
						break;
					case '\u0032':// hlp
						hlpNearby = pos;
						break;
					case '\u0033':// bmb
						bmbNearby = pos;
						break;
					case '\u0034':// hbmb
						hbmbNearby = pos;
						break;
					default:
						break;
					}
				}
			}
		}

	}

}
