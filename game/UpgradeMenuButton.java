package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * Draws an UpgradeMenuButton over which upgrades are chosen onto the Menu.
 * Must be expired then recreated every upgrade cycle
 * 
 * @author Kevin Cuellar
 * @version 12/5/2022
 */
public class UpgradeMenuButton extends Clickable {

	private static final int ARC_SIZE = 20;
	private static final int OFFSET_X = 5;
	private static final int OFFSET_Y = 26;
	private String text;
	private int cost;
	private int costIncrease;
	
	private Tower tower;
	
	/**
	* Constructor that initiates this UpgradeMenuButton with a state and a control
	* 
	* @param state the State object of the game
    * @param control the Control object of the game
    * @param bounds the bounding box of the GameObject and where it will be drawn
    * @param text the text to display in the button
    * @param cost the cost of the upgrade given by this button
    * @param costIncrease the amount of increase given by each subsequent purchase of the upgrade
    * @param tower the tower that this button buys (doesn't have to be a specific object)
    */	
	public UpgradeMenuButton(State state, Control control, Bounds bounds, String text, int cost, int costIncrease, Tower tower) {
		super(state, control, 1_000_000, bounds, true);
		this.text = text;
		this.cost = cost;
		this.costIncrease = costIncrease;
		this.tower = tower;
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
		g.fillRoundRect(bounds.posX, bounds.posY, bounds.width, bounds.height, ARC_SIZE, ARC_SIZE);
		//draw text
		g.setColor(Color.WHITE);
		g.setFont(new Font("", Font.BOLD, 15));
		g.drawString(text + "$" + cost, bounds.posX + OFFSET_X , bounds.posY + OFFSET_Y); 
	}
	
	/**
	 * Called when the cursor clicks into this objects bounding box.
	 * 
	 * @param mouseX the x position of the mouse
	 * @param mouseY the y position of the mouse
	 */
	@Override
	public boolean onClick(int mouseX, int mouseY) {
		if(state.getMoney() - cost > 0) { 
			state.setMoney(state.getMoney() - cost);
			//increase cost
			cost += costIncrease;
			tower.cost += costIncrease;
			//make upgrade happen -- make it evident through the image too
			//speed up or damage up depending on tower
			if(tower instanceof SaltTower) {
				tower.speed -= 5;
			} else {
				tower.damage += 10;
			}
			
		}
		return true;
	}

}
