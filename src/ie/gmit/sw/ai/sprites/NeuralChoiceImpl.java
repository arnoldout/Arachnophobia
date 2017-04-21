package ie.gmit.sw.ai.sprites;

import ie.gmit.sw.ai.Maze;
import ie.gmit.sw.ai.nn.Utils;
import ie.gmit.sw.ai.traversal.Coord;

public class NeuralChoiceImpl implements Chooseable{
	private SpiderNNService bernard = SpiderNNService.getInstance();
	private double[] actions = new double[5];
	private Coord[] actionNodes = new Coord[5];
	
	public Coord getGoal(int health, Maze model, int row, int col, Spider s, char spriteChar) {	
		Coord goalNode;
		scan(s, model ,col, row, health, spriteChar);
		
		if(bernard!=null)
		{
			double[] result = bernard.process(actions);
			int resultsIndx = Utils.getMaxIndex(result);
			try{
				goalNode = new Coord(actionNodes[resultsIndx].getRow(), actionNodes[resultsIndx].getCol());
			}
			catch(Exception e)
			{
				//always fallback on wander
				goalNode = new Coord(actionNodes[4].getRow(), actionNodes[4].getCol());
			}
		}
		else{
			goalNode = new Coord(actionNodes[4].getRow(), actionNodes[4].getCol());
		}
		return goalNode;
	}
	public void scan(Spider s, Maze m, int col, int row, int health, char spriteChar) {
		char[][] maze = m.getMaze();
		int x = col;
		int y = row;

		actions = new double[5];
		actionNodes = new Coord[5];

		int startx = x - 10 < 0 ? 0 : x - 10;
		int endx = x + 10 > 99 ? 99 : x + 10;
		int starty = y - 10 < 0 ? 0 : y - 10;
		int endy = y + 10 > 99 ? 99 : y + 10;

		for (int i = starty; i < endy; i++) {
			for (int j = startx; j < endx; j++) {
				actions[4] = (health/2)>49 ? 1 : 0;
				actionNodes[4] = s.getRandomCirclePoint(10);
				if (maze[i][j] != '0') {
					switch (maze[i][j]) {
					// pickups
					case '1':
					case '2':
					case '3':
					case '4':
						actions[0] = 1;
						actionNodes[0] = new Coord(i, j);
						break;
					// spartan
					case '5':
						actions[2] = 1;
						actionNodes[2] = new Coord(i, j);
						break;
					// spider (friendly or enemy)
					default:
						actions[checkSpiderType(maze[i][j], spriteChar)] = 1;
						actionNodes[checkSpiderType(maze[i][j], spriteChar)] = new Coord(i, j);
						break;
					}
				}
			}
		}
	}
	public int checkSpiderType(char c, char spriteChar) {
		// decipher if spider character is friend or foe
		if (c == spriteChar) {
			return 1;
		} else {
			return 3;
		}
	}
}
