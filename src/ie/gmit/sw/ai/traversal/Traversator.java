package ie.gmit.sw.ai.traversal;

import java.util.List;

public interface Traversator {
	public List<Node> traverse(Node[][] maze, Node start);
}
