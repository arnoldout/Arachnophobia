package ie.gmit.sw.ai.sprites;

import ie.gmit.sw.ai.Maze;

public enum SpriteType {
	spider_black {
		@Override
		public Moveable getNewInstance(Maze m, int x, int y, boolean isAlive) {
			return new BlackSpider(m, x, y, isAlive);
		}
	},spider_red {
		@Override
		public Moveable getNewInstance(Maze m, int x, int y, boolean isAlive) {
			return new RedSpider(m, x, y, isAlive);
		}
	},spider_orange{

		@Override
		public Moveable getNewInstance(Maze m, int x, int y, boolean isAlive) {
			return new OrangeSpider(m, x, y, isAlive);
		}
		
	},spider_grey{

		@Override
		public Moveable getNewInstance(Maze m, int x, int y, boolean isAlive) {
			// TODO Auto-generated method stub
			return new GreySpider(m, x, y, isAlive);
		}
		
	},spider_blue{

		@Override
		public Moveable getNewInstance(Maze m, int x, int y, boolean isAlive) {
			// TODO Auto-generated method stub
			return new BlueSpider(m, x, y, isAlive);
		}
		
	},spider_green{

		@Override
		public Moveable getNewInstance(Maze m, int x, int y, boolean isAlive) {
			// TODO Auto-generated method stub
			return new GreenSpider(m, x, y, isAlive);
		}
		
	},spider_brown{
		public Moveable getNewInstance(Maze m, int x, int y, boolean isAlive) {
			// TODO Auto-generated method stub
			return new BrownSpider(m, x, y, isAlive);
		}
	},spider_yellow{

		@Override
		public Moveable getNewInstance(Maze m, int x, int y, boolean isAlive) {
			// TODO Auto-generated method stub
			return new YellowSpider(m, x, y, isAlive);
		}
	},
	spartan {
		@Override
		public Moveable getNewInstance(Maze m, int x, int y, boolean isAlive) {
			return new Spartan(m, x, y, isAlive);
		}
	};
	public abstract Moveable getNewInstance(Maze m, int x, int y, boolean isAlive);
}
