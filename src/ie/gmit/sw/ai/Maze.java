package ie.gmit.sw.ai;

import java.util.Random;

public class Maze {
	private char[][] maze;

	public Maze(int dimension) {
		maze = new char[dimension][dimension];
		init();
		buildMaze();

		int featureNumber = (int) ((dimension * dimension) * 0.01);
		addFeature('\u0031', '0', featureNumber); // 1(int=49) is a sword, 0 is
													// a hedge
		addFeature('\u0032', '0', featureNumber); // 2(int=50) is help, 0 is a
													// hedge
		addFeature('\u0033', '0', featureNumber); // 3(int=51) is a bomb, 0 is a
													// hedge
		addFeature('\u0034', '0', featureNumber); // 4(int=52) is a hydrogen
													// bomb, 0 is a hedge

		featureNumber = (int) ((dimension * dimension) * 0.01);

		makeExit();
		// addFeature('\u0036', '0', featureNumber); //6(int=54) is a Black
		// Spider, 0 is a hedge
		// addFeature('\u0037', '0', featureNumber); //7(int=55) is a Blue
		// Spider, 0 is a hedge
		// addFeature('\u0038', '0', featureNumber); //8(int=56) is a Brown
		// Spider, 0 is a hedge
		// addFeature('\u0039', '0', featureNumber); //9(int=57) is a Green
		// Spider, 0 is a hedge
		// addFeature('\u003A', '0', featureNumber); //:(int=58) is a Grey
		// Spider, 0 is a hedge
		// addFeature('\u003B', '0', featureNumber); //;(int=59) is a Orange
		// Spider, 0 is a hedge
		// addFeature('\u003C', '0', featureNumber); //<(int=60) is a Red
		// Spider, 0 is a hedge
		// addFeature('\u003D', '0', featureNumber); //=(int=61) is a Yellow
		// Spider, 0 is a hedge
	}

	private void init() {
		for (int row = 0; row < maze.length; row++) {
			for (int col = 0; col < maze[row].length; col++) {
				maze[row][col] = '0'; // Index 0 is a hedge...
			}
		}
	}

	// just to be sure, we do a line from the bottom inward of 4 cells
	private void makeExit() {
		Random rand = new Random();
		int r = rand.nextInt(maze.length-3)+2;	
		maze[maze.length-1][r] = ' ';
		maze[maze.length-2][r] = ' ';
		maze[maze.length-3][r] = ' ';
		maze[maze.length-4][r] = ' ';

		maze[maze.length-2][r-1] = ' ';
		maze[maze.length-3][r-1] = ' ';
		maze[maze.length-4][r-1] = ' ';
		maze[maze.length-2][r+1] = ' ';
		maze[maze.length-3][r+1] = ' ';
		maze[maze.length-4][r+1] = ' ';

	}

	private void addFeature(char feature, char replace, int number) {
		int counter = 0;

		while (counter < feature) {

			int row = (int) (maze.length * Math.random());
			int col = (int) (maze[0].length * Math.random());

			if (maze[row][col] == replace) {
				maze[row][col] = feature;
				counter++;
			}
		}
	}

	private void buildMaze() {
		for (int row = 1; row < maze.length - 1; row++) {
			for (int col = 1; col < maze[row].length - 1; col++) {
				int num = (int) (Math.random() * 10);
				if (num > 5 && col + 1 < maze[row].length - 2) {
					maze[row][col + 1] = '\u0020'; // \u0020 = 0x20 = 32 (base
													// 10) = SPACE
				} else {
					if (row + 1 < maze.length - 2)
						maze[row + 1][col] = '\u0020';
				}
			}
		}
	}

	public char[][] getMaze() {
		return this.maze;
	}

	public char get(int row, int col) {
		return this.maze[row][col];
	}

	public void set(int row, int col, char c) {
		this.maze[row][col] = c;
	}

	public int size() {
		return this.maze.length;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int row = 0; row < maze.length; row++) {
			for (int col = 0; col < maze[row].length; col++) {
				sb.append(maze[row][col]);
				if (col < maze[row].length - 1)
					sb.append(",");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}