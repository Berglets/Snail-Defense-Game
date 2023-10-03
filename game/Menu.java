package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * Draws the menu over which towers are chosen, lives and money are displayed,
 * and other menu-related things.
 * 
 * @author Kevin Cuellar
 * @version 11/21/2022
 */
public class Menu extends GameObject {

	/**
	* Constructor that initiates this Menu with a state and a control
	* 
	* @param state the State object of the game
    * @param control the Control object of the game
    */	
	public Menu(State state, Control control) {
		super(state, control, 100);
		isVisible = true;
	}

    /**
     * Does nothing --- the menu is static
     * 
     * @param timeElapsed the time elapsed from the start of the game
     */
	@Override
	public void update(double timeElapsed) {
		//nothing --- the Menu doesn't update
	}

    /**
     * Draws the Menu onto the screen.
     * 
     * @param g the Graphics object to draw to
     */
	@Override
	public void draw(Graphics g) {
		//draw crazy frog and overlay
		g.drawImage(control.getImage("crazifrog.PNG"), 600, 0, 200, 600, null);
		g.setColor(new Color(165, 210, 232, 230));
		g.fillRect(600, 0, 200, 600);
		
		//LIVES + MONEY
		g.setColor(Color.BLACK);
		g.setFont(new Font("", Font.BOLD, 30));
		g.drawString("LIVES: " + state.getLives() , 610, 100);
		g.drawString("MONEY: " + state.getMoney() , 610, 70);
		
	}

}
