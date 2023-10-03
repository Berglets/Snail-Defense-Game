package game;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Holds values regarding the GameObjects in the current frame and the next frame
 * of the game. Allows for going through each "frame", getting current frame GameObjects,
 * and adding GameObjects to the next frame.
 * 
 * @author Kevin Cuellar
 * @version 11/25/2022
 */
public class State {
	
	private PriorityQueue<GameObject> currentFrameGameObjects;
	private PriorityQueue<GameObject> nextFrameGameObjects;
	
    private int money;
    private int lives;
    private boolean isGameOver = false;
    private Control control;
    
    private double elapsedTime; //seconds since last frame
    private long lastFrameStartTime;
    private double secondsSinceGameStart;
    private long gameStartTime;
    
    public boolean isUpgradeMenuOpen;
    
    private static Comparator<GameObject> comparator = (go1, go2) -> {
		if(go1.drawingPriority > go2.drawingPriority)
			return 1;
		else if(go1.drawingPriority < go2.drawingPriority)
			return -1;
		else 
			return 0;
	};
    
	/**
	* Constructor creates the ArrayList that holds the GameObjects.
    */
    public State(Control control) {
    	//priority queue instantiation
    	currentFrameGameObjects = new PriorityQueue<>(comparator);
    	this.control = control;
    	
    	//initialize these to the current time millis
    	gameStartTime = System.currentTimeMillis();
    	lastFrameStartTime = System.currentTimeMillis();
    }
    
	/**
	* Returns The Queue of GameObjects in the current frame.
    * 
    * @return The Queue of GameObjects in the current frame
    */
    public Queue<GameObject> getFrameObjects() {
        return currentFrameGameObjects;
    }
    
	/**
	* Creates the next frame of GameObjects with all the current GameObjects.
    */
    public void startFrame() {
        nextFrameGameObjects = new PriorityQueue<>(comparator);  // Creates empty PriorityQueue
        nextFrameGameObjects.addAll(currentFrameGameObjects);  // Add all the current ones to the new Queue.  This is more clear
        
        //calculate the elapsed time, and set the last Frame start time
        elapsedTime = (System.currentTimeMillis() - lastFrameStartTime) / 1000.0;
        secondsSinceGameStart = (System.currentTimeMillis() - gameStartTime) / 1000.0;
        lastFrameStartTime = System.currentTimeMillis();
        
        //System.out.println("elapsedTime: " + elapsedTime);
    }
    
	/**
	* Moves the next frame of GameObjects into the current frame. 
	* Removes expired objects from frame, and puts certain objects 
	* at the front of the list to be drawn with priority.
    */
    public void finishFrame() {
        //remove expired objects from upcoming frame
    	List<GameObject> temp = new ArrayList<>(nextFrameGameObjects);
    	for(GameObject go : temp)
    		if(go.isExpired()) 
    			nextFrameGameObjects.remove(go);
    	
    	currentFrameGameObjects = nextFrameGameObjects;
        //nextFrameGameObjects = new ArrayList<>();  //was here before
    }
    
    /**
	* Adds a GameObject to the next frame of execution.
	* 
	* @param go the GameObject to add to the next frame.
    */
    public void addGameObject(GameObject go) {
    	nextFrameGameObjects.offer(go);
    }
    
    /**
	* Sets an amount of money available to the player.
	* 
	* @param money the amount of money to set
    */
    public void setMoney(int money) {
    	this.money = money;
    }
    
    /**
	* Returns the amount of money available to the player.
	* 
	* @return the amount of money available to the player
    */
    public int getMoney() {
    	return money;
    }
    
    /**
	* Sets an amount of lives available to the player.
	* 
	* @param lives the amount of lives to set
    */
    public void setLives(int lives) {
    	this.lives = lives;
    	
    	//check for game over
    	if(this.getLives() <= 0) {
    		this.setIsGameOver(true);
    	}
    }
    
    /**
 	* Returns the amount of lives available to the player.
 	* 
 	* @return the amount of lives available to the player
     */
    public int getLives() {
    	return lives;
    }
    
    /**
	* Sets whether or not the game is over.
	* 
	* @param whether or not the game is over
    */
    private void setIsGameOver(boolean isGameOver) {
    	this.isGameOver = isGameOver;
    	
    	if(isGameOver)
    		this.addGameObject(new GameOver(this, control));
    }
    
    
    /**
 	* Returns whether or not the game is over.
 	* 
 	* @return whether or not the game is over
     */
    public boolean getIsGameOver() {
    	return isGameOver;
    }
   
    /**
 	* Returns the elapsed time since the last frame.
 	* 
 	* @return the elapsed time since the last frame
     */
    public double getElapsedTime() {
    	return this.elapsedTime;
    }
    
    /**
 	* Returns total time the game has been running.
 	* 
 	* @return total time the game has been running
     */
    public double getSecondsSinceGameStart() {
    	return secondsSinceGameStart;
    }
}
