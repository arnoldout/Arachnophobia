package ie.gmit.sw.ai.sprites;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import ie.gmit.sw.ai.Maze;
import ie.gmit.sw.ai.traversal.BestFirstTraversator;
import ie.gmit.sw.ai.traversal.MazeNodeConverter;
import ie.gmit.sw.ai.traversal.Node;

public abstract class Spider extends Moveable{
	private Node goalNode;
	private Node lastGoal;
	Random r = new Random();

	private BestFirstTraversator t;
	private Deque<Node> path;
	// careful with this, if pathfinging error occur later one, remember this
	// could be an issue with goal nodes
	private Node[][] travMaze = MazeNodeConverter.makeTraversable(getModel());
	public Spider(Maze model, int x, int y, boolean isAlive, char spriteChar) {
		super(model, x, y, isAlive, spriteChar);
		setHealth(100);
		goalNode = lastGoal = null;
		path = new LinkedList<Node>();

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

	public void traversePath()
	{
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
	//each spider can decide which risks they would rather take
	public abstract DistanceRisk compareRisks(DistanceRisk ...distanceRisks );
	
	@Override
	public void run(){
		DistanceRisk pickupRisk = SpiderService.getInstance().getPickupRisk(getModel(), getX(), getY(), getHealth());
		DistanceRisk spartanRisk = SpiderService.getInstance().getSpartanRisk(getX(), getY(), getHealth());
		DistanceRisk friendlyRisk = SpiderService.getInstance().getFriendlyRisk(getModel(), getX(), getY(),getSpriteChar() ,getHealth());
		DistanceRisk r = compareRisks(pickupRisk, spartanRisk, friendlyRisk);
		//if(lastGoal.getCol()!=r.getY()&&lastGoal.getRow()!=r.getX())
		//{
			travMaze = MazeNodeConverter.makeTraversable(getModel());
		//}
		this.goalNode = travMaze[r.getY()][r.getX()];
		traversePath();
	}
}