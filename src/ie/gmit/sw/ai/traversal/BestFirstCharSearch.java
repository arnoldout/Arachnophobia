package ie.gmit.sw.ai.traversal;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class BestFirstCharSearch {
	private char[][] maze;
	private Map<String, Coord> graph = new HashMap<String, Coord>();
	private Set<String> visited = new HashSet<String>();
	private List<Coord> path = new LinkedList<Coord>();
	private LinkedList<Coord> queue = new LinkedList<Coord>();
	private Coord start;

	public BestFirstCharSearch(char[][] maze) {
		this.maze = maze;
	}

	public List<Coord> traverse(Coord start, Coord goal) {
//		System.out.println("Starting point=" + start);
		this.start = start;
		cleanup();
		boolean done = false;
		queue.addFirst(start);
		Coord coord;
		try {
			while (!queue.isEmpty() && !done) {
				coord = queue.poll();
				
				while(!queue.isEmpty() && visited.contains(coord.toString()))
						coord = queue.poll();

//				if (visited.contains(coord.toString()))
//					System.out.println("visited");
				visited.add(coord.toString());


				if (coord.getRow() == goal.getRow() && coord.getCol() == goal.getCol()) {
//					System.out.println("found the goal, getting path!");
					done = true;
					getPath(coord);
				}

				List<Coord> paths = getChildren(coord.getRow(), coord.getCol());

				if (paths != null)
					for (Coord c : paths) {
						if (!visited.contains(c.toString())) {

							queue.addFirst(c);
							graph.put(c.toString(), coord);
						} 
					}

				Collections.sort(queue,
						(Coord current, Coord next) -> current.getHeuristic(goal) - next.getHeuristic(goal));
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Collections.reverse(path);

		return path;
	}

	private List<Coord> getChildren(int row, int col) {
		List<Coord> coords = new LinkedList<Coord>();
		if (maze[row][col] == '0')
			return coords;
		if (row < 99 && maze[row + 1][col] != '0')
			coords.add(new Coord(row + 1, col));
		if (row > 0 && maze[row - 1][col] != '0')
			coords.add(new Coord(row - 1, col));
		if (col < 99 && maze[row][col + 1] != '0')
			coords.add(new Coord(row, col + 1));
		if (col > 0 && maze[row][col - 1] != '0')
			coords.add(new Coord(row, col - 1));
		return coords;
	}

	private void cleanup() {
		visited.clear();
		graph.clear();
		queue.clear();
		path.clear();
	}

	private void getPath(Coord coord) {
		if (coord.getRow() == start.getRow() && coord.getCol() == start.getCol())
			return;
		path.add(coord);
		if (graph.containsKey(coord.toString())) {
			getPath(graph.get(coord.toString()));
		}

	}

}