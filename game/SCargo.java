package game;

/**
 * An SCargo is a basic enemy GameObject that stores its percentage
 * across the Path and moves itself forward.
 * 
 * @author Kevin Cuellar
 * @version 11/25/2022
 */
public class SCargo extends Enemy {

	/**
	* Constructor that creates Scargo at the start of the path.
	* 
	* @param state the State object of the game
    * @param control the Control object of the game
    */	
	public SCargo(State state, Control control) {
		super(state, control, "s-cargo.png", "scargo");
		setLivesToLose(5).setHealth(10).setMovementSpeed(0.02).setDeathMoney(40);
	}
}
