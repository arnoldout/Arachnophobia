package ie.gmit.sw.ai.sprites;

import java.util.Deque;
import java.util.LinkedList;

import ie.gmit.sw.ai.GameController;
import ie.gmit.sw.ai.Maze;
import ie.gmit.sw.ai.nn.BackpropagationTrainer;
import ie.gmit.sw.ai.nn.NeuralNetwork;
import ie.gmit.sw.ai.nn.Trainator;
import ie.gmit.sw.ai.nn.Utils;
import ie.gmit.sw.ai.nn.activator.Activator.ActivationFunction;
import ie.gmit.sw.ai.traversal.BestFirstTraversator;
import ie.gmit.sw.ai.traversal.MazeNodeConverter;
import ie.gmit.sw.ai.traversal.Node;
import ie.gmit.sw.ai.traversal.Node.Direction;

public class Spartan extends Moveable {
	// move this guy out of here, maybe. it's okay to leave one in the spartan
	// maybe, theres just one of him
	private NeuralNetwork testeroo;
	// *********************************************************************
	// If reworking algorithms works, make this a traversator again
	// I was just playing with them and got tired of changing the interface
	// whenever i wanted to try another algorithm
	private BestFirstTraversator t;
	// ********************************************************************
	private Node goalNode;
	private Node lastGoal;
	private Deque<Node> path;

	// these are temporary, do the comments above the scan method later and
	// these should change
	private int swrdNearby = 0;
	private int hlpNearby = 0;
	private int bmbNearby = 0;
	private int hbmbNearby = 0;
	private int hasmap = 0;
	private double[] inputs = new double[5];
	// careful with this, if pathfinging error occur later one, remember this
	// could be an issue with goal nodes
	private Node[][] travMaze = MazeNodeConverter.makeTraversable(getModel());

	public Spartan(Maze model, int x, int y, boolean isAlive) {
		super(model, x, y, isAlive, '\u0035');

		path = new LinkedList<Node>();

		goalNode = lastGoal = null;

		// ************************************************************************
		// start off training a NN for now, then move elsewhere later
		// pass in nearby treasure
		// need to pass in enemies later
		double[][] trainingData = { { 1, 0, 0, 0, 0 }, // only a sword nearby
				{ 0, 1, 0, 0, 0 }, // only help nearby, doesnt have a map
				{ 0, 1, 0, 0, 1 }, // help nearby, already has a map
				{ 0, 0, 1, 0, 0 }, // bomb nearby
				{ 0, 0, 0, 1, 0 }, // hmb nearby
				{ 1, 1, 0, 0, 0 }, // sword and help nearby, has no map
				{ 1, 0, 1, 0, 0 }, //
				{ 0, 1, 0, 1, 0 }, // no map
				{ 0, 1, 1, 0, 0 }, // help and bomb, nearby no map
				{ 0, 1, 1, 0, 1 }, // help nearby has map
				{ 0, 1, 0, 1, 0 }, // no map
				{ 0, 0, 1, 1, 0 }, //// bomb and hbomb nearby
				{ 0, 0, 0, 1, 0 }, // hbomb nearby
				{ 1, 1, 1, 1, 0 }, // all nearby, but no map
				{ 0, 0, 0, 0, 0 }// nothing in sight, wander around like a
									// tourist
		};
		// {getsword, gethlp, getbmb, gethmb, wander}
		double[][] expected = { { 1, 0, 0, 0, 0 }, // only a sword nearby
				{ 0, 1, 0, 0, 0 }, // only help nearby, doesnt have a map
				{ 0, 0, 0, 0, 1 }, // help nearby, already has a map
				{ 0, 0, 1, 0, 0 }, // bomb nearby
				{ 0, 0, 0, 1, 0 }, // hmb nearby
				{ 0, 1, 0, 0, 0 }, // sword and help nearby, has no map
				{ 1, 0, 0, 0, 0 }, // sword and bomb nearby
				{ 0, 0, 0, 0, 1 }, // no map
				{ 0, 1, 0, 0, 0 }, // help and bomb, nearby no map
				{ 0, 0, 0, 0, 1 }, // help nearby has map
				{ 0, 1, 0, 0, 0 }, // no map
				{ 0, 0, 0, 1, 0 }, //// bomb and hbomb nearby
				{ 0, 0, 0, 1, 0 }, // hbomb nearby
				{ 0, 1, 0, 0, 0 }, // all nearby, but no map
				{ 0, 0, 0, 0, 1 } };

		testeroo = new NeuralNetwork(ActivationFunction.HyperbolicTangent, inputs.length, 6, inputs.length);
		Trainator t = new BackpropagationTrainer(testeroo);
		t.train(trainingData, expected, .01, 10000);
		// training complete
		// ***************************************************************************
	}

	@Override
	public void run() {
		double[] actions = {};
		scan();
		inputs[0] = swrdNearby > 0 ? 1 : 0;
		inputs[1] = hlpNearby > 0 ? 1 : 0;
		inputs[2] = bmbNearby > 0 ? 1 : 0;
		inputs[3] = hbmbNearby > 0 ? 1 : 0;
		inputs[4] = hasmap;
		try {
			actions = testeroo.process(inputs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int resultsIndx = Utils.getMaxIndex(actions);

		// the index determines what his goal will be, cases should be what
		// index each output node is
		// maybe move to a better code structure for this if we get around to it
		switch (resultsIndx) {
		case 0:
			System.out.println("going for the sword@" + swrdNearby);
			goalNode = travMaze[decodeYPos(swrdNearby)][decodeXPos(swrdNearby)];
			break;
		case 1:
			System.out.println("going for the help@" + hlpNearby);
			goalNode = travMaze[decodeYPos(hlpNearby)][decodeXPos(hlpNearby)];
			break;
		case 2:
			System.out.println("going for the bomb@" + bmbNearby);
			goalNode = travMaze[decodeYPos(bmbNearby)][decodeXPos(bmbNearby)];
			break;
		case 3:
			System.out.println("going for the hbomb" + hbmbNearby);
			goalNode = travMaze[decodeYPos(hbmbNearby)][decodeXPos(hbmbNearby)];
			break;
		case 4:
			goalNode = null;
			System.out.println("wandering");
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
		if (goalNode != null) {

			if (lastGoal == null || !lastGoal.equals(goalNode)) {
				t = new BestFirstTraversator(goalNode);
				goalNode.setGoalNode(true);

				System.out.println("goal nodes" + lastGoal + " " + goalNode);
				if (goalNode != null) {
					if (lastGoal != null)
						lastGoal.setGoalNode(false);
					goalNode.setGoalNode(true);
					lastGoal = goalNode;
				}
				try {
					path = new LinkedList<Node>((t.traverse(travMaze, travMaze[this.getY()][this.getX()])));
					System.out.println("have path: " + path);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!path.isEmpty()) {
					path.poll();// the last one is the one he's
								// already on
				}
				goalNode.setGoalNode(false);
			}

			if (!path.isEmpty()) {
				Node n = path.peek();

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

				}
			}
		}

		// he doesnt have a goal, which means he is just going to wander around
		// make sure to set the last goal to null just in case he reached his
		// last goal or something
		else {
			if (lastGoal != null)
				lastGoal = null;
			System.out.println("deciding where to wander");
			int pos = getWanderPos();
			if (isValidMove(decodeYPos(pos), decodeXPos(pos))) {
				doMove(decodeYPos(pos), decodeXPos(pos));
			} else
				System.out.println("Cant wander there");
		}
	}

	// for the spartan, replaces the pickup he grabbed with a wall
	// the t/f is there for safety, if he sees this method return true
	// he triggers wh/e it was supposed to do.
	private boolean doPickup(int row, int col) {
		char target = this.getMaze()[row][col];
		if (target < '5' && target > '0') {
			this.getModel().set(row, col, '0');

			// get rid of these ifs, just to test a bug fix for now
			if (row < this.getRow() && col == this.getCol()) {// loot is above
				this.travMaze[this.getY()][this.getX()].removePath(Direction.North);
				this.travMaze[row][col].removePath(Direction.South);
			} else if (row > this.getRow() && col == this.getCol())// loot is
																	// below
			{
				this.travMaze[this.getY()][this.getX()].removePath(Direction.South);
				this.travMaze[row][col].removePath(Direction.North);
			}
			if (row == this.getRow() && col < this.getCol()) {// loot is left
				this.travMaze[this.getY()][this.getX()].removePath(Direction.West);
				this.travMaze[row][col].removePath(Direction.East);
			} else if (row == this.getRow() && col > this.getCol())// loot is
																	// right
			{
				this.travMaze[this.getY()][this.getX()].removePath(Direction.East);
				this.travMaze[row][col].removePath(Direction.West);
			}
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
	//unfinished, needs to kill the threads not just remove the char from the array
	private void boom(int row, int col, int radius) {
		int[][] bounds = getBounds(row, col, radius);

		for (int i = bounds[0][0]; i < bounds[0][1]; i++) {
			for (int j = bounds[1][0]; j < bounds[1][1]; j++) {
				if (this.getMaze()[i][j] > 5)
					this.getModel().set(i, j, ' ');
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
	}

	//don't call java a piece of shit
	private void printPOS() {
		System.out.println(this.getY() + " " + this.getX());
	}

	private int getWanderPos() {
		int range = (4 - 1) + 1;
		int i = (int) (Math.random() * range) + 1;
		switch (i) {
		case 1:
			return encodePos((this.getY() - 1), this.getX());
		case 2:
			return encodePos(this.getY(), (this.getX() - 1));
		case 3:
			return encodePos((this.getY() + 1), this.getX());
		case 4:
			return encodePos(this.getY(), (this.getX() + 1));
		default:
			return encodePos(this.getY(), (this.getX() - 1));
		}

	}

	// to get x : num % 1000
	// to get y : (int)num / 1000
	// faster than dealing with collections
	private int encodePos(int y, int x) {
		return x + 1000 * y;
	}

	private int decodeXPos(int pos) {
		return pos % 1000;
	}

	private int decodeYPos(int pos) {
		return (int) pos / 1000;
	}

	// scan for features nearby
	// consider storing all the positions of POIs and rescan depending on
	// movement instead of looping every interation?
	// cant store spiders through, since they move...
	private void scan() {
		char[][] maze = this.getMaze();
		int x = this.getX();
		int y = this.getY();
		swrdNearby = hlpNearby = hbmbNearby = bmbNearby = 0;

		// start @ current pos, x-5 to x+5
		// y-5 to y+5
		// Might up this to 10 or 7 at the least.
		int startx = x - 10 < 0 ? 0 : x - 10;
		int endx = x + 10 > 99 ? 99 : x + 10;
		int starty = y - 10 < 0 ? 0 : y - 10;
		int endy = y + 10 > 99 ? 99 : y + 10;

		for (int i = starty; i < endy; i++) {
			for (int j = startx; j < endx; j++) {
				// \u0031 is a sword, 0 is a hedge
				// \u0032 is help, 0 is a hedge
				// \u0033 is a bomb
				// \u0034 is a hydrogen bomb

				if (maze[i][j] != '0') {
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
