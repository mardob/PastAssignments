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
 * 1. level of the game
 */
public class level1 extends level{

	public static int worldWidth = 5600;
	public static int worldHeight = 1720;
	Player player;
    private   Sprite end = null;
    private  BufferedImage img = null;
    private  Sprite tresure = null;
    private   ArrayList<Saw> saws = new ArrayList<Saw>();
    private   ArrayList<Sprite> coins = new ArrayList<Sprite>();
    private   TileMap frontMap = new TileMap();
    private TileMap tmap = new TileMap();
    
    
	public level1(Player player){
		this.player = player;
		lvlInit();
	}
	
	private void lvlInit(){
		Sprite s;	// Temporary reference to a sprite
		
    	
			
		 player.setX(64);
		 player.setY(280);
		 player.setVelocityX(0);
		 player.setVelocityY(0);
		
			try{
	    		img = ImageIO.read(new File("maps/background.png"));
	    	}catch (IOException e) {System.out.println("error");}
		
		
			tmap.loadMap("maps", "lvl1.txt");
			frontMap.loadMap("maps", "frontMapLvl1.txt");
			
		 // frontMap.loadMap("maps", "frontMapLvl1.txt");
			try{
	    		img = ImageIO.read(new File("maps/background.png"));
	    	}catch (IOException e) {System.out.println("error");}
			
			
			
			

	        
	      //sets up saws in the world
	        Animation sawmill = new Animation();
	        sawmill.addFrame(loadImage("images/sawTiny.png"), 1000);
	        float[] sawsY = new float[]{727.0f};
	        float[] sawsX = new float[]{500.0f};
	        for (int c=0; c<sawsY.length; c++){
	        	Saw sa = new Saw(sawmill);
	        	sa.setX(sawsX[c]);
	        	sa.setY(sawsY[c]);
	        	sa.moveOnX(0.55f);
	        	sa.show();
	          	saws.add(sa);
	        }
	        
	      
	        //sets up doors that mark end of level
	        Animation door = new Animation();
	        door.addFrame(loadImage("images/door.png"), 1000);
	        if(door!=null){
	        	end = new Sprite(door);
	        	end.setX(80);
	        	end.setY(1360);
	        	end.show();
	        }
	    	
	    	//sets up collectibles - chests
	    	Animation chest = new Animation();
	        chest.addFrame(loadImage("images/tresure2.png"), 1000);
	        if(chest!=null){
	        	tresure = new Sprite(chest);
	        	tresure.setX(1800);
	        	tresure.setY(200);
	    		tresure.show();
	        }
	    	
			Animation ruby = new Animation();
	        ruby.addFrame(loadImage("images/ruby.png"), 1000);
	        	float[] coinsY = new float[]{320.0f,360.0f,560.0f,560.0f, 560.0f,560.0f,560.0f, 560.0f};
	        	float[] coinsX = new float[]{400.0f,640.0f,1480.0f,1960.0f, 2720.0f,3280.0f,3720.0f, 4680.0f};
	        	for (int c=0; c<coinsY.length; c++){
	        		s = new Sprite(ruby);
	        		s.setX(coinsX[c]);
	        		s.setY(coinsY[c]);
	        		s.show();
	        		coins.add(s);
	       }
	        	
	}//lvlinit
	
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
		 if(img!=null ){
	        int width = img.getWidth();
	        int Height = img.getHeight();
	        for(int i=0;i<9;i++){
	        	for(int j=0;j<6;j++){
	        		g.drawImage(img, i*width+xo, j*Height+yo, width, Height, null);
	        	}
	        }
	      }
		//Draws both tile maps 
		
		
        tmap.draw(g,xo,yo); 
        frontMap.draw(g,xo,yo);
        
       
        for (Saw s: saws){
        	s.setOffsets(xo,yo);
        	s.draw(g);
        }    
        
        for (Sprite s: coins){
        	s.setOffsets(xo,yo);
        	s.draw(g);
        }        
        
        if(end!=null){
        	end.setOffsets(xo, yo);
        	end.draw(g);
        }
		
    	if(tresure!=null){
        	tresure.setOffsets(xo, yo);
        	tresure.draw(g);
        }	
    	
	}// draw
	
	
	/**
	 * 	Method that returns list of saws in this level
	 * 
	 * @return  A reference to a list of saws 
	 */
	public ArrayList<Saw> getSaw(){
		return saws;
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
	 * 	Method that removes coin at the given position in list 
	 * 
	 * @param given int position in list of coins that is suppose to be deleted 
	 */
	public void removeCoin(int given){
		coins.remove(given);
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
	
	
	
	
	
	
}//Level1
