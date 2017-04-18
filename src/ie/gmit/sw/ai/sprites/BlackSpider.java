package ie.gmit.sw.ai.sprites;

import java.util.Random;

import ie.gmit.sw.ai.Maze;

public class BlackSpider extends Spider {

	Random r = new Random();
	public BlackSpider(Maze model, int x, int y, boolean isAlive) {
		super(model, x, y, isAlive, '\u0036');        
	}
	@Override
	public void run() {
		try{
			DistanceRisk pickupRisk = SpiderService.getInstance().getPickupRisk(getModel(), getX(), getY(), getHealth());
			DistanceRisk spartanRisk = SpiderService.getInstance().getSpartanRisk(getX(), getY(), getHealth());
			DistanceRisk friendlyRisk = SpiderService.getInstance().getFriendlyRisk(getModel(), getX(), getY(),getSpriteChar() ,getHealth());
			makeMove(compareRisks(pickupRisk, spartanRisk, friendlyRisk));
			//quickly compare 3 objects, get the highest object, if his x is bigger than ours, move up, if y is bigger, move left, if both randomize
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void makeMove(DistanceRisk risk)
	{
		if(risk.getX()!=this.getX()&&risk.getY()!=this.getY())
		{
			int Low = 1;
			int High = 3;
			int result = r.nextInt(High-Low) + Low;
			//if objective is equally far 
			if(result ==1)
			{
				risk.setX(this.getX());
			}
			else{
				risk.setY(this.getY());
			}
		}
		if(risk.getX()>this.getX())
		{
			moveDown();
		}
		else if(risk.getY()>this.getY())
		{
			moveRight();
		}
		if(risk.getX()<this.getX())
		{
			moveUp();
		}
		else if(risk.getY()<this.getY())
		{
			moveLeft();
		}
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
