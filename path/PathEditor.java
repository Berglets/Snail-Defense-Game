package path;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Used to create and edit paths on the tower defense game
 * graphically. 
 * 
 * @author Kevin Cuellar
 * @version 11/7/2022
 */
public class PathEditor extends JPanel implements Runnable, MouseListener, ActionListener{

	private JMenuItem loadItem;
	private JMenuItem clearItem;
	private JMenuItem saveItem;
	
	private BufferedImage backdrop;
	private Path path = new Path();
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new PathEditor());
	}
	
	 /**
	 * Creates application JFrame with menu bar and other details.
	 * Also sets the background image (the tower defense map)
    */
	@Override
	public void run() {
		 //Setting JFrame details
		 JFrame f = new JFrame("BTD 7");
	     f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     
	     Dimension d = new Dimension(600, 600);
	     this.setMinimumSize(d);
	     this.setPreferredSize(d);
	     this.setMaximumSize(d);
	     
	     f.setContentPane(this); 
	     
	     //adding background image
	     try 
	     {
	         backdrop = javax.imageio.ImageIO.read(new File("src/resources/path_2.jpg"));
	     } 
	     catch (IOException e) 
	     {
	         e.printStackTrace();
	     }
	     
	     //adding JMenu
	     JMenuBar menuBar = new JMenuBar();
	     JMenu fileMenu = new JMenu("File");
	     loadItem = new JMenuItem("Load");
	     clearItem = new JMenuItem("Clear");
	     saveItem = new JMenuItem("Save");
	     
	     menuBar.add(fileMenu);
	     fileMenu.add(loadItem);
	     fileMenu.add(clearItem);
	     fileMenu.add(saveItem);
	     
	     loadItem.addActionListener(this);
	     clearItem.addActionListener(this);
	     saveItem.addActionListener(this);
	     
	     f.setJMenuBar(menuBar);
	     
	     //setup
	     f.pack();
	     f.setVisible(true);
	     
	     this.addMouseListener(this);
	}
	
	 /**
     * Draws the Path path onto the BufferedImage background map. 
     * Also fills the application with light blue color underneath the image.
     * 
     * @param g The Graphics object to draw onto
     */
	public void paint (Graphics g)
    {
	     // Clear the background to a nice light blue color.
	     g.setColor(new Color (0.8f, 0.8f, 1.0f));
	     g.fillRect(0, 0, this.getWidth(), this.getHeight());
	     
	     //draw background
	     g.drawImage(backdrop, 0, 0, null);
	     
	     //draw path
	     g.setColor(Color.RED);
	     for(int i = 0; i < path.getPointCount() - 1; i++) {
	    	 g.drawLine(path.getX(i), path.getY(i), path.getX(i + 1), path.getY(i + 1));
	     }
	    
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	 /**
     * Adds the point the mouse clicked as a point on the Path.
     * 
     * @param e The MouseEvent object to listen to
     */
	@Override
	public void mouseReleased(MouseEvent e) {
		//add new point on the path and repaint
		path.add(e.getX(), e.getY());
		repaint();
		
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
     * Calls the correct method corresponding to each JMenuItem on the application
     * 
     * @param e the ActionEvent to find the JMenuItem that was clicked
     */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == loadItem) {
			loadPath();
		}
		else if (e.getSource() == clearItem) {
			clearPath();
		}
		else if (e.getSource() == saveItem) {
			savePath();
		}
	}
	
	 /**
	 * Asks user for a .txt file to read a Path from, scans it,
	 * replaces the current path with the new one, and paints it
     */
	private void loadPath() {
		//ask user for input file to load path from
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
		chooser.setFileFilter(filter);
		int result = chooser.showOpenDialog(this);
		
		if(result != JFileChooser.APPROVE_OPTION) 
			return; //no file has been loaded
		
		File pathFile = chooser.getSelectedFile();
		
		//create new path using a Scanner object with the file
		try(Scanner scan = new Scanner(pathFile);){
			path = new Path(scan);
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		repaint();
	}
	
	 /**
	 * clears the current path from the screen.
     */
	private void clearPath() {
		path = new Path();
		repaint();
	}
	
	 /**
	 * Asks user for a location to store a Path object, then creates the file (if not already existing)
	 * and stores the Path object in it.
     */
	private void savePath() {
		//ask user for file path to save Path at
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
		chooser.setFileFilter(filter);

		int result = chooser.showSaveDialog(this);
		
		if(result != JFileChooser.APPROVE_OPTION) 
			return; //no file has been loaded
		
		File savePathFile = new File(chooser.getSelectedFile().toString() + ".txt");
		
		//put current path into the given file
		try (PrintWriter out = new PrintWriter(savePathFile);) {
			out.print(path.toString());
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
