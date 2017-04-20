package ie.gmit.sw.ai.sprites;

import java.util.Random;

import ie.gmit.sw.ai.Maze;

public class BlackSpider extends Spider {

	Random r = new Random();
	public BlackSpider(String id, Maze model, int x, int y, boolean isAlive) {
		super(id, model, x, y, isAlive, '\u0036');        
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
