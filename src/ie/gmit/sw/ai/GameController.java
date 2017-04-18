package ie.gmit.sw.ai;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import ie.gmit.sw.ai.sprites.Moveable;
import ie.gmit.sw.ai.sprites.SpriteType;

//maybe rename this... because reasons....
public class GameController {
	private Maze model;
	private ScheduledExecutorService god;//I hope blasphemy isn't still illegal here...
	private SpriteService spriteService;
	@SuppressWarnings("unused")
	private GameController() {
		//require the model at instantiation
	}

	public GameController(Maze model) {
		this.model = model;
		spriteService = SpriteService.getInstance();
		god= new ScheduledThreadPoolExecutor(1000);
		// need to init all the spiders
		// which need to be runnable
		// then each spider's .run needs to continually do ai science
		// they need the model too, in order to update their position
		// so be sure to pass it to them.
		
		//This block just causes the addFeature to loop through the int value of the character
		//and adds that many of that to the map at random spots
		//we can use this stuff, just need to make sure we are creating the runnables as well.
		addFeature(SpriteType.spider_black,'\u0036', '0'); // 6(int=54) is a Black Spider, 0 is a hedge
//		addFeature(SpriteType.spider_blue,'\u0037', '0'); // 7(int=55) is a Blue Spider, 0 is a hedge
//		addFeature(SpriteType.spider_brown,'\u0038', '0'); // 8(int=56) is a Brown Spider, 0 is a hedge
//		addFeature(SpriteType.spider_green,'\u0039', '0'); // 9(int=57) is a Green Spider, 0 is a hedge
//		addFeature(SpriteType.spider_grey,'\u003A', '0'); // :(int=58) is a Grey Spider, 0 is a hedge
//		addFeature(SpriteType.spider_orange,'\u003B', '0'); // ;(int=59) is a Orange Spider, 0 is a hedge
//		addFeature(SpriteType.spider_red,'\u003C', '0'); // <(int=60) is a Red Spider, 0 is a hedge
//		addFeature(SpriteType.spider_yellow,'\u003D', '0'); // =(int=61) is a Yellow Spider, 0 is a hedge
		
	}

	//just plops the character representation of whatever the 'thing' is at it's position.
	//the spiders will be updating their position themselves, this is just for init purposes
	private void addFeature(SpriteType s, char feature, char replace) {
		int counter = 0;
		while (counter < feature) {


			int row = (int) (model.getMaze().length * Math.random());
			int col = (int) (model.getMaze()[0].length * Math.random());

			if (model.get(row, col) == replace) {
				model.set(row, col, feature);
				counter++;
				Moveable m = s.getNewInstance(model, row, col, true);
				spriteService.addSprite(m);
				//this creates the spider and gives it to the scheduler, every 2 seconds it calls the spiders run method.
				god.scheduleAtFixedRate(m, 0, 2, TimeUnit.SECONDS);
			}
		}
	}
	public void placePlayer(int x, int y){
		Moveable spartan = SpriteType.spartan.getNewInstance(model, x, y, true);
		spriteService.addSprite(spartan);
		god.scheduleAtFixedRate(spartan, 0, 2, TimeUnit.SECONDS);
	}
}
