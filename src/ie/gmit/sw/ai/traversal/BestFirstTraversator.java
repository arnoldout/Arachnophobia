package ie.gmit.sw.ai.traversal;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class BestFirstTraversator  {
	private Node goal;

	public BestFirstTraversator(Node goal) {
		this.goal = goal;
	}

	public void setGoalNode(Node goal) {
		this.goal = goal;
	}

	public List<Node> traverse(Node[][] maze, Node node) {

		LinkedList<Node> queue = new LinkedList<Node>();
		LinkedList<Node> tampered = new LinkedList<Node>();
		queue.addFirst(node);
		List<Node> list = new LinkedList<Node>();

		try {
			while (!queue.isEmpty()) {
				node = queue.poll();
				node.setVisited(true);
				tampered.offer(node);

				if (node.isGoalNode()) {
					System.out.println("found the goal, getting path!");
					
					getPath(node, list);
					
				}

				Node[] children = node.children(maze);
				for (int i = 0; i < children.length; i++) {
					if (children[i] != null && !children[i].isVisited()) {
						children[i].setParent(node);
						tampered.offer(children[i]);
						queue.addFirst(children[i]);
					}
				}

				// Sort the whole queue. Effectively a priority queue, first in,
				// best out
				Collections.sort(queue,
						(Node current, Node next) -> current.getHeuristic(goal) - next.getHeuristic(goal));
			}
			System.out.println("returning list");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Collections.reverse(list);
		//cleanup the visited nodes for future use
		for (Node n : tampered) {
			n.setVisited(false);
			n.setParent(null);
		}
		return list;

	}

	private void getPath(Node node, List<Node> list) {
		list.add(node);
		if (node.getParent() != null)
			getPath(node.getParent(), list);
	}
}
