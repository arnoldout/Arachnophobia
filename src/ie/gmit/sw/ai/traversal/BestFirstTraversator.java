package ie.gmit.sw.ai.traversal;

import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public class BestFirstTraversator  {
	private Node goal;

	public BestFirstTraversator(Node goal) {
		this.goal = goal;
	}

	public void setGoalNode(Node goal) {
		this.goal = goal;
	}

	public Deque<Node> traverse(Node[][] maze, Node node) {

		LinkedList<Node> queue = new LinkedList<Node>();
		queue.addFirst(node);
		Deque<Node> list = new LinkedList<Node>();

		try {
			while (!queue.isEmpty()) {
				node = queue.poll();
				node.setVisited(true);
				System.out.println(node);

				if (node.isGoalNode()) {
					System.out.println("getting path!");
					getPath(node, list);
				}

				Node[] children = node.children(maze);
				for (int i = 0; i < children.length; i++) {
					if (children[i] != null && !children[i].isVisited()) {
						children[i].setParent(node);
						queue.addFirst(children[i]);
					}
				}

				// Sort the whole queue. Effectively a priority queue, first in,
				// best out
				System.out.println("sorting");
				Collections.sort(queue,
						(Node current, Node next) -> current.getHeuristic(goal) - next.getHeuristic(goal));
			}
			System.out.println("returning list");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	private void getPath(Node node, Queue<Node> list) {
		list.add(node);
		if (node.getParent() != null)
			getPath(node.getParent(), list);
	}
}
