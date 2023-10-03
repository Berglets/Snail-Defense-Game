package game;

/**
 * Bounds is the bounding box of a GameObject. Whether it be for
 * drawing it on the screen or for figuring out its hitbox, this is used
 * to get more information about where the GameObject is drawn on screen.
 * 
 * @author Kevin Cuellar
 * @version 12/6/2022
 */
public class Bounds {
	public int height, width, posX, posY, offsetX, offsetY;
	
	/**
	* Constructor that initiates this Bounds object.
	* 
	* @param height the height of the image
    * @param width the width of the image
    * @param posX the x position of the image
    * @param posY the y position of the image
    * @param offsetX x offset of the position
    * @param offsetY y offset of the position
    */	
	public Bounds(int height, int width, int posX, int posY, int offsetX, int offsetY) {
		this.height = height;
		this.width = width;
		this.posX = posX + offsetX;
		this.posY = posY + offsetY;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}
}
