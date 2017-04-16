package ie.gmit.sw.ai.sprites;

import ie.gmit.sw.ai.Maze;

public enum SpriteType {	
	spider_black {
		@Override
		public Moveable getNewInstance(Maze m, int x, int y, boolean isAlive) {
			return new BlackSpider(m, x, y, isAlive);
		}
	},spider_Red {
		@Override
		public Moveable getNewInstance(Maze m, int x, int y, boolean isAlive) {
			// TODO Auto-generated method stub
			return new RedSpider(m, x, y, isAlive);
		}
	};
	public abstract Moveable getNewInstance(Maze m, int x, int y, boolean isAlive);
}
