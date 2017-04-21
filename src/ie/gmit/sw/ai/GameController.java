package ie.gmit.sw.ai;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import ie.gmit.sw.ai.sprites.FuzzyChoiceImpl;
import ie.gmit.sw.ai.sprites.Moveable;
import ie.gmit.sw.ai.sprites.NeuralChoiceImpl;
import ie.gmit.sw.ai.sprites.Spider;
import ie.gmit.sw.ai.sprites.SpriteType;

//maybe rename this... because reasons....
public class GameController {
	private Maze model;
	private ScheduledThreadPoolExecutor god;//I hope blasphemy isn't still illegal here...
	private SpriteService spriteService;

	@SuppressWarnings("unused")
	private GameController() {
		// require the model at instantiation
	}

	@SuppressWarnings("unchecked")
	public GameController(Maze model) {
		this.model = model;

		spriteService = SpriteService.getInstance();
		god= new ScheduledThreadPoolExecutor(1000);
		god.setRemoveOnCancelPolicy(true);
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

		Queue<Moveable> spiders = new LinkedList<Moveable>();
		spiders.addAll(addFeature(SpriteType.spider_black, '\u0036', '0'));
		spiders.addAll(addFeature(SpriteType.spider_blue, '\u0037', '0')); 
		spiders.addAll(addFeature(SpriteType.spider_brown, '\u0038', '0')); 
		spiders.addAll(addFeature(SpriteType.spider_green, '\u0039', '0')); 
		spiders.addAll(addFeature(SpriteType.spider_grey, '\u003A', '0')); 
		spiders.addAll(addFeature(SpriteType.spider_orange, '\u003B', '0')); 
		spiders.addAll(addFeature(SpriteType.spider_red, '\u003C', '0')); 
		spiders.addAll(addFeature(SpriteType.spider_yellow, '\u003D', '0'));
		int counter = 0;
		while(!spiders.isEmpty()){
			//a third of all spiders should be neural
			Spider m = (Spider)spiders.poll();
			if(counter%3==0)
			{
 				m.setDecisionMaker(new NeuralChoiceImpl());
			}
			else{
				m.setDecisionMaker(new FuzzyChoiceImpl());
			}
			spriteService.putFuture(m.getId(),(ScheduledFuture<Double>) god.scheduleAtFixedRate(m, 0, 1, TimeUnit.SECONDS));	
			counter++;
		}
		
	}

	// this might be an issue later, due to the way the search algos work, they
	// set a boolean in the node to true when it is the goal node.
	// so having one master node array wont work without us rewriting the
	// algorithm to take in a goal nodes pos@traversal

	// just plops the character representation of whatever the 'thing' is at
	// it's position.
	// the spiders will be updating their position themselves, this is just for
	// init purposes
	private Queue<Moveable> addFeature(SpriteType s, char feature, char replace) {
		int counter = 0;
		Queue<Moveable> sprites = new LinkedList<Moveable>();
		while (counter < 10) {

			int row = (int) ((model.getMaze().length-2) * Math.random()+1);
			int col = (int) ((model.getMaze()[0].length-2) * Math.random()+1);

			if (model.get(row, col) == replace) {
				counter++;
				String uniqueID = UUID.randomUUID().toString();
				Moveable m = s.getNewInstance(uniqueID,model, row, col, true);
				spriteService.addSprite(m, row, col);
				sprites.add(m);
				//this creates the spider and gives it to the scheduler, every 2 seconds it calls the spiders run method.
			}
		}
		return sprites;
	}

	@SuppressWarnings("unchecked")
	public void placePlayer(int row, int col) {
		String uniqueID = UUID.randomUUID().toString();
		Moveable spartan = SpriteType.spartan.getNewInstance(uniqueID,model, row, col, true);
		spriteService.addSprite(spartan, row, col);
		spriteService.putFuture(uniqueID,(ScheduledFuture<Double>) god.scheduleAtFixedRate(spartan, 0, 2, TimeUnit.SECONDS));
	}
}