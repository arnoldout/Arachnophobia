package ie.gmit.sw.ai.sprites;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ie.gmit.sw.ai.Maze;
import ie.gmit.sw.ai.SpriteService;

public abstract class Spider extends Moveable{
	//eventually move to classes
	//FIS fis;

	Random r = new Random();
	public Spider(Maze model, int x, int y, boolean isAlive, char spriteChar) {
		super(model, x, y, isAlive, spriteChar);
		String fileName = "fcl/SpiderMove.fcl";
        //fis = FIS.load(fileName,true);
		setHealth(100);
	}
	public void eval()
	{
		//Variable risk = fis.getFunctionBlock("SpartanSystem").getVariable("risk");
		//System.out.println("Risk :"+risk.getLatestDefuzzifiedValue());
	}

	public List<DistanceRisk> getValidMoves()
	{
		List<DistanceRisk> dists = new ArrayList<DistanceRisk>();
		dists.add(new DistanceRisk(this.getX()+1, this.getY()));
		dists.add(new DistanceRisk(this.getX()-1, this.getY()));
		dists.add(new DistanceRisk(this.getX(), this.getY()+1));
		dists.add(new DistanceRisk(this.getX(), this.getY()-1));
		return dists;
	}

	public int highestSpartanDist(List<DistanceRisk> a)
	{
		Integer highestVal = 0;
		for (int i = 0; i < a.size(); i++) {
			if(isValidMove(a.get(i).getX(), a.get(i).getY())){
				if(spartanDist(a.get(i))>highestVal)
				{
					highestVal = i;
				}
			}
			else{
				a.remove(i);
			}
		}
		if(highestVal==0)
		{
			//doesn't matter which direction, so just randomly choose one
			
			int Low = 0;
			int High = a.size();
			highestVal = r.nextInt(High-Low) + Low;
		}
		return highestVal;
	}
	public int spartanDist(DistanceRisk p)
	{
		SpriteService ss = SpriteService.getInstance();
		for (int i = 0; i < ss.spritesSize(); i++) {
			if(ss.getSprite(i) instanceof Spartan)
			{
				int xVal = Math.abs(ss.getSprite(i).getX()-p.getX());
				int yVal = Math.abs(ss.getSprite(i).getY()-p.getY());
				if(xVal<=10&&yVal<=10)
				{
					int averagePercentage = ((xVal+yVal)/2)*10;
					return averagePercentage;
				}
				break;
			}
		}
		return 0;
	}
	@Override
	public abstract void run();
}
