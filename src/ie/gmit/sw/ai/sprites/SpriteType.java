package ie.gmit.sw.ai.sprites;

import ie.gmit.sw.ai.Maze;

public enum SpriteType { 
	spider_black {
		@Override
		public Moveable getNewInstance(String id, Maze m, int row, int col, boolean isAlive) {
			return new BlackSpider(id, m, row, col, isAlive);
		}
	},spider_red {
		@Override
		public Moveable getNewInstance(String id, Maze m, int row, int col, boolean isAlive) {
			return new RedSpider(id, m, row, col, isAlive);
		}
	},spider_orange{

		@Override
		public Moveable getNewInstance(String id, Maze m, int row, int col, boolean isAlive) {
			return new OrangeSpider(id,m, row, col, isAlive);
		}
		
	},spider_grey{

		@Override
		public Moveable getNewInstance(String id, Maze m, int row, int col, boolean isAlive) {
			// TODO Auto-generated method stub
			return new GreySpider(id, m, row, col, isAlive);
		}
		
	},spider_blue{

		@Override
		public Moveable getNewInstance(String id, Maze m, int row, int col, boolean isAlive) {
			// TODO Auto-generated method stub
			return new BlueSpider(id,m, row, col, isAlive);
		}
		
	},spider_green{

		@Override
		public Moveable getNewInstance(String id, Maze m, int row, int col, boolean isAlive) {
			// TODO Auto-generated method stub
			return new GreenSpider(id, m, row, col, isAlive);
		}
		
	},spider_brown{
		public Moveable getNewInstance(String id, Maze m, int row, int col, boolean isAlive) {
			// TODO Auto-generated method stub
			return new BrownSpider(id, m, row, col, isAlive);
		}
	},spider_yellow{

		@Override
		public Moveable getNewInstance(String id, Maze m, int row, int col, boolean isAlive) {
			// TODO Auto-generated method stub
			return new YellowSpider(id, m, row, col, isAlive);
		}
	},
	spartan {
		@Override
		public Moveable getNewInstance(String id, Maze m, int row, int col, boolean isAlive) {
			return new Spartan(id, m, row, col, isAlive);
		}
	};
	public abstract Moveable getNewInstance(String id, Maze m, int row, int col, boolean isAlive);
}
