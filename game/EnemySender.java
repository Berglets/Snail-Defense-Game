package game;

import java.awt.Graphics;
import java.util.function.Supplier;

/**
 * A GameObject that sends enemies onto the path based on a lot of factors,
 * read the method header for update for a more in-depth explanation.
 * 
 * @author Kevin Cuellar
 * @version 11/26/2022
 */
public class EnemySender extends GameObject{

	private boolean actionPerformedInTimeSlot;
	private EnemyRoundKind[] rounds = EnemyRoundKind.values();
	private int index; //index in rounds
	private int counter; //how many times a specific Enemy has been sent out this round
	private boolean isWaitRound;
	private int roundNumber;
	
	/*
	 * you may change the following four fields:
	 */
	private final double secondsPerRound = 4;
	private final double secondsPerWait = 3;
	private double variableVariableStart = 0.01;
	private double variableVariableChange = 0.005;
	
	/**
	 * Creates the EnemySender GameObject
	 * 
	 * @param state the State object of the game
	 * @param control the Control object of the game
	 */
	protected EnemySender(State state, Control control) {
		super(state, control, 0);
	}
	
    /**
     * Summons the Enemies in the same order that they are declared in EnemyRoundKind,
     * The amount of Enemies specified by the first Enum value in enemiesPerRound will be sent out over secondsPerRound time.
     * There will then be a wait period where no enemies are sent for secondsPerWait time.
     * Then next the Enum value will be sent out followed by a wait period and so on...
     * 
     * You can specify how the amount of enemies will change as the rounds go by in enemyChangePerSend.
     * You can specify the specific enemy with specific properties in produceEnemy
     * You can use the VariableVariable field (A variable which is variable over time, more specifically increments per round) 
     * to make any property of the Enemy variable (like its health, its speed, its size, lives it takes, etc.).
     * 
     * @param timeElapsed the time elapsed from the start of the game
     */
	@Override
	public void update(double timeElapsed) {
		double spr = secondsPerRound;
		if(isWaitRound)
			spr = secondsPerWait;
		double timeInRound = timeElapsed % spr;
		double howOftenToSendEnemy = spr / (rounds[index].enemiesPerRound + (rounds[index].enemyChangePerSend * roundNumber)); //in seconds		
		if(!actionPerformedInTimeSlot && timeInRound % howOftenToSendEnemy < howOftenToSendEnemy/2.0) {
			actionPerformedInTimeSlot = true;
			counter++;
			
			if(!isWaitRound)
				state.addGameObject(rounds[index].produceEnemy.produce(state, control, variableVariableStart));
		}
		else if(timeInRound % howOftenToSendEnemy > howOftenToSendEnemy/2.0) {
			actionPerformedInTimeSlot = false;
		}
		
		if(counter >= rounds[index].enemiesPerRound + (rounds[index].enemyChangePerSend * roundNumber)) {
			counter = 0;
			roundNumber++;
			if(!isWaitRound)
				index++;
			variableVariableStart += variableVariableChange;
			isWaitRound = !isWaitRound;
			if(index >= rounds.length)
				index = 0;
		}
	}

	/**
	 * Does nothing.
	 * 
	 * @param g the Graphics object to draw to
	 */
	@Override
	public void draw(Graphics g) {
		//do Nothing
	}
	
	/**
	 * An Enum used as a list of Enemies to send out with their specific values.
	 * They get sent out in order top to bottom.
	 * 
	 * @author Kevin Cuellar
	 * @version 11/26/2022
	 */
	private enum EnemyRoundKind {
		
		SNAIL0(5, 0.25, (s, c, d) -> (new Snail(s, c).setMovementSpeed(0.025))),
		SCARGO0(2, 0.1, (s, c, d) -> (new SCargo(s, c).setMovementSpeed(0.025))), 
		SNAIL1(5, 0.4, (s, c, d) -> (new Snail(s, c).setMovementSpeed(0.025))),
		SCARGO1(2, 0.2, (s, c, d) -> (new SCargo(s, c).setMovementSpeed(0.025))),
		TURBO0(2, 0.1, (s, c, d) -> (new Snail(s, c).setMovementSpeed(0.08))),
		SNAIL2(5, 0.25, (s, c, d) -> (new Snail(s, c).setMovementSpeed(0.025))),
		SCARGO2(2, 0.1, (s, c, d) -> (new SCargo(s, c).setMovementSpeed(0.025))), 
		SNAIL3(5, 0.4, (s, c, d) -> (new Snail(s, c).setMovementSpeed(0.025))),
		SCARGO3(2, 0.2, (s, c, d) -> (new SCargo(s, c).setMovementSpeed(0.025))),
		TURBO1(1, 0, (s, c, d) -> (new Snail(s, c).setMovementSpeed((d+0.15) * 1.5)));
	
		public double enemiesPerRound; //will truncate to nearest whole number
		public double enemyChangePerSend; //how much to change the amount of that enemy per round
		TriFunction<State, Control, Double, Enemy> produceEnemy;//the Double is for whatever you want to be variable over time
		
		EnemyRoundKind(double enemiesPerRound, double enemyChangePerSend, TriFunction<State, Control, Double, Enemy> produceEnemy) {
			this.enemiesPerRound = enemiesPerRound;
			this.enemyChangePerSend = enemyChangePerSend;
			this.produceEnemy = produceEnemy;
		}
	}
	
	/**
	 * A functional interface that allows for lambdas with three inputs and an output.
	 * 
	 * @author Kevin Cuellar
	 * @version 11/26/2022
	 */
	@FunctionalInterface
	interface TriFunction<T,U,V,R> {
		public R produce(T t, U u, V v);
	}

}
