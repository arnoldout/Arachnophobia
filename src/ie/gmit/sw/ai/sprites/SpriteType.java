package ie.gmit.sw.ai.sprites;

import ie.gmit.sw.ai.Sprite;

public enum SpriteType {
	hedge{
		@Override
		public Sprite getNewInstance()throws Exception{
			return new Sprite("Hedge", "resources/hedge.png");
		}
	},
	sword{
		@Override
		public Sprite getNewInstance()throws Exception{
			return new Sprite("Sword", "resources/sword.png");
		}
	},
	help{
		@Override
		public Sprite getNewInstance()throws Exception{
			return new Sprite("Help", "resources/help.png");
		}
	},
	regularBomb{
		@Override
		public Sprite getNewInstance()throws Exception{
			return new Sprite("Bomb", "resources/bomb.png");
		}
	},
	hydrogenBomb{
		@Override
		public Sprite getNewInstance()throws Exception{
			return new Sprite("Hydrogen Bomb", "resources/h_bomb.png");
		}
	},
	spartan{
		@Override
		public Sprite getNewInstance()throws Exception{
			return new Sprite("Spartan Warrior", "resources/spartan_1.png", "resources/spartan_2.png");
		}
	},
	spider_black {
		@Override
		public Sprite getNewInstance() throws Exception{
			// TODO Auto-generated method stub
			return new Sprite("Black Spider", "resources/black_spider_1.png", "resources/black_spider_2.png");
		}
	},
	spider_blue {
		@Override
		public Sprite getNewInstance() throws Exception{
			// TODO Auto-generated method stub
			return new Sprite("Blue Spider", "resources/blue_spider_1.png", "resources/blue_spider_2.png");
		}
	},
	spider_brown {
		@Override
		public Sprite getNewInstance() throws Exception{
			// TODO Auto-generated method stub
			return new Sprite("Brown Spider", "resources/brown_spider_1.png", "resources/brown_spider_2.png");
		}
	},
	spider_green {
		@Override
		public Sprite getNewInstance() throws Exception{
			return new Sprite("Green Spider", "resources/green_spider_1.png", "resources/green_spider_2.png");
		}
	},
	spider_grey {
		@Override
		public Sprite getNewInstance() throws Exception{
			return new Sprite("Grey Spider", "resources/grey_spider_1.png", "resources/grey_spider_2.png");
		}
	},
	spider_orange {
		@Override
		public Sprite getNewInstance() throws Exception{
			return new Sprite("Orange Spider", "resources/orange_spider_1.png", "resources/orange_spider_2.png");
		}
	},
	spider_red {
		@Override
		public Sprite getNewInstance() throws Exception{
			return new Sprite("Red Spider", "resources/red_spider_1.png", "resources/red_spider_2.png");
		}
	},
	spider_yellow {
		@Override
		public Sprite getNewInstance() throws Exception{
			return new Sprite("Yellow Spider", "resources/yellow_spider_1.png", "resources/yellow_spider_2.png");
		}
	};
	
	public abstract Sprite getNewInstance() throws Exception;
}
