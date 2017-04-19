package ie.gmit.sw.ai.traversal;

public class Coord {
	private int row;
	private int col;

	public Coord(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public int getRow() {
		return this.row;
	}

	public int getCol() {
		return this.col;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Coord) {
			return (((Coord) o).getRow() == this.getRow() && ((Coord) o).getCol() == this.getCol());
		}

		return false;
	}

	@Override
	public String toString() {
		return this.getRow() + "/" + this.getCol();
	}

	public int getHeuristic(Coord goal) {
		double x1 = this.col;
		double y1 = this.row;
		double x2 = goal.getCol();
		double y2 = goal.getRow();
		return (int) Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
	}
}