package game;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import path.Path;

/**
 * Initializes the GameObjects and moves through each frame.
 * Does things that generally control the application.
 * 
 * @author Kevin Cuellar
 * @version 12/6/2022
 */
public class Control implements Runnable, ActionListener, MouseListener, MouseMotionListener {
	
	private State state;
	private View view;
	private static Path path;
	private Map<String, BufferedImage> imageMap;
	private int mouseX, mouseY;
	
	public static List<SaltCrystals> crystals;
	
	public static Bounds firstButton = new Bounds(50, 50, 610, 120, 0, 0);
	public static Bounds secondButton = new Bounds(50, 50, 680, 120, 0, 0);
	
	/**
	 * Constructor - makes Swing invoke this object's run() method later.
	 */
	public Control() {
		SwingUtilities.invokeLater(this);
	}
	
	/**
	 * Invoked when the GUI thread 'runs later'.
	 */
	@Override
	public void run() {
		
		// Preinit --- load path from file
		final String pathFileFilePath = "resources/path_2.txt"; //change if name of file changes
		
		try {
			ClassLoader myLoader = this.getClass().getClassLoader();
	        InputStream pathStream = myLoader.getResourceAsStream(pathFileFilePath);
	        Scanner pathScanner = new Scanner(pathStream);
	        path = new Path(pathScanner);
		} catch(NullPointerException e) { 
			e.printStackTrace();
			System.out.println("Could not find or load " + pathFileFilePath);
            System.exit(0);  // Close the frame, bail out.
		}
	    
		// Initialization 
		state = new State(this);
        view = new View(this, state);
        imageMap = new HashMap<>();
        state.setMoney(1000);
        state.setLives(10);
        crystals = new ArrayList<>();
        
	    //add listeners
	    view.addMouseListener(this);
	    view.addMouseMotionListener(this);
	    
        //Jumpstarts the initial frame and draws it
        state.startFrame();  // Prepares the creation of the 'next' frame
        state.addGameObject(new Background(state, this));  // Add one background object to our list
        state.addGameObject(new Menu(state, this));  
        openTowersMenu(); //spawns the two tower buttons
        state.addGameObject(new EnemySender(state, this));
        state.finishFrame();    // Mark the next frame as ready

        view.repaint();  // Draw it.
        
        Timer t = new Timer(16, this); // Triggers every 16 milliseconds, reports to ActionListener
        t.start();
	}
	
	/**
	 * Returns the path that is being used for this game.
	 * 
	 * @return the path that is being used for this game
	 */
	public Path getPath() {
		return path;
	}
	
	/**
	 * Fetches the image from a file in the "resources" folder each time a unique filename is given.
	 * Stores filename keys in a map with the image values to retrieve faster each subsequent time
	 * the same filename is given.
	 * 
	 * @param filename the file name of the image inside the "resources" folder 
	 * @return the image from that file
	 */
	public BufferedImage getImage(String filename) {
        //if image has been loaded into map, return it
		if(imageMap.containsKey(filename))
			return imageMap.get(filename);
		
		try {
            ClassLoader myLoader = Control.class.getClassLoader();
            InputStream imageStream = myLoader.getResourceAsStream("resources/" + filename);
            BufferedImage image = javax.imageio.ImageIO.read(imageStream);
            imageMap.put(filename, image);
            System.out.println("Loading " + filename); //TA verification 
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        	System.out.println("Could not find or load resources/" + filename);
            System.exit(0);  // Close the frame, bail out.
            return null;  // Does not happen, the application has exited.
        }
	}

	/**
	 * Triggered on a timer in this class's run() method. 
	 * Starts a new frame with last frame's GameObjects,
	 * updates all the GameObjects, then updates the current frame 
	 * to be this next frame, then draws onto screen.
	 * 
	 * @param e the ActionEvent to be processed
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		state.startFrame();
		
		//don't update if game over
        if(!state.getIsGameOver()) {
        	//update all GameObjects
        	for(GameObject go : state.getFrameObjects()) {
        		go.update(state.getSecondsSinceGameStart());
        		
        		//check for collisions
        		if(go instanceof Enemy) {
        			Enemy en = ((Enemy)go);
        			Point p = getPath().convertToCoordinates(en.percentage);
        			p.x += en.offsetX;
        			p.y += en.offsetY;
        			
        			List<SaltCrystals> cList = new ArrayList<>(crystals);
        			for(SaltCrystals sc : cList) {
        				//check if mouse is over bounding box and that game is not over
        				if(sc.bounds == null)
        					continue;
        				if(en.isExpired == true)
        					continue;
        				if(p.x < sc.bounds.posX || p.x > sc.bounds.posX + sc.bounds.width)
        					continue;
        				if(p.y < sc.bounds.posY || p.y > sc.bounds.posY + sc.bounds.height)
        					continue;
        				
        				//take off health
        				en.setHealth(en.getHealth() - SaltCrystals.DAMAGE_DONE);
        				//destroy crystals
        				sc.isExpired = true;
        				crystals.remove(sc);
        			}
        			
        		}
        	}
        }
        state.finishFrame();
        view.repaint(); //essentially calls paint
	}

    /**
	* Returns the current X coordinate of the mouse.
	* 
	* @return the current X coordinate of the mouse
    */
	public int getMouseX() {
		return mouseX;
	}
	
    /**
	* Returns the current Y coordinate of the mouse.
	* 
	* @return the current Y coordinate of the mouse
    */
	public int getMouseY() {
		return mouseY;
	}
	
	/**
	 * Goes through each GameObject and allows the first object to accept a click
	 * to accept a click.
	 * 
	 * @param e unused
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		for(GameObject go : state.getFrameObjects()) {
			if(go instanceof Clickable) {
				Clickable c = (Clickable) go;
				if(c.consumeClick(mouseX, mouseY))
					return;
			}
		}
		//nothing was clicked, close upgrade menu if open
		if(state.isUpgradeMenuOpen) {
			state.isUpgradeMenuOpen = false;
			for(GameObject go : state.getFrameObjects()) {
				if(go instanceof UpgradeMenuButton) {
					go.isExpired = true;
				}
			}
			openTowersMenu();
		}
	}
	/**
	 * Stores the mouse X and Y coordinates every time it moves
	 * 
	 * @param e the MouseEvent to get mouse coordinates from
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		//System.out.println(e.getX() + " " + e.getY() + " " + "COLOR: " + getImage("mask.png").getRGB(mouseX, mouseY));
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * returns whether or not a point is on or near the path
	 * 
	 * @param posX the x position of the point
	 * @param posY the y position of the point
	 * @return whether or not a point is on or near the path
	 */
	public boolean isNearPath(int posX, int posY) {
		if(getImage("mask.png").getRGB(posX, posY) == -1)
			return false;
		else
			return true;
	}
	
	/**
	 *  creates the Objects needed for the towers menu screen
	 */
	public void openTowersMenu() {
       state.addGameObject(new TowerButton(state, this, firstButton, "salt.png"));
       state.addGameObject(new TowerButton(state, this, secondButton, "catapult.png"));
	}
}
