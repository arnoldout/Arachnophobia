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
	try{
		List<XYPair> dists = new ArrayList<XYPair>();
		dists.add(new XYPair(this.getX()+1, this.getY()));
		dists.add(new XYPair(this.getX()-1, this.getY()));
		dists.add(new XYPair(this.getX(), this.getY()+1));
		dists.add(new XYPair(this.getX(), this.getY()-1));
		int spartan = highestSpartanDist(dists);
		System.out.println("Spartan Risk :"+evalSpartanRisk(dists.get(spartan)));
		
		
		doMove(dists.get(spartan).getX(), dists.get(spartan).getY());
	}
	catch(Exception e){
		e.printStackTrace();
	}
	}
	public double evalSpartanRisk(XYPair pair)
	{
		FunctionBlock functionBlock = fis.getFunctionBlock("SpartanSystem");
		functionBlock.setVariable("spartanPos", spartanDist(pair));
		functionBlock.setVariable("health", getHealth());
		functionBlock.evaluate();
		
		functionBlock = fis.getFunctionBlock("BombSystem");
		//calc spartan euclidean distance to bomb
		//calc spider euclidean distance to bomb
		functionBlock.setVariable("spartanPosToBomb", 60);
		functionBlock.setVariable("spiderPosToBomb", 50);
		functionBlock.setVariable("health", getHealth());
		functionBlock.evaluate();
		
		Variable risk = functionBlock.getVariable("risk");
		return risk.getLatestDefuzzifiedValue();

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
