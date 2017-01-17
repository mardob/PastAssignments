import java.util.Date;
/**
 * token to be passed between members of the ring.
 * 
 * @author Evan Magill 
 * @version (Nov'16)
 */
public class Token implements java.io.Serializable
{
    // class variable that stores the number of node visits as the token circulates.
    private int count;
    // maximum amount of times token should circle around network
    private int maxCircles;
    // node at which we should start our circle
    private String startingNodeID;
    //counter for amount of time token did full circle around network
    private int circles = 0;
    private String moreTimeNodeID;
    // ID of node to skip it's turn every other time
    private String missTurnID;
    // ID of this token
    private Date thisTokenID;
    //flag that when true represents order to kills all the nodes
    private boolean order66;


    /**
     * Constructor for objects of class token
     */
    public Token(int initial_count, int maxSteps, String startingNodeID, String moreTimeNodeID, String missTurnID, boolean order66)
    {
        // initialise class variables
        count = initial_count;
        this.maxCircles = maxSteps;
        this.moreTimeNodeID = moreTimeNodeID;
        this.startingNodeID = startingNodeID;
        this.missTurnID = missTurnID;
        this.order66 = order66;
        //initialise token's id as current timestamp
        thisTokenID = new Date();
    } // end constructor
    

    /**
     * METHOD: incrCount
     * 
     * @param      none.
     * @return     the current node visit count for the token
     */
    public int incrCount()
    {
        // simply increment the class variable, count (number of node visits)
        count++ ;
        return count ;
    }  // End of method: incrCount()
    
    
    
    /***
     * Method that checks if maximum amount of circles has been achieved and raises counter of amount of circles
     * @param id	id of the node calling this method if it is starting node increments counter of circles 
     * @return
     */
    public boolean checkMaxCircles(String id){
    	//check if called by starting node
    	if(startingNodeID.equals(id))
    		//if yes increment circles counter
    		circles++;
    	//check if maximum amount of circles have been achieved if yes return true else return false
    	if(circles > maxCircles)
    	return true;
    	else return false;
    }// end of checkMaxCircles
    
    
    /***
     * Method that checks if this is last turn or not
     * @return	true if last turn, false if not
     */
    public boolean checkLastTurn(){
    	if(circles == maxCircles)
    	return true;
    	else return false;
    }
    
    
    /***
     * Check if process asking should skip his turn or not
     * @param id	id of the process
     * @return	true if should skip, false if it shouldn't 
     */
    public boolean shouldSkip(String id){
    	// check if process calling is the one which should skip turn and if it is even turn
    	if(missTurnID.equals(id) && circles%2==0 )
    	return true;
    	else return false;
    }
    
    
    // Get method for order66
    public boolean order66(){
    	return order66; 
    }
    
    
    // Get method for tokenID
    public Date getTokenID(){
    	return thisTokenID;
    }
    
    
    // Get method for moreTimeNodeID
    public String getMoreTimeNodeID(){
    	return moreTimeNodeID;
    }
    
    
 // Get method for getMissTurnID
    public String getMissTurnID(){
    	return missTurnID;
    }
    
    
}  // end of class Token
