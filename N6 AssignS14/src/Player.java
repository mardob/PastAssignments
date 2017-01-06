import game2D.*;


/**
 * @author 2224717 
 * 
 * 	Class Player that is extended version of Sprite
 */
public class Player extends Sprite{
	
	//Flags
	private boolean moveRight =false;
	private boolean moveLeft =false;
	private boolean lookRight=false; 
	private boolean jumped = false;
	
	//Player resources
	private long tStartJump;
	Animation walkRight;
	Animation walkLeft;
	Animation jumpRight;
	Animation jumpLeft;
	
    
	/**
	 * Constructor of Player
	 */
	public Player(){
		 super(null);
		 
		 walkRight = new Animation();
		 walkRight.loadAnimationFromSheet("images/player.png", 3 , 1, 370);
		 
		 walkLeft = new Animation();
		 walkLeft.loadAnimationFromSheet("images/playerB.png", 3 , 1, 370);
		 
		 jumpRight = new Animation();
		 jumpRight.loadAnimationFromSheet("images/jump.png", 1 , 1, 370);
		 
		 jumpLeft = new Animation();
		 jumpLeft.loadAnimationFromSheet("images/jumpB.png", 1 , 1, 370);
		 
		 this.setAnimation(walkRight);
		 
	}//player

	/**
	 * Method that teleports player to sprite
	 *
	 * @param Sprite player teleports to this sprite  
	 */
    public void blink(Sprite dagger){
    	if(dagger != null){
    		this.setX(dagger.getX());
    		this.setY(dagger.getY());
    	}
    }// blink
	
    
	/**
	 * Method that takes care of player jumping
	 */
    public void jump(){
    	if( !jumped ){
    		setVelocityY(-0.18f);
    		setJumped(true);
    		if(moveRight)
    		setAnimation(jumpRight);
    		else{ setAnimation(jumpLeft); }
    		setStartJump(0);
    	}
    }// jump
	
	
	/**
	 * Method that changes player jump state
	 *
	 * @param boolean value is set to internal flag jumped
	 */
	public void setJumped(boolean jumped){
		this.jumped = jumped;
		if(!jumped){
			if(lookRight)
				setAnimation(walkRight);
			else{setAnimation(walkLeft);}
		}
	}//setJumped
	
	
	/**
	 *	Set method for timestamp when player jumped
	 *
	 * @param long value is set to internal value startJump
	 */
	public void setStartJump(long startJump){
		this.tStartJump = startJump;
	}//setstartJump
	
	
	/**
	 * Set method for look right flag
	 *
	 * @param boolean value is set to internal flag lookRight
	 */
	public void setLookRight(boolean lookRight){
		this.lookRight = lookRight;
	}//setLookRight
	
	
	/**
	 *	Set method for flag move right
	 *
	 * @param boolean value is set to internal flag moveRight
	 */
	public void setMoveRight(boolean moveRight){
		if(moveRight){
			this.moveRight = moveRight;
			this.moveLeft = false;
			if(moveRight && !jumped)
				this.setAnimation(walkRight);
		}else{
			this.moveRight = moveRight;
		}
	}//setMoveRight
	
	
	/**
	 *	Set method for flag move left
	 *
	 * @param boolean value is set to internal flag moveLeft
	 */
	public void setMoveLeft(boolean moveLeft){
		if(moveLeft){
			this.moveLeft = moveLeft;
			this.moveRight = false;
			if(moveLeft && !jumped)this.setAnimation(walkLeft);
		}else{
			this.moveLeft = moveLeft;
		}
	}//setMoveLeft

		
	/**
	 * 
	 * @return boolean returns value of jumped
	 */
	public boolean getJumped(){
		return jumped;
	}
	
	
	/**
	 * 
	 * @return long returns value of startjump
	 */
	public long getStartJump(){
		return tStartJump;
	}
	
	
	/**
	 * 
	 * @return boolean returns value of lookRight
	 */
	public boolean getLookRight(){
		return lookRight;
	}
	
	
	/**
	 * 
	 * @return boolean returns value of moveRight
	 */
	public boolean getMoveRight(){
		return moveRight;
	}
	
	
	/**
	 * 
	 * @return boolean returns value of moveLeft
	 */
	public boolean getMoveLeft(){
		return moveLeft;
	}
		
}//Player
