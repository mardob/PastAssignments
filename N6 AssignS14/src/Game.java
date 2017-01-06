import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import game2D.*;

// Game demonstrates how we can override the GameCore class
// to create our own 'game'. We usually need to implement at
// least 'draw' and 'update' (not including any local event handling)
// to begin the process. You should also add code to the 'init'
// method that will initialise event handlers etc. By default GameCore
// will handle the 'Escape' key to quit the game but you should
// override this with your own event handler.

/**
 * @author 2224717
 *
 */
@SuppressWarnings("serial")

public class Game extends GameCore 
{
	// Useful game constants
	private static final int screenWidth = 900; 	
	private static final int screenHeight = 600;
	private final float gravity = 0.0002f;
	
    // Game state flags
	private boolean gameWon = false;
    private  boolean standing = false;
    
    // Game resources
    //Number of level 1 = 1st level 2 = 2nd level and so on.
	private int levelID = 1;
    private level level;
    private  Animation blinkLeft = new Animation();
    private   Animation blinkRight = new Animation();
    private  music backGround =null;
    private  Sprite dagger =null;
    private String msg="";
    private  Player	player = null;
    // The total is amount of points you got so far in a game
    private   int total;         			
    private   int collectibles = 0;
   
    
    
    /**
	 * The obligatory main method that creates
     * an instance of our class and starts it running
     * 
     * @param args	The list of parameters this program might use (ignored)
     */
    public static void main(String[] args) {
        Game gct = new Game();
        gct.init();
        // Start in windowed mode with the given screen height and width
        gct.run(false,screenWidth,screenHeight);
    }	//main

    
    
    /**
     * Initialise the class, e.g. set up variables, load images,
     * create animations, register event handlers
     */
    public void init()
    {         
    	blinkRight.addFrame(loadImage("images/blinkRight.png"), 1000);
    	blinkLeft.addFrame(loadImage("images/blinkLeft.png"), 1000);
    	
    	player = new Player();
        if(levelID==1)level = new level1(player);
        else if(levelID==2)level = new level2(player);
        
        player.show();
        
        if(levelID!=1){total = 0;}
        
        //Start music
       if(backGround == null){
        		backGround = new music();
        		if(backGround==null){System.out.println("Problem with loading sound.");}
        		else {new Thread (backGround).start();}     	
       } 
    }	//init


    /**
    **Method that restarts world from scratch.
    */
    public void restartGame(){
    	dagger = null;
    	init();
    }// restartGame
    
    /**
     * Method that loads next level 
     */
    public void loadNextLevel(){
    	
    	level = new level2(player);
    	if(levelID == 2){ 
    		msg="Congratulation you finished our demo. Preorder now our DLC pack for more content."; 
    		this.gameWon = true;
    	}
    	levelID = 2;      
    } //loadNextLevel
    
    
    /**
     * Draw the current state of the game
     * 
     * @param Grphics2D graphics object draw draws into
     */
    public void draw(Graphics2D g)
    {    	
    	//Calculation of xo and yo which are x and y offsets 
        int xo = (int)(screenWidth/2 - player.getWidth()/2 - player.getX());  //34 player width
		int yo = (int)(screenHeight/2 - player.getHeight()/2 - player.getY());  //59 player height    
        xo = Math.min(xo,0);
        xo = Math.max(xo, screenWidth - level.getMapWidth());
        yo = Math.min(yo,0);
        yo = Math.max(yo, screenHeight - level.getMapHeight());

        //Main background colour
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        // Draws everything that is part of given level
        level.draw(g, xo, yo, screenWidth, screenHeight);
      
    	//Applyes offset and draws energy ball if it is exists right now
        if(dagger!=null){
        	dagger.setOffsets(xo, yo);
        	dagger.draw(g);
        }

        // Apply offsets to player and draw 
        player.setOffsets(xo, yo);
        player.drawTransformed(g);
          
        //Draws number of points and collectibles player gained during the game    
        String msg = String.format("Score: " + this.total);

        String message = String.format("Collectibles: " + this.collectibles);
        g.setColor(Color.white);
        g.drawString(msg, getWidth() - 80, 50);
        g.drawString(message, -70, 65);
        
        //If game is won display final messages
        if(gameWon){
        	g.drawString(this.msg, 50, 300);
        	String msg2 = "Your final score is " + this.total + "points & " + this.collectibles + " collectibles found" ;
        	g.drawString(msg2, 50, 350);
        	msg2 = "To quit press ESC to restart press R" ;
        	g.drawString(msg2, 50, 400);
        }
    }//draw

   
    
    
    /**
     * Updates all sprites and check for collisions
     * 
     * @param elapsed The elapsed time between this call and the previous call of elapsed
     */    
    public void update(long elapsed)
    {	
    	//check if game is won
    	if(gameWon){
    		return;
    	}
    	
        // Make adjustments to the speed of the sprite due to gravity  
    	if(!standing){
    		player.setVelocityY(player.getVelocityY()+(gravity*elapsed));
    	}	
    	
    		//ends jump
    		if(player.getJumped()){
    			player.setStartJump ( player.getStartJump() + elapsed );
    			if(player.getStartJump() > 2000){
    				player.setJumped(false);
    			}
    		}
    		
    	//update player movement 
       		if (player.getMoveLeft()) 
       		{
       			player.setVelocityX(-0.099f);
       		} else {
       			if (player.getMoveRight()) 
       			{
       				player.setVelocityX(0.099f);
       			} else {
   						player.setVelocityX(0);
       				}
       			}        		
       	//Update rubies (coins)	
       	ArrayList<Sprite> coins = level.getCoins();
    	for(Sprite s: coins)
       		s.update(elapsed);
       	
    	//Update energy ball(dagger)
       	if(dagger!=null){
       		dagger.update(elapsed);
       		handleTileMapCollisions(dagger,elapsed);
       	}

       	//Update saws and check collision 
       	ArrayList<Saw> saws = level.getSaw();
        for (Saw saw: saws){
        	saw.update(elapsed);
       		handleTileMapCollisions(saw,elapsed);
       		if(saw.getVelocityX()==0){ saw.moveOnX(saw.getXvelocity()*-1); }
       		if(saw.getVelocityY()==0){ saw.moveOnY(saw.getYvelocity()*-1); }
       		
       		
       		//Collision check between player and saw that results in restart of level
       		if((boundingBoxCollision(player, saw) == true)){
       			restartGame();
            }	
       	}
        // Check for collision between player and tiles 
        handleTileMapCollisions(player,elapsed);
        
        // Now update the players animation and position
        player.update(elapsed);

        
        Sprite tresure = level.getTresure();
        if(tresure!=null && (boundingBoxCollision(player, tresure) == true)){
        	collectibles++;
        	level.removeCollectible();
        }
        
        //Check all coins or detection and get rid of that one that collides with player
        int detected=-1;
       for (int i =0; i < coins.size(); i++){
        	Sprite s = coins.get(i);

        	if(boundingBoxCollision(player, s) == true){
        		total = total + 10;
        		detected = i;
        	}
        	
        	//check if coin was found if so ends a loop
        	if(detected > -1){break;}
        }
        if(detected > -1){level.removeCoin(detected);}
        
    }//update
    	
		
    
    /**
     * Checks and handles collisions with the tile map for the
     * given sprite 's'.
     * 
     * @param s			The Sprite to check collisions for
     * @param elapsed	How time has gone by
     */
    public void handleTileMapCollisions(Sprite s, long elapsed)
    {
    	//Local variables 
    	//sets up 2 basic int position values
    	int tileX = (int)(s.getX());
    	int tileY = (int)(s.getY());
    	//sets up 2 floats for position calculation
    	float positionX = s.getX();
    	float positionY = s.getY();    	
    	TileMap tmap = level.getMap();
    	int tileXSize = tmap.getTileWidth();
    	int tileYSize = tmap.getTileHeight();
    	final int distance = 2;
    	
    	
    	// Method calculates 4 corner points and 2 middle points of given sprite and finds out on what kind of tiles it represents.
    	
    	if (  (s.equals(player)) &&		((tmap.getTileChar(((tileX-distance) / tileXSize), ((tileY-distance) / tileYSize))=='s') ||
    									(tmap.getTileChar((tileX+s.getWidth()-distance) / tileXSize, (tileY-distance) / tileYSize)=='s')||
    									(tmap.getTileChar((tileX+s.getWidth()-distance) / tileXSize, (tileY+s.getHeight()-distance) / tileYSize)=='s')||
    									(tmap.getTileChar((tileX-distance) / tileXSize, (tileY+s.getHeight()-distance) / tileYSize)=='s')))
    	{	restartGame();	} 
    	

    	
    	//check left middle point
		if(tmap.getTileChar((tileX - distance)/ tileXSize, ((tileY + (s.getHeight() / 2) + distance) / tileYSize) ) == 'p'){
			s.setX(positionX + 0.5f);
			s.setVelocityX(0);
			tileX = (int)(s.getX());
			positionX = s.getX();
		}
		
		//check right middle point
		if(tmap.getTileChar((tileX + s.getWidth() + distance)/ tileXSize, ((tileY + (s.getHeight() / 2)) / tileYSize)) == 'p'){
			s.setX(positionX - 0.5f);
			s.setVelocityX(0);
			tileX = (int)(s.getX());
			positionX = s.getX();
		}
    	
    	
    	// BOTTOM RIGHT point
    	if((	tmap.getTileChar((tileX+s.getWidth()) / tileXSize, (tileY+s.getHeight()) / tileYSize))=='p'
    		|| (tmap.getTileChar((tileX+s.getWidth()) / tileXSize, (tileY+s.getHeight()) / tileYSize))=='t'
    		|| (tmap.getTileChar((tileX+s.getWidth()) / tileXSize, (tileY+s.getHeight()) / tileYSize))=='b'
    	){
    		//check down
    		if(tmap.getTileChar((tileX + s.getWidth())/ tileXSize, ((tileY + distance) / tileYSize)) == 'p'){
        		s.setY(positionY - 0.2f);
        		s.setVelocityY(0);
            	tileY = (int)(s.getY());
            	positionY = s.getY();
        	} 
    		
    		//check right
    		if(tmap.getTileChar((tileX + s.getWidth() + distance)/ tileXSize, ((tileY + s.getHeight()) / tileYSize)) == 'p'){
    				s.setX(positionX - 0.7f);
    				s.setVelocityX(0);
    				tileX = (int)(s.getX());
    				positionX = s.getX();
        		}
    		
    		//check bottom right corner
    		if(tmap.getTileChar((tileX + s.getWidth() + distance)/ tileXSize, ((tileY + s.getHeight()+ distance) / tileYSize)) == 'p'){
    				s.setVelocityX(0);
    				s.setVelocityY(0);
    				s.setX(positionX - 0.5f);
    				s.setY(positionY - 0.5f);
                	tileY = (int)(s.getY());
                	positionY = s.getY();
    				tileX = (int)(s.getX());
    				positionX = s.getX();
        		} 
		}//bottom right point
    	
    	
    	// BOTTOM LEFT	
    	if(	   (tmap.getTileChar(tileX / tileXSize, (tileY+s.getHeight()) / tileYSize))=='p'
    		|| (tmap.getTileChar(tileX / tileXSize, (tileY+s.getHeight()) / tileYSize))=='t'
    		|| (tmap.getTileChar(tileX / tileXSize, (tileY+s.getHeight()) / tileYSize))=='b'
    	){
    		//check bellow
    	
    		if(tmap.getTileChar(tileX/ tileXSize, ((tileY + s.getHeight() + distance) / tileYSize)) == 'p'){
        		s.setY(positionY - 0.4f);
        		s.setVelocityY(0);
            	tileY = (int)(s.getY());
            	positionY = s.getY();
        	}
    		
    		//check left
    		if(tmap.getTileChar((tileX - distance)/ tileXSize, ((tileY + s.getHeight()) / tileYSize)) == 'p'){
        			s.setX(positionX + 0.7f);
        			s.setVelocityX(0);
        			tileX = (int)(s.getX());
        			positionX = s.getX();
        	}
    		

    		//check left bottom corner
    		if(tmap.getTileChar((tileX - distance)/ tileXSize, ((tileY + s.getHeight()+ distance) / tileYSize)) == 'p'){
        			s.setX(positionX + 0.5f);
        			s.setVelocityX(0);
        			tileX = (int)(s.getX());
        			positionX = s.getX();
        			s.setY(positionY - 0.5f);
            		s.setVelocityY(0);
                	tileY = (int)(s.getY());
                	positionY = s.getY();
        		}
		}//bottom left
    	
    	//Turns off gravity if player is standing on some block 
    	if( tmap.getTileChar(tileX/ tileXSize, ((tileY + s.getHeight() + distance+2) / tileYSize)) == 'p' ||
    		tmap.getTileChar((tileX + s.getWidth())/ tileXSize, ((tileY+ s.getHeight() + distance+2) / tileYSize)) == 'p'	)
    	{
    		if (s instanceof Player)standing = true;
    	} else { 
    		if (s instanceof Player)
    		standing = false;}
    	
    	
    	// TOP LEFT Point
    	if(	   tmap.getTileChar((tileX / tileXSize), (tileY / tileYSize))=='p' 
    		|| tmap.getTileChar((tileX / tileXSize), (tileY / tileYSize))=='t' 
    		|| tmap.getTileChar((tileX / tileXSize), (tileY / tileYSize))=='b' 
    	){
    		//check left
    		if(tmap.getTileChar((tileX - distance)/ tileXSize, (tileY / tileYSize)) == 'p'){
    			s.setX(positionX + 0.5f);
    			s.setVelocityX(0);
    			tileX = (int)(s.getX());
    			positionX = s.getX();
    		}
    		
    		//check above
    		if(tmap.getTileChar(tileX/ tileXSize, ((tileY - distance) / tileYSize)) == 'p'){
    			s.setY(positionY + 0.5f);
        		s.setVelocityY(0);
            	tileY = (int)(s.getY());
            	positionY = s.getY();

        	}
    		
    		//check left top corner
    		if(tmap.getTileChar((tileX - distance)/ tileXSize, ((tileY - distance) / tileYSize)) == 'p'){
    			s.setX(positionX + 0.5f);
    			s.setY(positionY + 0.5f);
    			s.setVelocityX(0);
    			s.setVelocityY(0);
    			tileX = (int)(s.getX());
    			positionX = s.getX();
    			tileY = (int)(s.getY());
    			positionY = s.getY();
    		}
		}// top left
    	
    	
    	//TOP RIGHT point
    	if((   tmap.getTileChar((tileX+s.getWidth()) / tileXSize, tileY / tileYSize))=='p'
    		||(tmap.getTileChar((tileX+s.getWidth()) / tileXSize, tileY / tileYSize))=='t'	
    		||(tmap.getTileChar((tileX+s.getWidth()) / tileXSize, tileY / tileYSize))=='b'
    	){
    		//check right
    		if(tmap.getTileChar((tileX + s.getWidth() + distance)/ tileXSize, (tileY / tileYSize)) == 'p'){
    				s.setX(positionX - 0.5f);
    				s.setVelocityX(0);
    				tileX = (int)(s.getX());
    				positionX = s.getX();
        		}
    		// check to above
        		if(tmap.getTileChar((tileX + s.getWidth())/ tileXSize, ((tileY - distance) / tileYSize)) == 'p'){
            		s.setY(positionY + 0.5f);
            		s.setVelocityY(0);
                	tileY = (int)(s.getY());
                	positionY = s.getY();
            	}
        		
        		// check top right corner
        		if(tmap.getTileChar((tileX + s.getWidth()+ distance)/ tileXSize, ((tileY - distance) / tileYSize)) == 'p'){
            		s.setY(positionY + 0.5f);
            		s.setVelocityY(0);
                	tileY = (int)(s.getY());
                	positionY = s.getY();
                	
                	s.setX(positionX - 0.5f);
    				s.setVelocityX(0);
    				tileX = (int)(s.getX());
    				positionX = s.getX();
            	}	
		}//top right point
    	
    } // Draw
    
    /**
     * Creates new energy ball originating at player that he can "jump"(teleport) to 
     */
    public void shoot(){
    	if(dagger == null){
    		if(player.getLookRight()){
        		dagger =  new Sprite(blinkRight);
    			dagger.setX(player.getX() + player.getWidth());
    			dagger.setY(player.getY());
    			dagger.setVelocityX(0.2f);
    		
    		}else {    
    			dagger =  new Sprite(blinkLeft);
    			dagger.setX(player.getX() - dagger.getWidth());
    			dagger.setY(player.getY());
    			dagger.setVelocityX(-0.2f);
    		}
    		dagger.show();
    	}else{

    		dagger=null;
    		shoot();
    	}
    }//shoot
    
    
     
    /**
     * Override of the keyPressed event defined in GameCore to catch our
     * own events
     * 
     *  @param e The event that has been generated
     */
    public void keyPressed(KeyEvent e) 
    { 
    	// Inputed key value
    	int key = e.getKeyCode();
    	
    	//Always possible to turn off game
    	if(KeyEvent.VK_ESCAPE == key){
    		stop();
    	}
    	// if game is not won yet check for keys pressed
    	if(!gameWon){
    		switch(key)
    		{
    			case KeyEvent.VK_LEFT 		:player.setMoveLeft(true); player.setLookRight(false); break;
    			case KeyEvent.VK_RIGHT		:player.setMoveRight(true); player.setLookRight(true); break;
    			case KeyEvent.VK_DOWN		:if(boundingBoxCollision(player, level.getEnd()) == true){loadNextLevel();} break;
    			case KeyEvent.VK_SPACE		:player.blink(dagger);  dagger=null; break;
    			case KeyEvent.VK_SHIFT		:shoot(); break;
    			case KeyEvent.VK_1			:loadNextLevel(); break;
    			case KeyEvent.VK_UP			:player.jump(); break;
    			default : break;
    		}
    	}else {
    		// Restart game if pressed R only if game is won
    		if(KeyEvent.VK_R == key){
    			gameWon = false;
    			levelID =1;
    			init();
    		}
    	} 
    	
    }//keyPressed

    /**
     * Checks what key was released
     * 
     *  @param KeyEvent event corresponding to released button
     */
    public void keyReleased(KeyEvent e) { 

		int key = e.getKeyCode();
		//if game is not won check if user lift the finger of pressed buttons
		if(!gameWon)
			switch (key)
			{
				case KeyEvent.VK_LEFT		: player.setMoveLeft(false);  break;
				case KeyEvent.VK_RIGHT		: player.setMoveRight(false);  break;
				default :  break;
			}
	}// keyReleased
    
    public boolean boundingBoxCollision(Sprite s1, Sprite s2)
    {	
    	// Basic hitbox  
    	if(((s1.getX() + s1.getWidth()) >= s2.getX()) && (s1.getX() <= s2.getX() + s2.getWidth()) && 
    	        ((s1.getY() + s1.getHeight()) >= s2.getY()) && (s1.getY() <= (s2.getY() + s2.getHeight()) ))
    	{
    		//Check for special circular hitbox for saw 
    		if(s2 instanceof Saw || s1 instanceof Saw){
    			float dx,dy,minimum;
    			dx = s1.getX() - s2.getX() ; 
    			dy = s1.getY()  - s2.getY() ; 
    			minimum = s1.getRadius() + s2.getRadius(); 
    			return (((dx * dx) + (dy * dy)) < (minimum * minimum)); 
    		}
    		else{return true;} 
    	}   
    	else{return false;}      
    }// BoundingBoxCollision


	
	
}