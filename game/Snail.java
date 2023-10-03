package game;

/**
 * A snail is a basic enemy GameObject that stores its percentage
 * across the Path and moves itself forward.
 * 
 * @author Kevin Cuellar
 * @version 11/25/2022
 */
public class Snail extends Enemy {

	/**
	* Constructor that creates snail at the start of the path.
	* 
	* @param state the State object of the game
    * @param control the Control object of the game
    */	
	public Snail(State state, Control control) {
		super(state, control, "snail.png", "snail");
	}
	
	/**
	 * Set the speed of the Snail. Standard is 0.025.
	 * if the snail is faster than 0.08, becomes turbo.
	 * 
	 * @param movementSpeed the speed of the enemy. Standard is 0.025
	 * @return this Enemy
	 */
	@Override
	public Enemy setMovementSpeed(double speed) {
		super.setMovementSpeed(speed);
		if(percentageMovementSpeed >= 0.08)
			setEnemyImage("turbo.png");
		else
			setEnemyImage("snail.png");
		return this;
	}
}
