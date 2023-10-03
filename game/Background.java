package game;

import java.awt.Graphics;

/**
 * Background is a static image that determines what "map" 
 * the game is played on.
 * 
 * @author Kevin Cuellar
 * @version 11/14/2022
 */
public class Background extends GameObject{
	
    /**
     * Constructor that sets background visible and non-expired.
     * 
     * @param state the State object of the game
     * @param control the Control object of the game
     */
	public Background(State state, Control control) {
		super(state, control, -1_000_000);
		isVisible = true;
		isExpired = false;
	}
	
    /**
     * Does nothing --- the background is static
     * 
     * @param timeElapsed the time elapsed from the start of the game
     */
	@Override
	public void update(double timeElapsed) {
		//nothing --- the background doesn't update
	}

    /**
     * Draws the background image onto the screen.
     * 
     * @param g the Graphics object to draw to
     */
	@Override
	public void draw(Graphics g) {
		g.drawImage(control.getImage("path_2.jpg"), 0, 0, null); //draw background
	}

}
