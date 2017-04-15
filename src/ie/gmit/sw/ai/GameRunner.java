package ie.gmit.sw.ai;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import ie.gmit.sw.ai.sprites.SpriteType;
import ie.gmit.sw.ai.traversal.MazeNodeConverter;
import ie.gmit.sw.ai.traversal.Node;
public class GameRunner implements KeyListener{
	private static final int MAZE_DIMENSION = 100;
	private static final int IMAGE_COUNT = 14;
	private GameView view;
	private Maze model;
	private Node[][] traversableMaze;
	@SuppressWarnings("unused")
	private GameController controller;
	private int currentRow;
	private int currentCol;
	
	public GameRunner() throws Exception{
		model = new Maze(MAZE_DIMENSION);
		//not sure if this works just yet, but it's there. Heuristics should still be euclidean distance, since the area is so wide open
		//this actually still might be viable for later.
		traversableMaze = MazeNodeConverter.makeTraversable(model);
    	view = new GameView(model);
    	controller = new GameController(model);
    	Sprite[] sprites = getSprites();
    	view.setSprites(sprites);
    	
    	placePlayer();
    	
    	Dimension d = new Dimension(GameView.DEFAULT_VIEW_SIZE, GameView.DEFAULT_VIEW_SIZE);
    	view.setPreferredSize(d);
    	view.setMinimumSize(d);
    	view.setMaximumSize(d);
    	
    	JFrame f = new JFrame("GMIT - B.Sc. in Computing (Software Development)");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.addKeyListener(this);
        f.getContentPane().setLayout(new FlowLayout());
        f.add(view);
        f.setSize(1000,1000);
        f.setLocation(100,100);
        f.pack();
        f.setVisible(true);
	}
	
	private void placePlayer(){   	
    	currentRow = (int) (MAZE_DIMENSION * Math.random());
    	currentCol = (int) (MAZE_DIMENSION * Math.random());
    	model.set(currentRow, currentCol, '5'); //A Spartan warrior is at index 5
    	updateView(); 		
	}
	
	private void updateView(){
		view.setCurrentRow(currentRow);
		view.setCurrentCol(currentCol);
	}

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && currentCol < MAZE_DIMENSION - 1) {
        	if (isValidMove(currentRow, currentCol + 1)) currentCol++;   		
        }else if (e.getKeyCode() == KeyEvent.VK_LEFT && currentCol > 0) {
        	if (isValidMove(currentRow, currentCol - 1)) currentCol--;	
        }else if (e.getKeyCode() == KeyEvent.VK_UP && currentRow > 0) {
        	if (isValidMove(currentRow - 1, currentCol)) currentRow--;
        }else if (e.getKeyCode() == KeyEvent.VK_DOWN && currentRow < MAZE_DIMENSION - 1) {
        	if (isValidMove(currentRow + 1, currentCol)) currentRow++;        	  	
        }else if (e.getKeyCode() == KeyEvent.VK_Z){
        	view.toggleZoom();
        }else{
        	return;
        }
        
        updateView();       
    }
    public void keyReleased(KeyEvent e) {} //Ignore
	public void keyTyped(KeyEvent e) {} //Ignore

    
	private boolean isValidMove(int row, int col){
		if (row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == ' '){
			model.set(currentRow, currentCol, '\u0020');
			model.set(row, col, '5');
			return true;
		}else{
			return false; //Can't move
		}
	}
	
	private Sprite[] getSprites() throws Exception{
		//Read in the images from the resources directory as sprites. Note that each
		//sprite will be referenced by its index in the array, e.g. a 3 implies a Bomb...
		//Ideally, the array should dynamically created from the images... 
		Sprite[] sprites = new Sprite[IMAGE_COUNT];
		sprites[1] = SpriteType.sword.getNewInstance();
		sprites[0] = SpriteType.hedge.getNewInstance();
		sprites[2] = SpriteType.help.getNewInstance();
		sprites[3] = SpriteType.regularBomb.getNewInstance();
		sprites[4] = SpriteType.hydrogenBomb.getNewInstance();
		sprites[5] = SpriteType.spartan.getNewInstance();
		sprites[6] = SpriteType.spider_black.getNewInstance();
		sprites[7] = SpriteType.spider_blue.getNewInstance();
		sprites[8] = SpriteType.spider_brown.getNewInstance();
		sprites[9] = SpriteType.spider_green.getNewInstance();
		sprites[10] = SpriteType.spider_grey.getNewInstance();
		sprites[11] = SpriteType.spider_orange.getNewInstance();
		sprites[12] = SpriteType.spider_red.getNewInstance();
		sprites[13] = SpriteType.spider_yellow.getNewInstance();
		return sprites;
	}
	
	public static void main(String[] args) throws Exception{
		new GameRunner();
	}
}