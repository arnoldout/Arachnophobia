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
			double pickupRisk = SpiderService.getInstance().getPickupRisk(getModel(), getX(), getY(), getHealth());
			double spartanRisk = SpiderService.getInstance().getSpartanRisk(getX(), getY(), getHealth());
			double friendlyRisk = SpiderService.getInstance().getFriendlyRisk(getModel(), getX(), getY(),getSpriteChar() ,getHealth());
			System.out.println("Pickup Risk :"+pickupRisk);
			System.out.println("Spartan Risk :"+spartanRisk);
			System.out.println("friendly Risk :"+friendlyRisk);
			moveDown();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
