package ie.gmit.sw.ai.sprites;

import java.util.Deque;
import java.util.LinkedList;

import ie.gmit.sw.ai.Maze;
import ie.gmit.sw.ai.traversal.BestFirstTraversator;
import ie.gmit.sw.ai.traversal.MazeNodeConverter;
import ie.gmit.sw.ai.traversal.Node;

public abstract class Spider extends Moveable{
	private Node goalNode;
	private Node lastGoal;
	int counter = 0;
	private double[] actions = new double[5];
	private Node[] actionNodes = new Node[5];
	private SpiderNNService spiderService = SpiderNNService.getInstance();
	private BestFirstTraversator t;
	private Deque<Node> path;
	// careful with this, if pathfinging error occur later one, remember this
	// could be an issue with goal nodes
	private Node[][] travMaze = MazeNodeConverter.makeTraversable(getModel());
	public Spider(String id, Maze model, int row, int col, boolean isAlive, char spriteChar) {
		super(id, model, row, col, isAlive, spriteChar, 50);
		goalNode = lastGoal = null;
		path = new LinkedList<Node>();

	}


	public void traversePath()
	{		try{

		if(counter>1)
		{
			takeDamage(100);
			return;
		}
		counter++;
		//attackScan();
		
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
					path = new LinkedList<Node>((t.traverse(travMaze, travMaze[this.getRow()][this.getCol()])));
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
			goalNode = getRandomCirclePoint(7);
			traversePath();			
		}

	}
	catch(Exception e){e.printStackTrace();}
	}
	//each spider can decide which risks they would rather take
	public abstract DistanceRisk compareRisks(DistanceRisk ...distanceRisks );
	
	@Override
	public void run(){
		
		//need refactoring out into interface or enum
		//travMaze = MazeNodeConverter.makeTraversable(getModel());
		//fuzzyGoal();
		//System.out.println(this.goalNode);
		
		travMaze = MazeNodeConverter.makeTraversable(getModel());
		neuralGoal();
		//System.out.println(this.goalNode);

		traversePath();

	}
	public void fuzzyGoal()
	{
		DistanceRisk pickupRisk = SpiderService.getInstance().getPickupRisk(getModel(), getCol(), getRow(), getHealth());
		DistanceRisk spartanRisk = SpiderService.getInstance().getSpartanRisk(getCol(), getRow(), getHealth());
		DistanceRisk friendlyRisk = SpiderService.getInstance().getFriendlyRisk(getModel(), getCol(), getRow(),getSpriteChar() ,getHealth());
		DistanceRisk r = compareRisks(pickupRisk, spartanRisk, friendlyRisk);
		this.goalNode = travMaze[r.getY()][r.getX()];
	}
	public void neuralGoal()
	{
		scan();
		double[] result = spiderService.testNN(actions);
		for (int i = 0; i < result.length; i++) {
			if(result[i] == 1)
			{
				goalNode = travMaze[actionNodes[i].getCol()][actionNodes[i].getRow()];
				break;
			}
		}
	}
	private void scan() {
		char[][] maze = this.getMaze();
		int x = this.getCol();
		int y = this.getRow();

		actions = new double[5];
		actionNodes = new Node[5];
		
		int startx = x - 10 < 0 ? 0 : x - 10;
		int endx = x + 10 > 99 ? 99 : x + 10;
		int starty = y - 10 < 0 ? 0 : y - 10;
		int endy = y + 10 > 99 ? 99 : y + 10;

		for (int i = starty; i < endy; i++) {
			for (int j = startx; j < endx; j++) {
				actions[4] = (getHealth()/2)>49 ? 1 : 0;
				if (maze[i][j] != '0') {
					switch (maze[i][j]) {
					//pickups
					case '1':
					case '2':
					case '3':
					case '4':
						actions[0] = 1;
						actionNodes[0] = new Node(i,j);
						break;
						//spartan
					case '5':
						actions[2] = 1;
						actionNodes[2] = new Node(i,j);
						break;
						//spider (friendly or enemy)
					default:
						actions[checkSpiderType(maze[i][j])] = 1;
						actionNodes[checkSpiderType(maze[i][j])] = new Node(i,j);
						break;
					}
				}
			}
		}
	}
	public int checkSpiderType(char c)
	{
		//decipher if spider character is friend or foe
		if(c == this.getSpriteChar())
		{
			return 1;
		}
		else{
			return 3;
		}
	}
	
	public Node getGoalNode() {
		return goalNode;
	}

	public void setGoalNode(Node goalNode) {
		this.goalNode = goalNode;
	}

	public Node getLastGoal() {
		return lastGoal;
	}

	public void setLastGoal(Node lastGoal) {
		this.lastGoal = lastGoal;
	}

}