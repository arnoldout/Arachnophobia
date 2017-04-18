package ie.gmit.sw.ai.sprites;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ie.gmit.sw.ai.Maze;
import ie.gmit.sw.ai.SpriteService;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class BlackSpider extends Spider {
	FIS fis;

	Random r = new Random();
	public BlackSpider(Maze model, int x, int y, boolean isAlive) {
		super(model, x, y, isAlive, '\u0036');
		
		String fileName = "fcl/SpiderMove.fcl";
        fis = FIS.load(fileName,true);
        
        
	}
	@Override
	public void run() {
	
		List<XYPair> dists = new ArrayList<XYPair>();
		dists.add(new XYPair(this.getX()+1, this.getY()));
		dists.add(new XYPair(this.getX()-1, this.getY()));
		dists.add(new XYPair(this.getX(), this.getY()+1));
		dists.add(new XYPair(this.getX(), this.getY()-1));
		int y = highestSpartanDist(dists);
		
		FunctionBlock functionBlock = fis.getFunctionBlock("MovementSystem");
		fis.setVariable("spartanPos", spartanDist((dists.get(y))));
		fis.setVariable("health", getHealth());
		fis.evaluate();
		
		Variable risk = functionBlock.getVariable("risk");
		System.out.println("Risk :"+risk.getLatestDefuzzifiedValue());
		
		doMove(dists.get(y).getX(), dists.get(y).getY());
	}
	public int highestSpartanDist(List<XYPair> a)
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
	public int spartanDist(XYPair p)
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
}
