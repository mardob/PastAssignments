import game2D.Sprite;
import game2D.Animation;


/**
 * @author 2224717 
 * 
 * slightly modified Sprite class from game2D designed to move in a line only and not freely 
 */
public class Saw extends Sprite{
	
	private float Xvelocity = 0;
	private float Yvelocity = 0;
	
	
	
	/**
	 * 	Constructor of saw
	 * 
	 * @param Animation object that specifies animation
	 */
	public Saw(Animation anim){
		super(anim);
	}
	
	/**
	 * Method that makes saw move on the X coordinate with given velocity
	 * 
	 * @param float value is used as velocity of this Saw
	 */
	public void moveOnX(float velocity){
		super.setVelocityX(velocity);
		Xvelocity = velocity;
		
	}
	
	/**
	 * Method that makes saw move on the Y coordinate with given velocity
	 * 
	 * @param float value is used as velocity of this Saw
	 */
	public void moveOnY(float velocity){
		super.setVelocityY(velocity);
		Yvelocity = velocity;
		
	}
	
	/**
	 * Method that returns velocity in X direction
	 * 
	 * @return float value is used as velocity of this Saw
	 */
	public float getXvelocity(){
		return Xvelocity;
	}
	
	/**
	 * Method that returns velocity in Y direction
	 * 
	 * @return float value is used as velocity of this Saw
	 */
	public float getYvelocity(){
		return Yvelocity;
	}
	
}// Saw
