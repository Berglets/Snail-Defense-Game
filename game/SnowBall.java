package game;

import java.awt.Graphics;
import java.awt.Point;
import java.util.Scanner;

import path.Path;

/**
 * A Snowball is sent out by the SaltCatapult and it will try to aim for the closest
 * Enemy, it won't always suceed but it does a lot of damage. 
 * 
 * @author Kevin Cuellar
 * @version 12/5/2022
 */
public class SnowBall extends GameObject {

	private int x, y;
	private Path p;
	private double percentage;
	private static final double PERCENTAGE_CHANGE = 0.10;
	private static final int OFFSET_X = -20;
	private static final int OFFSET_Y = -20;
	private int DAMAGE_DONE = 1;
	private Enemy aimedEnemy;
	
	/**
	* Constructor that initiates this SnowBall with a state and a control, and
	* a position on the screen and a position to move towards.
	* 
	* @param state the State object of the game
    * @param control the Control object of the game
    * @param spawnX the X coordinate of this SaltCrystal
    * @param spawnY the Y coordinate of this SaltCrystal
    * @param aimPos the Point on the path that this SaltCrystal is aiming for.
    * @param aimedEnemy the enemy this SnowBall is aiming for
    * @param damage the amount of damage done by this SnowBall
    */	
	protected SnowBall(State state, Control control, int spawnX, int spawnY, Point aimPos, Enemy aimedEnemy, int damage) {
		super(state, control, -999_998);
		this.aimedEnemy = aimedEnemy;
		DAMAGE_DONE = damage;
		x = spawnX;
		y = spawnY;
		p = new Path(new Scanner("2\n" + spawnX + " " + spawnY + "\n" + (aimPos.x + OFFSET_X) + " " + (aimPos.y + OFFSET_Y)));
	}

    /**
     * How to change the object each time update is called
     * 
     * @param timeElapsed the time elapsed from the start of the game
     */
	@Override
	public void update(double timeElapsed) {
		if(percentage < 1.0) {
			Point newP = p.convertToCoordinates(percentage);
			x = newP.x;
			y = newP.y;
			percentage += PERCENTAGE_CHANGE;
		}
	
		if(percentage >= 1.0) {
			aimedEnemy.setHealth(aimedEnemy.getHealth() - DAMAGE_DONE);
			isExpired = true;
		}
		
	}

    /**
     * Draws the GameObject on to the given Graphics object.
     * 
     * @param g the Graphics object to draw to
     */
	@Override
	public void draw(Graphics g) {
		g.drawImage(control.getImage("snowball.png"), x, y, 20, 20, null);
	}

}
