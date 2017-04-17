package ie.gmit.sw.ai.sprites;

import java.util.Random;

import ie.gmit.sw.ai.Maze;
import net.sourceforge.jFuzzyLogic.*;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class BlackSpider extends Spider {
	FIS fis;Maze m ;

	Random r = new Random();
	public BlackSpider(Maze model, int x, int y, boolean isAlive) {
		super(model, x, y, isAlive, '\u0036');
		
		String fileName = "fcl/SpiderMove.fcl";
        FIS fis = FIS.load(fileName,true);
        FunctionBlock functionBlock = fis.getFunctionBlock("MovementSystem");
        fis.setVariable("spartanPos", 100); //Apply a value to a variable
        fis.setVariable("health", 100);// Evaluate
        fis.evaluate();       
        m = model;
        Variable risk = functionBlock.getVariable("risk");
        double d =risk.getLatestDefuzzifiedValue();
	}

	@Override
	public void run() {
		lookAround();
		moveLeft();
	}
	public void lookAround()
	{
		for (int i = -3; i < 3; i++) {
			System.out.println();
			for (int j = -3; j < 3; j--) {
				try{
					if(isValidMove((getX()+i),(getY()+j))){
						System.out.print(getSquare((getX()+i), (getY()+j)));
						if(getSquare((getX()+i), (getY()+j))=='5'){
							System.out.println("Sees hero");
						}
					}
				}
				catch(ArrayIndexOutOfBoundsException e)
				{
					//Spider just near the edge, not a problem
				}
			}
		}
	}
}
