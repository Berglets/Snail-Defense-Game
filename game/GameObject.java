package game;

import java.awt.Graphics;

/**
 * GameObjects are entities within the game. 
 * They are updated and drawn internally by extending classes.
 * 
 * @author Kevin Cuellar
 * @version 12/5/2022
 */
public abstract class GameObject {
	protected boolean isVisible = true;
	protected boolean isExpired;
	public int drawingPriority; //higher numbers are drawn last (on top)
	/*
	 * Background: -1_000_000
	 * Enemies: -999_999 - 0
	 */
	
	//state and control for which game this object is in
	protected State state;
	protected Control control;
	
	/**
	* Constructor that initiates all GameObjects with a state and a control
	* 
	* @param state the State object of the game
    * @param control the Control object of the game
    * @param drawingPriority the priority for when this GameObject should be drawn, higher numbers are drawn last (on top), if not drawn, then this doesn't matter
    */	
	protected GameObject(State state, Control control, int drawingPriority) {
		this.state = state;
		this.control = control;
		this.drawingPriority = drawingPriority;
	}
	
    /**
     * Returns visibility of this GameObject on the JPanel.
     * 
     * @return The visibility of this GameObject on the JPanel
     */
	public boolean isVisible() { return isVisible; }
	
    /**
     * Returns whether or not this GameObject is expired.
     * 
     * @return whether or not this GameObject is expired
     */
    public boolean isExpired() { return isExpired; }
    
    /**
     * How to change the object each time update is called
     * 
     * @param timeElapsed the time elapsed from the start of the game
     */
	abstract public void update(double timeElapsed);
	
    /**
     * Draws the GameObject on to the given Graphics object.
     * 
     * @param g the Graphics object to draw to
     */
	abstract public void draw(Graphics g);
}
