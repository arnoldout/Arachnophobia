package ie.gmit.sw.ai.sprites;

public class DistanceRisk {
	private int x;
	private int y;
	private double risk;
	public DistanceRisk(int x, int y, double risk) {
		super();
		this.x = x;
		this.y = y;
		this.risk = risk;
	}
	public DistanceRisk(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public double getRisk() {
		return risk;
	}
	public void setRisk(double risk) {
		this.risk = risk;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
}
