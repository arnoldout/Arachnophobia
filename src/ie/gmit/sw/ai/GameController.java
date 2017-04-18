package ie.gmit.sw.ai;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import ie.gmit.sw.ai.sprites.Moveable;
import ie.gmit.sw.ai.sprites.SpriteType;
import ie.gmit.sw.ai.traversal.MazeNodeConverter;
import ie.gmit.sw.ai.traversal.Node;

//maybe rename this... because reasons....
public class GameController {
	private Maze model;
	private static Node[][] traversableMaze;
	private ScheduledExecutorService god;// I hope blasphemy isn't still illegal
											// here...

	@SuppressWarnings("unused")
	private GameController() {
		// require the model at instantiation
	}

	public GameController(Maze model) {
		this.model = model;
		// not sure if this works just yet, but it's there. Heuristics should
		// still be euclidean distance, since the area is so wide open
		// this actually still might be viable for later.
		traversableMaze = MazeNodeConverter.makeTraversable(model);
		god = new ScheduledThreadPoolExecutor(1000);
		// need to init all the spiders
		// which need to be runnable
		// then each spider's .run needs to continually do ai science
		// they need the model too, in order to update their position
		// so be sure to pass it to them.

		// This block just causes the addFeature to loop through the int value
		// of the character
		// and adds that many of that to the map at random spots
		// we can use this stuff, just need to make sure we are creating the
		// runnables as well.
		addFeature(SpriteType.spider_black, '\u0036', '0');
		addFeature(SpriteType.spider_blue, '\u0037', '0'); 
		addFeature(SpriteType.spider_brown, '\u0038', '0'); 
		addFeature(SpriteType.spider_green, '\u0039', '0'); 
		addFeature(SpriteType.spider_grey, '\u003A', '0'); 
		addFeature(SpriteType.spider_orange, '\u003B', '0'); 
		addFeature(SpriteType.spider_red, '\u003C', '0'); 
		addFeature(SpriteType.spider_yellow, '\u003D', '0'); 
	}

	// this might be an issue later, due to the way the search algos work, they
	// set a boolean in the node to true when it is the goal node.
	// so having one master node array wont work without us rewriting the
	// algorithm to take in a goal nodes pos@traversal
	public static Node[][] getNodes() {
		return traversableMaze.clone();
	}

	// just plops the character representation of whatever the 'thing' is at
	// it's position.
	// the spiders will be updating their position themselves, this is just for
	// init purposes
	private void addFeature(SpriteType s, char feature, char replace) {
		int counter = 0;
		while (counter < feature) {

			int row = (int) (model.getMaze().length * Math.random());
			int col = (int) (model.getMaze()[0].length * Math.random());

			if (model.get(row, col) == replace) {
				model.set(row, col, feature);
				counter++;

				// this creates the spider and gives it to the scheduler, every
				// 2 seconds it calls the spiders run method.
				god.scheduleAtFixedRate(s.getNewInstance(model, row, col, true), 0, 1, TimeUnit.SECONDS);
			}
		}
	}

	public void placePlayer(int x, int y) {
		Moveable spartan = SpriteType.spartan.getNewInstance(model, x, y, true);
		god.scheduleAtFixedRate(spartan, 0, 2, TimeUnit.SECONDS);
	}
}
