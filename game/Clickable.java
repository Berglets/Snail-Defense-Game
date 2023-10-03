package game;

import java.awt.Color;
import java.awt.Graphics;

/**
 * A basic implementation for a GameObject that can be left-clicked. 
 * no update or draw methods because that should be for inheriting classes to implement
 * 
 * @author Kevin Cuellar
 * @version 12/5/2022
 */
public abstract class Clickable extends GameObject {
	
	protected boolean isClickable;
	protected Bounds bounds;
	
	/**
	* Constructor that initiates this Clickable.
	* 
	* @param state the State object of the game
    * @param control the Control object of the game
    * @param drawPriority priority order to be drawn, higher numbers are drawn last and end up on top
    * @param bounds the bounding box of the GameObject
    * @param isClickable whether or not this Clickable is able to be clicked into at the current moment.
    */	
	public Clickable(State state, Control control, int drawPriority, Bounds bounds, boolean isClickable) {
		super(state, control, drawPriority);
		this.bounds = bounds;
		this.isClickable = isClickable;
	}

	/**
	 * Called when the bounding box of this GameObject is left clicked into.
	 * Override to define what should happen when it is clicked.
	 * 
	 * @param mouseX the X position of the mouse
	 * @param mouseY the Y position of the mouse
	 * @return whether or not this Clickable GameObject was able to consume the click
	 */
	public abstract boolean onClick(int mouseX, int mouseY);

	/**
	 * Consumes a click if the click happened within this Clickable's 
	 * boundingBox and if it is clickable at the time this is called.
	 * Do not override this method unless you are changing the behavior of where a
	 * GameObject can be clicked
	 * 
	 * @param mouseX the X position of the mouse
	 * @param mouseY the Y position of the mouse
	 * @return whether or not this Clickable was able to consume the click
	 */
	public boolean consumeClick(int mouseX, int mouseY) {
		//check if mouse is over bounding box and that game is not over
		if(!isClickable)
			return false;
		if(mouseX < bounds.posX || mouseX > bounds.posX + bounds.width)
			return false;
		if(mouseY < bounds.posY || mouseY > bounds.posY + bounds.height)
			return false;
		
		//object was clickable and click was within bounding box
		return onClick(mouseX, mouseY);
	}
}
