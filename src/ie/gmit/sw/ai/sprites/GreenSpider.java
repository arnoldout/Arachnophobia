package ie.gmit.sw.ai.sprites;

import ie.gmit.sw.ai.Maze;

public class GreenSpider extends Spider{

	public GreenSpider(Maze model, int x, int y, boolean isAlive) {
		super(model, x, y, isAlive, '\u0039');
		
	}
	public DistanceRisk compareRisks(DistanceRisk ...distanceRisks )
	{
		DistanceRisk highest = new DistanceRisk(0,0,0);
		for (int i = 0; i < distanceRisks.length; i++) {
			if(distanceRisks[i].getRisk()>highest.getRisk()){
				highest = distanceRisks[i];
			}
		}
		return highest;
	}

}
