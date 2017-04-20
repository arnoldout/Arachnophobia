package ie.gmit.sw.ai.sprites;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ie.gmit.sw.ai.Maze;
import ie.gmit.sw.ai.SpriteService;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class SpiderService {
	private static SpiderService ss;
	private FIS fis;

	private SpiderService() {
		String fileName = "fcl/SpiderMove.fcl";
		fis = FIS.load(fileName, true);
	}

	public static SpiderService getInstance() {
		if (ss == null) {
			ss = new SpiderService();
		}
		return ss;
	}
	
	public DistanceRisk getPickupRisk(Maze m, int x, int y, int health) {
		DistanceRisk pair = findNearestItem(m, x, y, new HashSet<Character>(Arrays.asList(new Character('1'),new Character('2'),new Character('3'),new Character('4'))));
		
		if(pair!=null){
			double d = euclideanDistance(x, y, pair.getX(), pair.getY());
			SpriteService ss = SpriteService.getInstance();
			double spartanEuclid = 0;
			for (int i = 0; i < ss.spritesSize(); i++) {
				if (ss.getSprite(i) instanceof Spartan) {
					spartanEuclid = euclideanDistance(ss.getSprite(i).getCol(), ss.getSprite(i).getRow(), pair.getX(), pair.getY());
					break;
				}
			}
			FunctionBlock functionBlock = fis.getFunctionBlock("BombSystem");
			functionBlock.setVariable("spartanPosToBomb", spartanEuclid);
			functionBlock.setVariable("spiderPosToBomb", d);
			functionBlock.setVariable("health", health);
			functionBlock.evaluate();
			Variable risk = functionBlock.getVariable("aggressiveness");
			pair.setRisk(risk.getLatestDefuzzifiedValue());
		}
		else{
			//will just send spider to same spot
			pair = new DistanceRisk(x, y, 0);
		}
		return pair;
	}


	
	public DistanceRisk getFriendlyRisk(Maze m, int x, int y,char spiderType, int health)
	{
		DistanceRisk pair = findNearestItem(m, x, y, new HashSet<Character>(Arrays.asList(new Character(spiderType))));
		double friendDist = 0;
		
		if(pair!=null)
		{
			friendDist = euclideanDistance(pair.getX(), pair.getY(), x, y);
			FunctionBlock functionBlock = fis.getFunctionBlock("FriendHeal");
			functionBlock.setVariable("friendDistance", friendDist);
			functionBlock.setVariable("health", health);
			functionBlock.evaluate();
			Variable risk = functionBlock.getVariable("attraction");
			pair.setRisk(risk.getLatestDefuzzifiedValue());
		}
		else{
			pair = new DistanceRisk(x, y, 0);
		}
		return pair;
	}
	

	public DistanceRisk getSpartanRisk(int x, int y, int health) {
		DistanceRisk spartanRisk = findSpartan();
		if(spartanRisk!=null)
		{
			int spartanDist = spartanDistance(spartanRisk.getX(), spartanRisk.getY(),x, y);
			FunctionBlock functionBlock = fis.getFunctionBlock("SpartanSystem");
			functionBlock.setVariable("spartanPos", spartanDist);
			functionBlock.setVariable("health", health);
			functionBlock.evaluate();
			Variable risk = functionBlock.getVariable("risk");
			spartanRisk.setRisk(risk.getLatestDefuzzifiedValue());
		}
		else{
			spartanRisk = new DistanceRisk(x, y, 0);
		}
		return spartanRisk;
	}
	public DistanceRisk findSpartan()
	{
		SpriteService ss = SpriteService.getInstance();
		for (int i = 0; i < ss.spritesSize(); i++) {
			if (ss.getSprite(i) instanceof Spartan) {
				return new DistanceRisk(ss.getSprite(i).getCol(), ss.getSprite(i).getRow());
			}
		}
		return null;
	}
	public int spartanDistance(int spriteX, int spriteY, int x, int y) {
		int xVal = Math.abs(spriteX - x);
		int yVal = Math.abs(spriteY - y);
		if (xVal <= 10 && yVal <= 10) {
			int averagePercentage = ((xVal + yVal) / 2) * 10;
			return averagePercentage;
		}
		return 0;
	}


	public DistanceRisk findNearestItem(Maze m, int x, int y, Set<Character> chars) {
		/*
		 * Can easily be made better
		 */
		char[][] b = m.getMaze();
		DistanceRisk pair = null;
		// quick search of surrounding area, fuzzy spiders can only see what's
		// in their vision
//		System.out.println("x:" + x + "y:" + y);
		for (int i = -5; i < 6; i++) {
			for (int j = -5; j < 6; j++) {
				try {
					if (chars.contains(new Character(b[x + i][y + j]))) {
						pair = new DistanceRisk(x + i, y + j);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					//just searching outside of array, its fine
				}
			}
		}
		return pair;
	}

	public double euclideanDistance(int x1, int y1, int x2, int y2) {
		return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
	}
}
