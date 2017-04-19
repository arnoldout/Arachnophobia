package ie.gmit.sw.ai.sprites;

import ie.gmit.sw.ai.Maze;

public class BlueSpider extends Spider{

	public BlueSpider(Maze model, int x, int y, boolean isAlive) {
		super(model, x, y, isAlive, '\u0037');
	}
	public DistanceRisk compareRisks(DistanceRisk ...distanceRisks )
	{
		//just example, probably needs more coherent implementation
		distanceRisks[0].setRisk(distanceRisks[0].getRisk()+20);
		DistanceRisk highest = new DistanceRisk(0,0,0);
		for (int i = 0; i < distanceRisks.length; i++) {
			if(distanceRisks[i].getRisk()>highest.getRisk()){
				highest = distanceRisks[i];
			}
		}
		return highest;
	}
}
