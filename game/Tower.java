package game;

import java.awt.Graphics;

/**
 * A tower is an object that gets taken from the TowerButton corresponding to it then
 * it is dragged to the map where it is placed and can then be used.
 * Any custom implementation must be done inside the inheriting classes.
 * 
 * @author Kevin Cuellar
 * @version 12/5/2022
 */
public abstract class Tower extends Clickable {
	
	private static boolean currentTowerInUse; //whether or not A tower is following the cursor
	protected boolean isMoving; //whether or not THIS tower is following the cursor
	
	String imageFileName;
	int range; //measured in pixels circle around tower 
	int speed = 50; // how often (in millis) to send attack
	int cost; //cost of the tower to initially purchase, then becomes cost of upgrades
	int damage;
	
	
	/**
	* Constructor that initiates this Clickable.
	* 
	* @param state the State object of the game
    * @param control the Control object of the game
    * @param drawPriority priority order to be drawn, higher numbers are drawn last and end up on top
    * @param bounds the bounding box of the GameObject
    * @param imageFileName the filename of the image to use for this Tower
    * @param range not used
    * @param cost the cost of this enemy to place down
    */	
	protected Tower(State state, Control control, int drawingPriority, Bounds bounds, String imageFileName, int range, int cost) {
		super(state, control, drawingPriority, bounds, true);
		this.imageFileName = imageFileName;
		this.range = range;
		this.cost = cost;
		
		//allows for only one tower to be held by the cursor at a time
		if(currentTowerInUse) {
			isExpired = true;
			isVisible = false;
			return;
		}
		
		isVisible = true;
		isMoving = true;
		currentTowerInUse = true;
		state.setMoney(state.getMoney() - cost);
	}
	
	 /**
     * How to change the object each time update is called
     * 
     * @param timeElapsed the time elapsed from the start of the game
     */
	public abstract void onUpdate(double timeElapsed);
	
	/**
	 * called once Tower does its draw. DO NOT OVERRIDE TOWER'S DRAW METHOD
	 * 
	 * @param g the Graphics object to draw to
	 */
	public abstract void onDraw(Graphics g); //must have something or nothing will draw
	
    /**
     * If the tower is currently not placed, will replace its
     * X and Y coordinates to those of the cursor.
     * 
     * @param timeElapsed the time elapsed from the start of the game
     */
	@Override
	public void update(double timeElapsed) {
		if(isMoving) {
			bounds.posX = control.getMouseX() + bounds.offsetX;
			bounds.posY = control.getMouseY() + bounds.offsetY;
		} else {
			onUpdate(timeElapsed);
		}
	}
	
    /**
     * Draws the Tower onto the cursor or if its placed
     * will just stay in one spot.
     * 
     * @param g the Graphics object to draw to
     */
	@Override
	public void draw(Graphics g) {
		if(isMoving) {
			g.drawImage(control.getImage(imageFileName), bounds.posX, bounds.posY, 75, 75, null);
		} else {
			onDraw(g);
		}
		 
	}
	
	/**
	 * Called when the cursor clicks into this objects bounding box.
	 * 
	 * @param mouseX the x position of the mouse
	 * @param mouseY the y position of the mouse
	 */
	@Override
	public boolean onClick(int mouseX, int mouseY) {
		
		//attempts to place the tower
		if(isMoving) {
			isMoving = false;
			currentTowerInUse = false;
			
			//destroys moving tower if it is outside the map or on path
			if(control.isNearPath(mouseX, mouseY) == true || mouseX > 600 || mouseX < 0 || mouseY > 600 || mouseY < 0) {
				isExpired = true;
				state.setMoney(state.getMoney() + cost);
			}
				
		} else if(!currentTowerInUse) { //open upgrade menu for this tower
			state.isUpgradeMenuOpen = true;
			 
			//remove tower buttons and other UgradeMenuButtons
			for(GameObject go : state.getFrameObjects()) {
				if(go instanceof TowerButton || go instanceof UpgradeMenuButton) {
					go.isExpired = true;
				}
			}
			
			Bounds b1 = new Bounds(40, 180, 610, 140, 0, 0);
			
			if(this instanceof SaltCatapult) {
				state.addGameObject(new UpgradeMenuButton(state, control, b1, "DAMAGE UPGRADE: ", cost, 30, this)); //change those numbs so that they are accurate to the tower
			} else {
				state.addGameObject(new UpgradeMenuButton(state, control, b1, "SPEED UPGRADE: ", cost, 30, this)); //change those numbs so that they are accurate to the tower
			}
			
		}

		return true;
	}
}
