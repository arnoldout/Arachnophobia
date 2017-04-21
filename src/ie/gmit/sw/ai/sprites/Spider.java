package ie.gmit.sw.ai.sprites;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

import ie.gmit.sw.ai.Maze;
import ie.gmit.sw.ai.SpriteService;
import ie.gmit.sw.ai.traversal.BestFirstCharSearch;
import ie.gmit.sw.ai.traversal.Coord;

public abstract class Spider extends Moveable {
	private Coord goalNode;
	private Coord lastGoal;

	private Chooseable decisionMaker;
	private BestFirstCharSearch t;
	private Deque<Coord> path;
	private AtomicInteger roundCounter = new AtomicInteger(0);

	public Spider(String id, Maze model, int row, int col, boolean isAlive, char spriteChar) {
		super(id, model, row, col, isAlive, spriteChar, 100);
		goalNode = lastGoal = null;
		path = new LinkedList<Coord>();
		decisionMaker = new NeuralChoiceImpl();
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
		//give the spiders 3 rounds to move out
		if(roundCounter.get() > 3)
		{
			healOrAttackScan();
		}
		roundCounter.incrementAndGet();
	}

	// each spider can decide which risks they would rather take
	public abstract DistanceRisk compareRisks(DistanceRisk... distanceRisks);

	@Override
	public void run() {
		try{
			this.goalNode = decisionMaker.getGoal(this.getHealth(), this.getModel(), this.getRow(), this.getCol(), this, this.getSpriteChar());
			traversePath();
		}
		catch(Exception e)
		{
			e.printStackTrace();
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