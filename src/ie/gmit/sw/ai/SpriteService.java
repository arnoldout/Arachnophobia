package ie.gmit.sw.ai;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;

import ie.gmit.sw.ai.sprites.Moveable;
import ie.gmit.sw.ai.sprites.Spartan;

public class SpriteService {

	private static SpriteService ss;
	private ConcurrentMap<String, ScheduledFuture<Double>> set = new ConcurrentHashMap<String, ScheduledFuture<Double>>();
	private Moveable[][] maze;

	private SpriteService() {
		maze = new Moveable[100][100];
	}

	public static SpriteService getInstance() {
		if (ss == null) {
			ss = new SpriteService();
		}
		return ss;
	}

	public Moveable findSpartan() {
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				Moveable m = maze[i][j];
				if (m instanceof Spartan) {
					return m;
				}
			}
		}
		return null;
	}

	public void killSprite(String id) {
		try {
			if (set.get(id).cancel(false)) {
				set.remove(id);
				for (int i = 0; i < maze.length; i++) {
					for (int j = 0; j < maze[i].length; j++) {
						Moveable m = maze[i][j];
						if ((m != null) && m.getId().equals(id)) {
							maze[m.getRow()][m.getCol()] = null;
							break;
						}
					}
				}
			}
		} catch (NullPointerException e) {
			System.out.println("Sprite already dead");
		}
	}

	public ScheduledFuture<Double> putFuture(String arg0, ScheduledFuture<Double> arg1) {
		return set.put(arg0, arg1);
	}

	public void addSprite(Moveable arg0, int row, int col) {
		maze[row][col] = arg0;
	}

	public void replaceSprite(Moveable arg0, int row, int col, int oldRow, int oldCol) {
		maze[row][col] = arg0;
		maze[oldRow][oldCol] = null;
	}

	public Moveable findSprite(int row, int col, int charType) {
		Moveable m = maze[row][col];
		if (m == null) {
			int startx = col - 4 < 0 ? 0 : col - 4;
			int endx = col + 4 > 99 ? 99 : col + 4;
			int starty = row - 4 < 0 ? 0 : row - 4;
			int endy = row + 4 > 99 ? 99 : row + 4;

			// scan 3x3 area of nearby squares for enemies
			// if found enemy, then attack it, if it's a
			// friendly spider (same colour), then it gets a health buff
			for (int i = starty; i <= endy; i++) {
				for (int j = startx; j <= endx; j++) {
					m = maze[i][j];
					if (m != null) {
						if (m.getSpriteChar() == charType) {
							return m;
						}
					}
				}
			}
		} else {
			return m;
		}
		return null;
	}

	public double euclideanDistance(int x1, int y1, int x2, int y2) {
		return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
	}
}
