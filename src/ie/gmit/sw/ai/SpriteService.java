package ie.gmit.sw.ai;

import java.util.ArrayList;
import java.util.List;

import ie.gmit.sw.ai.sprites.Moveable;

public class SpriteService {

	private static SpriteService ss;
	
	private List<Moveable> sprites;
	private SpriteService()
	{
		sprites = new ArrayList<Moveable>();
	}
	public static SpriteService getInstance()
	{
		if(ss==null)
		{
			ss = new SpriteService();
		}
		return ss;
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
}
