package game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Creates the game view window for the game using JPanel. 
 * Contains the origin of the Graphics object from which all GameObjects
 * are drawn onto. 
 * 
 * @author Kevin Cuellar
 * @version 11/14/2022
 */
public class View extends JPanel{
	private Control control;
	private State state;
	
	/**
	 * Constructor that initializes the view window and
	 * saves the game Control and State.
	 * 
	 * @param control the Control object correlated to this View
	 * @param state the State object correlated to this View
	 */
	public View(Control control, State state) {
		this.control = control;
		this.state = state;
		
		JFrame frame = new JFrame("Tower Defense");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Dimension d = new Dimension(800, 600);
	    this.setMinimumSize(d);
	    this.setPreferredSize(d);
	    this.setMaximumSize(d);
	     
		frame.setContentPane(this);
		
		frame.pack();
		frame.setVisible(true);
		
	}
	
	/**
	 * Draws the GameObjects onto the screen.
	 * 
	 * @param g the Graphics object to draw to
	 */
	@Override
	public void paint(Graphics g) {
		Queue<GameObject> goq = new PriorityQueue<>(state.getFrameObjects());
		int size = goq.size();
		
		//draw game objects
		for(int i = 0; i < size; i++) {
			GameObject go = goq.poll();
			if (go.isVisible() && !go.isExpired())
                go.draw(g);
		}
	}
}
