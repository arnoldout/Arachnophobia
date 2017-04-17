package ie.gmit.sw.ai.traversal;

import ie.gmit.sw.ai.Maze;
import ie.gmit.sw.ai.traversal.Node.Direction;

public class MazeNodeConverter {

	private MazeNodeConverter() {
	}

	public static Node[][] makeTraversable(Maze model) {
		char[][] maze = model.getMaze();
		Node[][] nodes = new Node[maze.length][maze.length];
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze.length; j++) {
				nodes[i][j] = new Node(i, j);
				// go from the top left the bottom right
				// build paths if the left one is not a hedge and the top is not
				// a hedge
				if (i > 0) {
					
					if (maze[i - 1][j] != '0') {
						// 52 is the int of hydrogen bomb, so any actual
						// character is above this
						// this does however mean that pickups will be
						// considered hedges for the time being.
						// might want to add paths when the spartan picks them
						// up...
						
						nodes[i][j].addPath(Direction.West);
						nodes[i - 1][j].addPath(Direction.East);
					}
				}
				if (j > 0) {
					// build top
					if (maze[i][j - 1] != '0') {
						nodes[i][j].addPath(Direction.North);
						nodes[i][j - 1].addPath(Direction.South);
					}
				}
			}
		}

		return nodes;
	}
}
