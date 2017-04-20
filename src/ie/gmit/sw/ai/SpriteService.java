package ie.gmit.sw.ai;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

import ie.gmit.sw.ai.sprites.Moveable;

public class SpriteService {

	private static SpriteService ss;
	
	private List<Moveable> sprites;
	private ConcurrentMap<String,ScheduledFuture<Double>> set = new ConcurrentHashMap<String,ScheduledFuture<Double>>();
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
		try{
			if(set.get(id).cancel(false)){
				set.remove(id);
				for (int i = 0; i < sprites.size();i++) {
					if(sprites.get(i).getId().equals(id))
					{
						sprites.remove(i);
						break;
					}
				}
				
			}
		}
		catch(NullPointerException e)
		{
			System.out.println("Sprite already dead");
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
	public Moveable findSprite(int row, int col, int charType)
	{
		Integer lowestPos = null;
		double lowestDist = 1000000;
		System.out.println("Sprites Size :"+sprites.size());
		List<Moveable> l = sprites.stream().filter(sp -> sp.getSpriteChar()==charType).collect(Collectors.toList());
		System.out.println("L Size : "+l.size());
		for (int i = 0; i < l.size(); i++) {
			if(sprites.get(i).getSpriteChar()!=charType){
				double dist = euclideanDistance(sprites.get(i).getRow(),sprites.get(i).getCol(),row,col);
				if(lowestPos==null)
				{
					lowestPos = new Integer(i);
				}
				if(dist<lowestDist)
				{
					lowestDist = dist;
					lowestPos = new Integer(i);
				}
			}
		}
		try{
			return l.get(lowestPos);
		}
		catch(NullPointerException e)
		{
			return null;
		}
	}

	public double euclideanDistance(int x1, int y1, int x2, int y2) {
		return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
	}
}
