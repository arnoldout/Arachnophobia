package ie.gmit.sw.ai.traversal;

import java.util.Deque;

public interface Traversator {
	public Deque<Node> traverse(Node[][] maze, Node start);
}
