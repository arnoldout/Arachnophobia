package ie.gmit.sw.ai.sprites;

import ie.gmit.sw.ai.Maze;

public class BrownSpider extends Spider{

	public BrownSpider(String id, Maze model, int x, int y, boolean isAlive) {
		super(id,model, x, y, isAlive, '\u0038');
		
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
