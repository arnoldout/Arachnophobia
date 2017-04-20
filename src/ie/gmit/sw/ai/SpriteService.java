package ie.gmit.sw.ai;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;

import ie.gmit.sw.ai.sprites.Moveable;

public class SpriteService {

	private static SpriteService ss;
	
	private List<Moveable> sprites;
	private Map<String,ScheduledFuture<Double>> set = new HashMap<String,ScheduledFuture<Double>>();
	private SpriteService()
	{
		sprites = new CopyOnWriteArrayList<Moveable>();
	}
	
	public static SpriteService getInstance()
	{
		if(ss==null)
		{
			ss = new SpriteService();
		}
		return ss;
	}
	public void killSprite(String id)
	{
		try {
			set.get(id).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ScheduledFuture<Double> putFuture(String arg0, ScheduledFuture<Double> arg1) {
		return set.put(arg0, arg1);
	}
	

	public boolean addSprite(Moveable arg0) {
		return sprites.add(arg0);
	}
	public Moveable getSprite(int arg0) {
		return sprites.get(arg0);
	}
	public int spritesSize() {
		return sprites.size();
	}
	public Moveable findSprite(int x, int y)
	{
		for (int i = 0; i < sprites.size(); i++) {
			if(sprites.get(i).getX() == x && sprites.get(i).getY() == y)
			{
				return sprites.get(i);
			}
		}
		return null;
	}
}
