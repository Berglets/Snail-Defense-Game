package path;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * A path Object stores a list of points that makes up a path.
 * Each path Object has methods to retrieve values from the path 
 * and a method to add values to the path.
 * 
 * @author Kevin Cuellar
 * @version 11/14/2022
 */
public class Path {
	private ArrayList<Point> pointList;
	private static final int NEAR_AMOUNT = 15; //in pixels 
	private static final int EXTENSION_AMOUNT = 5; //in pixels, the point further on the point formula
	
	/**
    * Constructor - creates a path with no points.
    */
	public Path() {
		pointList = new ArrayList<>();
	}
	
	 /**
     * Constructor - reads points from a given Scanner object
     * and creates a path with them.
     * 
     * format for the path is the amount of points in the path on the first line,
     * then each subsequent line has an x coordinate and a y coordinate with a space
     * in between.
     * 
     * @param in The Scanner object to read from
     */
	public Path(Scanner in) {
		pointList = new ArrayList<>();
		int pointAmount = in.nextInt();
		for(int i = 0; i < pointAmount; i++) {
			Point p = new Point(in.nextInt(), in.nextInt());
			pointList.add(p);
		}
	}
	
	 /**
	 * Returns The amount of points in the Path.
     * 
     * @return The amount of points in the Path
     */
	public int getPointCount() {
		return pointList.size();
	}
	

    /**
     * Gets the x value from the nth point in the path.
     * nth values start at 0.
     * 
     * @param n the nth point in the Path (nth points start at 0). 
     * @return The x value of the nth point in the Path
     */
	public int getX(int n) {
		return pointList.get(n).x;
	}
	
    /**
     * Gets the y value from the nth point in the path.
     * nth values start at 0.
     * 
     * @param n the nth point in the Path (nth points start at 0). 
     * @return The y value of the nth point in the Path
     */
	public int getY(int n) {
		return pointList.get(n).y;
	}
	
    /**
     * Adds the given x and y coordinates as a point on the path.
     * 
     * @param x the x coordinate of the point to add.
     * @param y the y coordinate of the point to add.
     */
	public void add(int x, int y) {
		pointList.add(new Point(x,  y));
	}
	
    /**
     * Returns a string representation of this Path in the Path format.
     * Can be used in a Scanner object to create a copy of this path.
     * 
     * @return string representation of this Path in the Path format
     */
	@Override
	public String toString() {
		int pointAmount = getPointCount();
		String s = "" + pointAmount;
		
		for(int i = 0; i < pointAmount; i++)
			s += "\n" + getX(i) + " " + getY(i);
		return s;
	}
	
	/**
	 * Draws this path on the given Graphics object.
     * Used for testing.
     * 
     * @param g the graphics object to draw this path on
     */
	public void draw(Graphics g) {
	     g.setColor(Color.RED);
	     for(int i = 0; i < getPointCount() - 1; i++) {
	    	 g.drawLine(getX(i), getY(i), getX(i + 1), getY(i + 1));
	     }
	}
	
	/** 
	 * Given a percentage between 0% and 100%, this method calculates
	 * the location along the path that is exactly this percentage
	 * along the path. The location is returned in a Point object
	 * (integer x and y), and the location is a screen coordinate.
	 * 
	 * If the percentage is less than 0%, the starting position is
	 * returned. If the percentage is greater than 100%, the final
	 * position is returned.
	 * 
	 * Callers must not change the x or y coordinates of any returned
	 * point object (or the caller could be changing the path).
	 * 
	 * @param percentTraveled a distance along the path
	 * @return the screen coordinate of this position along the path
	 */
	public Point convertToCoordinates(double percentage) {
		if(percentage <= 0.0) {
			return new Point(getX(0), getY(0)); //returns first point
		} else if(percentage >= 1.0) {
			return new Point(getX(getPointCount()-1), getY(getPointCount()-1)); //returns last point
		}
		
		/*
		 * Calculate total length of path.
		 * if path has < 2 points, will do some weird stuff but shouldn't crash.
		 * This implementation sucks, we don't need to calculate total length or each segment length each time
		 * unless the path is dynamic or something. 
		 * Change later for more speed.
		 */
		double totalLength = 0;
		double[] segmentLengths = new double[getPointCount()-1];
		for(int i = 0; i < getPointCount() - 1; i++) {
			segmentLengths[i] = Math.sqrt((Math.pow(getX(i+1) - getX(i), 2)) + (Math.pow(getY(i+1) - getY(i), 2))); //distance formula
			totalLength += segmentLengths[i];
		}
		
		double lengthMoved = percentage * totalLength;
		double usedDistance = 0;
		int insideSegmentIndex = 0; //which segment are we in? index of segmentLengths
		
		//find segment lengthMoved is in and used distance within segment
		for(int i = 0; i < segmentLengths.length; i++) {
			if(lengthMoved < segmentLengths[i]) { //it is within this segment
				insideSegmentIndex = i;
				usedDistance = lengthMoved;
				break;
			}
			 lengthMoved -= segmentLengths[i]; //converting lengthMoved to UsedDistance
		}
		
		double percentageAcrossSegment = usedDistance / segmentLengths[insideSegmentIndex]; 
		double inverse = (1 - percentageAcrossSegment);
		
		int xResult = (int)((inverse * getX(insideSegmentIndex)) + (percentageAcrossSegment * getX(insideSegmentIndex+1)));
		int yResult = (int)((inverse * getY(insideSegmentIndex)) + (percentageAcrossSegment * getY(insideSegmentIndex+1)));
		
		return new Point(xResult, yResult);
	}
	
	public Point closestPoint(int posX, int posY) {
		Point closestPoint = pointList.get(0);
		double closestDistance = distanceFormula(posX, posY, closestPoint.x, closestPoint.y);
		
		for(Point p : pointList) {
			double distance = distanceFormula(posX, posY, p.x, p.y);
			if(distance <= closestDistance) {
				closestDistance = distance;
				closestPoint = p;
			}
		}
		
		return closestPoint;
	}
	
	public static double distanceFormula(double x1, double y1, double x2, double y2) {
		double ac = Math.abs(y2 - y1);
	    double cb = Math.abs(x2 - x1);
	        
	    return Math.hypot(ac, cb);
	}
	
}
