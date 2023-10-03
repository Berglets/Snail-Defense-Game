package game;

import java.awt.Graphics;
import java.awt.Point;

/**
 * Draws a SaltTower originally has to be clicked on from the TowerButton
 * then it follows the cursor until it is placed on the map.
 * 
 * @author Kevin Cuellar
 * @version 12/6/2022
 */
public class SaltTower extends Tower {
	

	private boolean midHasPassed; //measured every time timeInBlock is at its middle, a point in time, which is speed/2
	private static final int HOW_FAST_CAN_IT_GO = 20; //don't mess with this, fined tuned.
	
	private Point closestPoint;
	
	/**
	* Constructor that initiates this SaltTower with a state and a control, and
	* a position on the screen.
	* 
	* @param state the State object of the game
    * @param control the Control object of the game
    * @param x the X coordinate of this SaltTower
    * @param y the Y coordinate of this SaltTower
    */	
	public SaltTower(State state, Control control, int x, int y) {
		super(state, control, 100000, new Bounds(50, 50, x, y, -25, -25), "salt.png", 0, 10);
	}

	 /**
     * How to change the object each time update is called
     * 
     * @param timeElapsed the time elapsed from the start of the game
     */
	@Override
	public void onUpdate(double timeElapsed) {
		if(!isMoving && closestPoint == null) {
			closestPoint = control.getPath().closestPoint(bounds.posX, bounds.posY);
		}
		
		double timeElapsedMillis = timeElapsed * HOW_FAST_CAN_IT_GO;
		double timeInBlock = timeElapsedMillis % speed;
		
		if(!midHasPassed && (int)timeInBlock == (int)(speed/2.0)) {
			midHasPassed = true;
			//send salt crystals here
			SaltCrystals s = new SaltCrystals(state, control, bounds.posX, bounds.posY, closestPoint);
			state.addGameObject(s);
		}
		else if(midHasPassed && (int)timeInBlock > (int)(speed/2.0) + 1) {
			midHasPassed = false;
		}
		//nothing happends
	}

	/**
	 * called once Tower does its draw. DO NOT OVERRIDE TOWER'S DRAW METHOD
	 * 
	 * @param g the Graphics object to draw to
	 */
	@Override
	public void onDraw(Graphics g) {
		g.drawImage(control.getImage(imageFileName), bounds.posX, bounds.posY, null);
		
	}
}
