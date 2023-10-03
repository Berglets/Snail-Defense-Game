package game;

import java.awt.Graphics;
import java.awt.Point;
import java.util.Scanner;

import path.Path;
/**
 * SaltCrystals are shout out by SaltTowers and they do 1 damage on any enemy that goes across them.
 * 
 * @author Kevin Cuellar
 * @version 12/6/2022
 */
public class SaltCrystals extends GameObject {

	public Bounds bounds; //can be null if not ready to be used
	private int x, y;
	private Path p;
	private double percentage;
	private static final double PERCENTAGE_CHANGE = 0.20;
	private static final int OFFSET_X = -20;
	private static final int OFFSET_Y = -20;
	public static final int DAMAGE_DONE = 1;
	
	
	/**
	* Constructor that initiates this SaltCrystal with a state and a control, and
	* a position on the screen and a position to move towards.
	* 
	* @param state the State object of the game
    * @param control the Control object of the game
    * @param spawnX the X coordinate of this SaltCrystal
    * @param spawnY the Y coordinate of this SaltCrystal
    * @param aimPos the Point on the path that this SaltCrystal is aiming for.
    */	
	protected SaltCrystals(State state, Control control, int spawnX, int spawnY, Point aimPos) {
		super(state, control, -999_999);
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
		if(percentage <= 2.0) {
			Point newP = p.convertToCoordinates(percentage);
			x = newP.x;
			y = newP.y;
			percentage += PERCENTAGE_CHANGE;
		}
		
		if(percentage >= 0.7 && bounds == null) {
			bounds = new Bounds(50, 50, x, y, OFFSET_X, OFFSET_Y);
			Control.crystals.add(this);
		}
	}

    /**
     * Draws the GameObject on to the given Graphics object.
     * 
     * @param g the Graphics object to draw to
     */
	@Override
	public void draw(Graphics g) {
		g.drawImage(control.getImage("salt_crystals.png"), x, y, null);
	}

}
