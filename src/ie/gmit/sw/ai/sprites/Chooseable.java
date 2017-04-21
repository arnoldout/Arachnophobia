package ie.gmit.sw.ai.sprites;

import ie.gmit.sw.ai.Maze;
import ie.gmit.sw.ai.traversal.Coord;

public interface Chooseable {

	Coord getGoal(int health, Maze model, int row, int col, Spider s, char spriteChar);

}