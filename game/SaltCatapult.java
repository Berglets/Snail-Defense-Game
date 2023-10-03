package game;

import java.awt.Graphics;
import java.awt.Point;

import path.Path;

/**
 * Draws a SaltCatapult originally has to be clicked on from the TowerButton
 * then it follows the cursor until it is placed on the map. It will then spew snowballs
 * at the enemies and does a lot of damage. Careful though because it will often miss and won't
 * always aim for the first enemies!
 * 
 * @author Kevin Cuellar
 * @version 12/6/2022
 */
public class SaltCatapult extends Tower {
	

	private boolean midHasPassed; //measured every time timeInBlock is at its middle, a point in time, which is speed/2
	private static final int HOW_FAST_CAN_IT_GO = 30; //don't mess with this, fined tuned.
	
	
	/**
	* Constructor that initiates this SaltCatapult with a state and a control, and
	* a position on the screen.
	* 
	* @param state the State object of the game
    * @param control the Control object of the game
    * @param x the X coordinate of this SaltTower
    * @param y the Y coordinate of this SaltTower
    */	
	public SaltCatapult(State state, Control control, int x, int y) {
		super(state, control, 100000, new Bounds(50, 50, x, y, -25, -25), "catapult.png", 0, 10);
		this.damage = 3;
	}

	/**
	 * called once Tower does its update. DO NOT OVERRIDE TOWER'S UPDATE METHOD
	 * 
	 * @param timeElapsed time in seconds since game start
	 */
	@Override
	public void onUpdate(double timeElapsed) {
		
		double timeElapsedMillis = timeElapsed * HOW_FAST_CAN_IT_GO;
		double timeInBlock = timeElapsedMillis % speed;
		
		if(!midHasPassed && (int)timeInBlock == (int)(speed/2.0)) {
			midHasPassed = true;
			
			//find closest Enemy
			Enemy closest = null;
			Point position = null;
			double distance = 0;
			
			for(GameObject go : state.getFrameObjects()) {
				if(go instanceof Enemy) {
					Enemy en = (Enemy)go;
					Point p = control.getPath().convertToCoordinates(en.percentage);
					double d = Path.distanceFormula(bounds.posX, bounds.posY, p.x, p.y);
					if(d >= distance) {
						distance = d;
						position = p;
						closest = en;
					}
				}
			}
			//we did find an Enemy
			if(closest != null) {
				SnowBall sb = new SnowBall(state, control, bounds.posX, bounds.posY, position, closest, damage);
				state.addGameObject(sb);
			}
				
			
			
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
		g.drawImage(control.getImage(imageFileName), bounds.posX, bounds.posY, 80, 50, null);
		
	}
}
