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

	
	public double getFriendlyRisk(Maze m, int x, int y,char spiderType, int health)
	{
		XYPair pair = findNearestItem(m, x, y, new HashSet<Character>(Arrays.asList(new Character(spiderType))));
		double friendDist = 0;
		
		if(pair==null)
		{
			friendDist = 0;
		}else{
			friendDist = euclideanDistance(pair.getX(), pair.getY(), x, y);
		}
		System.out.println(friendDist);
		FunctionBlock functionBlock = fis.getFunctionBlock("FriendHeal");
		functionBlock.setVariable("friendDistance", friendDist);
		functionBlock.setVariable("health", health);
		functionBlock.evaluate();
		Variable risk = functionBlock.getVariable("desperateness");
		return risk.getLatestDefuzzifiedValue();
	}
	

	public double getSpartanRisk(int x, int y, int health) {
		int spartanDist = spartanDistance(x, y);
		FunctionBlock functionBlock = fis.getFunctionBlock("SpartanSystem");
		functionBlock.setVariable("spartanPos", spartanDist);
		functionBlock.setVariable("health", health);
		functionBlock.evaluate();
		Variable risk = functionBlock.getVariable("risk");
		return risk.getLatestDefuzzifiedValue();
	}

	public int spartanDistance(int x, int y) {
		SpriteService ss = SpriteService.getInstance();
		for (int i = 0; i < ss.spritesSize(); i++) {
			if (ss.getSprite(i) instanceof Spartan) {
				int xVal = Math.abs(ss.getSprite(i).getX() - x);
				int yVal = Math.abs(ss.getSprite(i).getY() - y);
				if (xVal <= 10 && yVal <= 10) {
					int averagePercentage = ((xVal + yVal) / 2) * 10;
					return averagePercentage;
				}
				break;
			}
		}
		return 0;

	}

	public double getPickupRisk(Maze m, int x, int y, int health) {
		XYPair pair = findNearestItem(m, x, y, new HashSet<Character>(Arrays.asList(new Character('1'),new Character('2'),new Character('3'),new Character('4'))));
		double d = euclideanDistance(x, y, pair.getX(), pair.getY());
		SpriteService ss = SpriteService.getInstance();
		double spartanEuclid = 0;
		for (int i = 0; i < ss.spritesSize(); i++) {
			if (ss.getSprite(i) instanceof Spartan) {
				spartanEuclid = euclideanDistance(ss.getSprite(i).getX(), ss.getSprite(i).getY(), pair.getX(), pair.getY());
				break;
			}
		}
		FunctionBlock functionBlock = fis.getFunctionBlock("BombSystem");
		functionBlock.setVariable("spartanPosToBomb", spartanEuclid);
		functionBlock.setVariable("spiderPosToBomb", d);
		functionBlock.setVariable("health", health);
		functionBlock.evaluate();
		Variable risk = functionBlock.getVariable("aggressiveness");
		return risk.getLatestDefuzzifiedValue();
	}

	public XYPair findNearestItem(Maze m, int x, int y, Set<Character> chars) {
		/*
		 * Can easily be made better
		 */
		char[][] b = m.getMaze();
		XYPair pair = null;
		// quick search of surrounding area, fuzzy spiders can only see what's
		// in their vision
		System.out.println("x:" + x + "y:" + y);
		for (int i = -5; i < 6; i++) {
			for (int j = -5; j < 6; j++) {
				try {
					if (chars.contains(new Character(b[x + i][y + j]))) {
						pair = new XYPair(x + i, y + j);
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
