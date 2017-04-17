package ie.gmit.sw.ai.sprites;

import java.util.Deque;
import java.util.Queue;

import ie.gmit.sw.ai.GameController;
import ie.gmit.sw.ai.Maze;
import ie.gmit.sw.ai.nn.BackpropagationTrainer;
import ie.gmit.sw.ai.nn.NeuralNetwork;
import ie.gmit.sw.ai.nn.Trainator;
import ie.gmit.sw.ai.nn.Utils;
import ie.gmit.sw.ai.nn.activator.Activator.ActivationFunction;
import ie.gmit.sw.ai.traversal.BestFirstTraversator;
import ie.gmit.sw.ai.traversal.Node;
import ie.gmit.sw.ai.traversal.Traversator;

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
	private Deque<Node> path;

	// these are temporary, do the comments above the scan method later and
	// these should change
	private int swrdNearby = 0;
	private int hlpNearby = 0;
	private int bmbNearby = 0;
	private int hbmbNearby = 0;
	private int hasmap = 0;
	private double[] inputs = new double[5];

	public Spartan(Maze model, int x, int y, boolean isAlive) {
		super(model, x, y, isAlive, '\u0035');

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
				{ 1, 1, 1, 1, 0 }// all nearby, but no map
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
				{ 0, 1, 0, 0, 0 }// all nearby, but no map
		};

		testeroo = new NeuralNetwork(ActivationFunction.HyperbolicTangent, inputs.length, 6, inputs.length);
		Trainator t = new BackpropagationTrainer(testeroo);
		t.train(trainingData, expected, .01, 10000);

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int resultsIndx = Utils.getMaxIndex(actions);
		Node[][] travMaze = GameController.getNodes();

		switch (resultsIndx) {
		case 0:
			System.out.println("going for the sword@" + swrdNearby);
			goalNode = travMaze[(int) swrdNearby / 1000][swrdNearby % 1000];
			t = new BestFirstTraversator(goalNode);
			break;
		case 1:
			System.out.println("going for the help@" + hlpNearby);
			goalNode = travMaze[(int) hlpNearby / 1000][hlpNearby % 1000];
			t = new BestFirstTraversator(goalNode);
			break;
		case 2:
			System.out.println("going for the bomb@" + bmbNearby);
			if (bmbNearby == 0) {
				System.out.println("broke");
				break;
			}
			goalNode = travMaze[(int) bmbNearby / 1000][bmbNearby % 1000];
			t = new BestFirstTraversator(goalNode);
			break;
		case 3:
			System.out.println("going for the hbomb" + hbmbNearby);
			goalNode = travMaze[(int) hbmbNearby / 1000][hbmbNearby % 1000];
			t = new BestFirstTraversator(goalNode);
			break;
		case 4:
			goalNode = null;
			System.out.println("wandering");
			break;
		default:
			break;
		}

		// just doing this for now, basically he will home in on one goal node
		// and ignore everything until he gets there. need to use the nn and
		// account for enemies to break that behavior or rework this garbage
		// also make this a queue and rework the method that builds it in the algorithm
		// Deque is unecessary...
		if (path == null) {
			if (goalNode != null)
				goalNode.setGoalNode(true);
			path = t.traverse(travMaze, travMaze[this.getY()][this.getX()]);
			if (!path.isEmpty())
				path.removeLast();// the last one is the one he's already on
			goalNode.setGoalNode(false);
		}
		if (!path.isEmpty()) {
			Node n = path.getLast();
			if (isValidMove(n.getRow(), n.getCol())) {
				n = path.pollLast();
				doMove(n.getRow(), n.getCol());

			}
			// System.out.println("@" + this.getY() + " " + this.getX());
			// System.out.println(path);
		} else
			path = null;// so messy
	}

	private int getWanderPos() {
		// just doing random movement for now
		System.out.println("wandering");
		int range = (4 - 1) + 1;
		int i = (int) (Math.random() * range) + 1;
		switch (i) {
		case 1:
			return (this.getX() - 1) + 1000 * this.getY();
		case 2:
			return this.getX() + 1000 * (this.getY() - 1);
		case 3:
			return (this.getX() + 1) + 1000 * this.getY();
		case 4:
			return this.getX() + 1000 * (this.getY() + 1);
		default:
			return this.getX() + 1000 * (this.getY() - 1);
		}

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
		//Might up this to 10 or 7 at the least.
		int startx = x - 5 < 0 ? 0 : x - 5;
		int endx = x + 5 > 99 ? 99 : x + 5;
		int starty = y - 5 < 0 ? 0 : y - 5;
		int endy = y + 5 > 99 ? 99 : y + 5;

		for (int i = starty; i < endy; i++) {
			for (int j = startx; j < endx; j++) {
				// \u0031 is a sword, 0 is a hedge
				// \u0032 is help, 0 is a hedge
				// \u0033 is a bomb
				// \u0034 is a hydrogen bomb

				// to get x : num % 1000
				// to get y : (int)num % 1000
				// faster than dealing with collections
				switch (maze[i][j]) {
				case '\u0031':// sword
					swrdNearby = j + 1000 * i;
					break;
				case '\u0032':// sword
					hlpNearby = j + 1000 * i;
					break;
				case '\u0033':// sword
					bmbNearby = j + 1000 * i;
					break;
				case '\u0034':// sword
					hbmbNearby = j + 1000 * i;
					break;
				default:
					break;
				}
			}
		}
	}
}
