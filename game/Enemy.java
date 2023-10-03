package game;

import java.awt.Graphics;
import java.awt.Point;

/**
 * An Enemy is a GameObject that stores its percentage
 * across the Path and moves itself forward.
 * 
 * @author Kevin Cuellar
 * @version 11/25/2022
 */
public class Enemy extends GameObject {
	
	public double percentage; //changes on its own, don't change this unless you override the update method
	// couldn't be bothered to make these private and add getters. 
	// just make sure not to modify these directly, use the setters. 
	private String enemyName;
	protected double percentageMovementSpeed = 0.025;
	protected int livesLostOnReachEnd = 1; 
	protected int offsetX = -20; //offset for when Enemy is drawn on the path
	protected int offsetY = -20; //offset for when Enemy is drawn on the path
	protected String enemyImageFilename;
	protected int health = 1;
	protected int moneyOnDeath = 15;
	
	public static int enemyDrawingPriority = -999_990;
	
	/**
	 * Creates a basic enemy with some basic predefined stats.
	 * 
	 * @param state the State object of the game
	 * @param control the Control object of the game
	 * @param enemyImageFilename the filename for the image of this Enemy
	 */
	public Enemy(State state, Control control, String enemyImageFilename, String enemyName) {
		super(state, control, enemyDrawingPriority);
		enemyDrawingPriority++;
		this.enemyName = enemyName;
		this.enemyImageFilename = enemyImageFilename;
		isVisible = true;
	}
	
	/**
	 * Set the speed of the enemy. Standard is 0.025.
	 * 
	 * @param movementSpeed the speed of the enemy. Standard is 0.025
	 * @return this Enemy
	 */
	public Enemy setMovementSpeed(double movementSpeed) {
		percentageMovementSpeed = movementSpeed;
		return this;
	}
	
	/**
	 * Set the amount of money the enemy drops on death. Standard is 15.
	 * 
	 * @param moneyOnDeath the amount of money the enemy drops on death
	 * @return this Enemy
	 */
	public Enemy setDeathMoney(int moneyOnDeath) {
		this.moneyOnDeath = moneyOnDeath;
		return this;
	}
	
	/**
	 * Set the amount of lives lost when this enemy reaches the end
	 * 
	 * @param livesToLose the amount of lives lost when this enemy reaches the end
	 * @return this Enemy
	 */
	public Enemy setLivesToLose(int livesToLose) {
		livesLostOnReachEnd = livesToLose;
		return this;
	}
	
	/**
	 * Set the image to use for this enemy
	 * 
	 * @param imageFileName the image to use for this enemy
	 * @return this Enemy
	 */
	public Enemy setEnemyImage(String imageFileName) {
		enemyImageFilename = imageFileName;
		return this;
	}
	
	/**
	 * Set the offset of the enemy image on the path
	 * 
	 * @param x the x offset
	 * @param y the y offset
	 * @return this Enemy
	 */
	public Enemy setImageOffset(int x, int y) {
		this.offsetX = x;
		this.offsetY = y;
		return this;
	}
	
	/**
	 * Set the health of this Enemy
	 * 
	 * @param health the health of this Enemy
	 * @return this Enemy
	 */
	public Enemy setHealth(int health) {
		this.health = health; 
		if(health <= 0) {
			this.isExpired = true;
			state.setMoney(state.getMoney() + moneyOnDeath);
		}
			
		return this;
	}
	
	/**
	 * Returns the health of this Enemy
	 * 
	 * @return health of this Enemy
	 */
	public int getHealth() {
		return health;
	}
	
	/**
	 * Returns the name of the Enemy.
	 * @return the name of the Enemy
	 */
	public String getEnemyName() {
		return enemyName;
	}
	
	/**
	* Moves this Enemy's percentage across Path forward based on percentageMovementSpeed. 
	* Will expire the Enemy if it has reached the end of the path
	* and remove the amount of lives signified through constructor.
    */
	@Override
	public void update(double timeElapsed) {
		percentage += (percentageMovementSpeed * state.getElapsedTime());
		if(percentage >= 0.99) {
			isExpired = true;
			state.setLives(state.getLives() - livesLostOnReachEnd);
		}
	}

	/**
	* Draws the Enemy at the correct coordinates based on the percentage
	* across the Path.
    * 
    * @param g the Graphics object to draw to
    */
	@Override
	public void draw(Graphics g) {
		Point loc = control.getPath().convertToCoordinates(percentage);
		loc.x += offsetX;
		loc.y += offsetY;
        g.drawImage(control.getImage(enemyImageFilename), loc.x, loc.y, null);
	}
}
