package ie.gmit.sw.ai.sprites;

import java.util.Deque;
import java.util.LinkedList;

import ie.gmit.sw.ai.Maze;
import ie.gmit.sw.ai.nn.Utils;
import ie.gmit.sw.ai.traversal.BestFirstCharSearch;
import ie.gmit.sw.ai.traversal.Coord;
import ie.gmit.sw.ai.traversal.Node;

public abstract class Spider extends Moveable {
	private Coord goalNode;
	private Coord lastGoal;

	private double[] actions = new double[5];
	private Coord[] actionNodes = new Coord[5];
	private SpiderNNService spiderService = SpiderNNService.getInstance();
	private BestFirstCharSearch t;
	private Deque<Coord> path;

	public Spider(String id, Maze model, int row, int col, boolean isAlive, char spriteChar) {
		super(id, model, row, col, isAlive, spriteChar, 50);
		goalNode = lastGoal = null;
		path = new LinkedList<Coord>();

	}
	public void traversePath()
	{
		attackScan();
		// If he has a goal, and if it is different from the last,
		// he needs to find a path.
		// first set the new goal node as the goal and set the last goal to
		// false, then save the new goal as the last goal for the next decision
		// process
		// If he hasnt changed his mind on where to go, just continue on the
		// path he already set out
		if (goalNode != null) {

			if (lastGoal == null || !lastGoal.equals(goalNode)) {
				t = new BestFirstCharSearch(this.getMaze());
				if (goalNode != null) {
					lastGoal = goalNode;
				}
				try {
					Coord start = new Coord(this.getRow(), this.getCol());
					path = new LinkedList<Coord>((t.traverse(start, goalNode)));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (!path.isEmpty()) {

				Coord n = path.peek();
//				System.out.println(n);
				if (isValidMove(n.getRow(), n.getCol())) {
					n = path.poll();
					doMove(n.getRow(), n.getCol());

				} else {
					Coord start = new Coord(this.getRow(), this.getCol());
					path = new LinkedList<Coord>((t.traverse(start, goalNode)));

				}
			}


			// he doesnt have a goal, which means he is just going to wander
			// around
			// make sure to set the last goal to null just in case he reached
			// his
			// last goal or something
			else {
				if (lastGoal != null)
					lastGoal = null;
//				System.out.println("deciding where to wander");
				goalNode = getRandomCirclePoint(7);
				traversePath();
			}
		}
	}

	// each spider can decide which risks they would rather take
	public abstract DistanceRisk compareRisks(DistanceRisk... distanceRisks);

	@Override
	public void run() {

		// need refactoring out into interface or enum
		 //fuzzyGoal();
		// System.out.println(this.goalNode);

		neuralGoal();
		// System.out.println(this.goalNode);

		traversePath();

	}

	public void fuzzyGoal() {
		DistanceRisk pickupRisk = SpiderService.getInstance().getPickupRisk(getModel(), getCol(), getRow(),
				getHealth());
		DistanceRisk spartanRisk = SpiderService.getInstance().getSpartanRisk(getCol(), getRow(), getHealth());
		DistanceRisk friendlyRisk = SpiderService.getInstance().getFriendlyRisk(getModel(), getCol(), getRow(),
				getSpriteChar(), getHealth());
		DistanceRisk r = compareRisks(pickupRisk, spartanRisk, friendlyRisk);
		this.goalNode = new Coord(r.getY(), r.getX());
	}

	public void neuralGoal() {
		scan();
		if(spiderService!=null)
		{
			double[] result = spiderService.process(actions);
			int resultsIndx = Utils.getMaxIndex(result);
			try{
				goalNode = new Coord(actionNodes[resultsIndx].getRow(), actionNodes[resultsIndx].getCol());
			}
			catch(Exception e)
			{
				//always fallback on wander
				goalNode = new Coord(actionNodes[4].getRow(), actionNodes[4].getCol());
			}
		}
		else{
			goalNode = new Coord(actionNodes[4].getRow(), actionNodes[4].getCol());
		}
	}

	private void scan() {
		char[][] maze = this.getMaze();
		int x = this.getCol();
		int y = this.getRow();

		actions = new double[5];
		actionNodes = new Coord[5];

		int startx = x - 10 < 0 ? 0 : x - 10;
		int endx = x + 10 > 99 ? 99 : x + 10;
		int starty = y - 10 < 0 ? 0 : y - 10;
		int endy = y + 10 > 99 ? 99 : y + 10;

		for (int i = starty; i < endy; i++) {
			for (int j = startx; j < endx; j++) {
				actions[4] = (getHealth()/2)>49 ? 1 : 0;
				actionNodes[4] = getRandomCirclePoint(10);
				if (maze[i][j] != '0') {
					switch (maze[i][j]) {
					// pickups
					case '1':
					case '2':
					case '3':
					case '4':
						actions[0] = 1;
						actionNodes[0] = new Coord(i, j);
						break;
					// spartan
					case '5':
						actions[2] = 1;
						actionNodes[2] = new Coord(i, j);
						break;
					// spider (friendly or enemy)
					default:
						actions[checkSpiderType(maze[i][j])] = 1;
						actionNodes[checkSpiderType(maze[i][j])] = new Coord(i, j);
						break;
					}
				}
			}
		}
	}

	public int checkSpiderType(char c) {
		// decipher if spider character is friend or foe
		if (c == this.getSpriteChar()) {
			return 1;
		} else {
			return 3;
		}
	}

	public Coord getGoalNode() {
		return goalNode;
	}

	public void setGoalNode(Coord goalNode) {
		this.goalNode = goalNode;
	}

	public Coord getLastGoal() {
		return lastGoal;
	}

	public void setLastGoal(Coord lastGoal) {
		this.lastGoal = lastGoal;
	}

}