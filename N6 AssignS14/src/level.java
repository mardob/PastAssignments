import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import game2D.Sprite;
import game2D.TileMap;

/**
 * @author 2224717 
 * 
 * Abstract class that defines what different levels offer to game
 */
public abstract class level {


	public abstract void draw(Graphics2D g, int xo, int yo, int screenWidth, int screenHeight);
	
	
	/**
	 * 	Method that returns list of coins in this level
	 * 
	 * @return  A reference to a list of coins 
	 */
	public abstract ArrayList<Sprite> getCoins();
	
	/**
	 * 	Method that returns main tile map of this level
	 * 
	 * @return  A reference to a tile map of this level
	 */
	public abstract TileMap getMap();
		
	/**
	 * 	Method that returns secondary, front tile map of this level
	 * 
	 * @return  A reference to a tile map
	 */
	public abstract TileMap getFrontMap();

	/**
	 * 	Method that returns width of this level
	 * 
	 * @return  An int value of width this level
	 */
	public abstract int getMapWidth();	
	
	/**
	 * 	Method that returns height of this level
	 * 
	 * @return  An int value of height this level
	 */
	public abstract int getMapHeight();
	
	/**
	 * 	Method that returns list of saws in this level
	 * 
	 * @return  A reference to a list of saws 
	 */
	public abstract ArrayList<Saw> getSaw();
	
	/**
	 * 	Method that returns tresure in this level
	 * 
	 * @return  A reference to a tresure
	 */
	public abstract Sprite getTresure();
	
	/**
	 * 	Method that returns sprite door that is end of level
	 * 
	 * @return  A reference to a door sprite 
	 */
	public abstract Sprite getEnd();
	
	/**
	 * 	Method that removes coin at the given position in list 
	 * 
	 * @param given int position in list of coins that is suppose to be deleted 
	 */
	public abstract void removeCoin(int given);
	
	/**
	 * 	Method that removes collectible from the level
	 */
	public abstract void removeCollectible();
	
	
	
	/**
     * Loads an image with the given 'fileName'
     * "Borrowed" from GameCore so levels wouldn't have extend Gamecore
     * 
     *@author David Cairns
     * 
     * @param fileName The file path to the image file that should be loaded 
     * @return A reference to the Image object that was loaded
     */
    protected  Image loadImage(String fileName) 
    { 
    	return new ImageIcon(fileName).getImage(); 
    }
}
