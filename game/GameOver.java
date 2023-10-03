package game;

import java.awt.Graphics;

/**
 * GameOver is a static image that stops further play because
 * the player has lost the game.
 * 
 * @author Kevin Cuellar
 * @version 11/25/2022
 */
public class GameOver extends GameObject{
	
    /**
     * Constructor that sets GameOver visible and non-expired.
     * 
     * @param state the State object of the game
     * @param control the Control object of the game
     */
	public GameOver(State state, Control control) {
		super(state, control, 100_000_000);
		isVisible = true;
		isExpired = false;
	}
	
    /**
     * Does nothing --- the GameOver screen is static
     * 
     * @param timeElapsed the time elapsed from the start of the game
     */
	@Override
	public void update(double timeElapsed) {
		//nothing --- the background doesn't update
	}

    /**
     * Draws the GameOver image onto the screen.
     * 
     * @param g the Graphics object to draw to
     */
	@Override
	public void draw(Graphics g) {
		g.drawImage(control.getImage("gameover.png"), 0, 0, null); //draw game over screen
	}

}
