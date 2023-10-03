package game;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Draws the TowerButton over which towers are chosen onto the Menu.
 * Must be expired then recreated every upgrade cycle
 * 
 * @author Kevin Cuellar
 * @version 12/5/2022
 */
public class TowerButton extends Clickable {
	
	private static int buttonSize = 50;
	private static int imageInButtonSize = 40;
	private static int arcSize = 20;
	private static int offsetInImage = 5;
	
	private String imageFileName;
	
	/**
	* Constructor that initiates this TowerButton with a state and a control
	* 
	* @param state the State object of the game
    * @param control the Control object of the game
    * @param bounds the bounding box of the GameObject and where it will be drawn
    * @param imageFileName the file path name of the image to be drawn inside this button
    */	
	public TowerButton(State state, Control control, Bounds bounds, String imageFileName) {
		super(state, control, 1000, bounds, true);
		this.imageFileName = imageFileName;
	}

	 /**
     * Does nothing --- TowerButtons are static
     * 
     * @param timeElapsed the time elapsed from the start of the game
     */
	@Override
	public void update(double timeElapsed) {
		//doesn't update
		//change so that if upgrading this dies
	}

    /**
     * Draws the TowerButton onto the screen with the specified image.
     * 
     * @param g the Graphics object to draw to
     */
	@Override
	public void draw(Graphics g) {
		g.setColor(new Color(85, 87, 87));
		g.fillRoundRect(bounds.posX, bounds.posY, buttonSize, buttonSize, arcSize, arcSize);
		g.drawImage(control.getImage(imageFileName), bounds.posX + offsetInImage, bounds.posY + offsetInImage, imageInButtonSize, imageInButtonSize, null); 
	}

	/**
	 * Called when the cursor clicks into this objects bounding box.
	 * 
	 * @param mouseX the x position of the mouse
	 * @param mouseY the y position of the mouse
	 */
	@Override
	public boolean onClick(int mouseX, int mouseY) {
		if(imageFileName.equals("salt.png")) {
			state.addGameObject(new SaltTower(state, control, mouseX, mouseY));
		} else {
			state.addGameObject(new SaltCatapult(state, control, mouseX, mouseY));
		}
		
		return true;
	}

}
