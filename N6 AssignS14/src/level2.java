import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import game2D.*;


/**
 * @author 2224717 
 * 
 * 	2. level of the game
 */
public class level2 extends level{

	public static int worldWidth = 5600;
	public static int worldHeight = 1720;
	
    private  Sprite end = null;
    private  BufferedImage img = null;
    private  BufferedImage mountains = null;
    Player 	 player;
    private  Sprite tresure= null ; 
    private  ArrayList<Saw> saws = new ArrayList<Saw>();
    private  ArrayList<Sprite> coins = new ArrayList<Sprite>();
    private  TileMap frontMap = new TileMap();
    private  TileMap tmap = new TileMap();



    public level2(Player player) {
    	this.player = player;
		lvlInit();
	}//level2


    private void lvlInit(){
    	 Sprite s;	// Temporary reference to a sprite
    	tmap.loadMap("maps", "lvl2.txt");
    	
    	 player.setX(64);
         player.setY(280);
         player.setVelocityX(0);
         player.setVelocityY(0);
    	
         	try {
         		img = ImageIO.read(new File("maps/trees.png"));
         		mountains = ImageIO.read(new File("maps/mountains far far away.png"));
         	} catch (IOException e) {System.out.println("Problem with loading of images for level 2");}
         
         
      //Sets up animation for saw one sprite only for now and fills list of saw
      // sprites with saws at their positions.
      Animation sawmill = new Animation();
      sawmill.addFrame(loadImage("images/sawTiny.png"), 1000);
    	  float[] sawsY = new float[]{480.0f, 480.0f, 480.0f, 480.0f, 480.0f};
    	  float[] sawsX = new float[]{280.0f, 1160.0f, 1320.0f, 1600.0f, 1920.0f};
      		for (int c=0; c<sawsY.length; c++){
      			Saw sa = new Saw(sawmill);
      			sa.setX(sawsX[c]);
      			sa.setY(sawsY[c]);
      			sa.moveOnY(0.2f);
      			sa.show();
      			saws.add(sa);
      	}
      
  	  
     
  	  
      // Creates animation for "coins" that is red ruby
      Animation ruby = new Animation();
    	ruby.addFrame(loadImage("images/ruby.png"), 1000);
    	if(ruby!=null){
    		//Creates 2 arrays of positions of coins in the world and then adds coins themselves to the world.
    		float[] coinsY = new float[]{320.0f, 320.0f, 320.0f, 320.0f, 320.0f, 320.0f};
    		float[] coinsX = new float[]{400.0f, 720.0f, 880.0f, 1640.0f,2240.0f, 3120.0f };
    		for (int c=0; c<coinsY.length; c++)
    		{
    			s = new Sprite(ruby);
        		s.setX(coinsX[c]);
        		s.setY(coinsY[c]);
        		s.show();
        		coins.add(s);
    		}
    	}
        
        //Sets up player at starting position and sets up starting velocity of player 
        player.setX(64);
        player.setY(280);
        player.setVelocityX(0);
        player.setVelocityY(0);
        
        
        // Creates and adds door that is ending of the level.
        Animation door = new Animation();
        door.addFrame(loadImage("images/door.png"), 1000);
        if(door!=null){
        	end = new Sprite(door);
        	end.setX(3640);
        	end.setY(600);
        	end.show();
        }
        
        
    	//Creates chest and sets it's position. Chest is single collectible in the each level.
    	Animation chest = new Animation();
    	chest.addFrame(loadImage("images/tresure2.png"), 1000);
    	if(chest!=null){
    		tresure = new Sprite(chest);
    		tresure.setX(3200);
    		tresure.setY(600);
    		tresure.show();
    	}
        
  } //lvlInit 
    
    /**
     * Method that draws all sprites and background images this level contains
     * 
     * @param Graphics2D graphic object to draw into
     * @param xo Int offset in X	
     * @param yo Int offset in Y
     * @param ScreenWidth int value of resolutions width (X)
     * @param ScreenHeight Int value of resolutions height (Y)
     *  
     **/
    public void draw(Graphics2D g, int xo, int yo,int screenWidth, int screenHeight){
    //	int width = img.getWidth();
	//	int Height = img.getHeight();
    	
		if(mountains!=null)
		g.drawImage(mountains, (int)(screenWidth + xo) , 40+yo, null);
		
		if(img!=null)
		for(int i=0;i<=9;i++){	
				g.drawImage(img, i*img.getWidth()+(int)(xo*1.3), 140+yo, img.getWidth(), img.getHeight(), null);			
			}
		
		//Draws the main tile map
		if(tmap!=null)
        tmap.draw(g,xo,yo); 
		
        //Draws all of the coins
        for (Sprite s: coins){
        	s.setOffsets(xo,yo);
        	s.draw(g);
        }    
      //Draws all of the saws
        for (Saw s: saws){
        	s.setOffsets(xo,yo);
        	s.draw(g);
        }  
        
        //Draws end of level
        if(end !=null){
        end.setOffsets(xo, yo);
    	end.draw(g);
        }
    	
    	if(tresure!=null){
        	tresure.setOffsets(xo, yo);
        	tresure.draw(g);
        }
       
	}//draw
    
    
    
    
    
	/**
	 * 	Method that returns list of saws in this level
	 * 
	 * @return  A reference to a list of saws 
	 */
	public ArrayList<Saw> getSaw(){
		return saws;
	} 
    
	/**
	 * 	Method that removes coin at the given position in list 
	 * 
	 * @param given int position in list of coins that is suppose to be deleted 
	 */
 	public void removeCoin(int given){
 		coins.remove(given);
 	}
 	
 	/**
	 * 	Method that removes collectible from the level
	 */
 	public void removeCollectible(){
		tresure = null;
	} 

 	/**
	 * 	Method that returns list of coins in this level
	 * 
	 * @return  A reference to a list of coins 
	 */
    public ArrayList<Sprite> getCoins(){
		return coins;
	}
	
    /**
	 * 	Method that returns main tile map of this level
	 * 
	 * @return  A reference to a tile map of this level
	 */
	public TileMap getMap(){
		return tmap;
	}	
	
	/**
	 * 	Method that returns tresure in this level
	 * 
	 * @return  A reference to a tresure
	 */
	public Sprite getTresure(){
		return tresure;
	} 
	
	/**
	 * 	Method that returns sprite door that is end of level
	 * 
	 * @return  A reference to a door sprite 
	 */
	public Sprite getEnd(){
		return end;
	}
	
	/**
	 * 	Method that returns secondary, front tile map of this level
	 * 
	 * @return  A reference to a tile map
	 */
	public TileMap getFrontMap(){
		return frontMap;
	}	
	
	/**
	 * 	Method that returns width of this level
	 * 
	 * @return  An int value of width this level
	 */
	public int getMapWidth(){
		return worldWidth;
	}
	
	/**
	 * 	Method that returns height of this level
	 * 
	 * @return  An int value of height this level
	 */
	public int getMapHeight(){
		return worldHeight;
	}
	
	

}
