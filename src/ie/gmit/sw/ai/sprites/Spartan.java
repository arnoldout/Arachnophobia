package ie.gmit.sw.ai.sprites;

import java.awt.event.KeyEvent;

import ie.gmit.sw.ai.Maze;
import ie.gmit.sw.ai.nn.BackpropagationTrainer;
import ie.gmit.sw.ai.nn.NeuralNetwork;
import ie.gmit.sw.ai.nn.Trainator;
import ie.gmit.sw.ai.nn.Utils;
import ie.gmit.sw.ai.nn.activator.Activator.ActivationFunction;
import ie.gmit.sw.ai.nn.activator.ActivatorFactory;

public class Spartan extends Moveable {
	private NeuralNetwork testeroo;

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
		int asdf = Utils.getMaxIndex(actions);
		switch (asdf) {
		case 0:
			System.out.println("going for the sword");
			break;
		case 1:
			System.out.println("going for the help");
			break;
		case 2:
			System.out.println("going for the bomb");
			break;
		case 3:
			System.out.println("going for the hbomb");
			break;
		case 4:
			System.out.println("wandering");
			break;
		default:
			break;
		}

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
					swrdNearby = x + 1000 * y;
					break;
				case '\u0032':// sword
					hlpNearby = x + 1000 * y;
					break;
				case '\u0033':// sword
					bmbNearby = x + 1000 * y;
					break;
				case '\u0034':// sword
					hbmbNearby = x + 1000 * y;
					break;
				default:
					break;
				}
			}
		}
	}

	private void isValidMove(int y, int x) {
		Maze model = this.getModel();
		if (y <= model.size() - 1 && x <= model.size() - 1 && model.get(y, x) == ' ') {
			this.doMove(y, x);
		}
	}
}
