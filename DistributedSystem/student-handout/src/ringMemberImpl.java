/**
 * ringMemberImpl.java
 * Implements ringMember interface
 * 2224717
 */

import java.rmi.*;
import java.net.* ;
import java.util.Date;

public class ringMemberImpl extends java.rmi.server.UnicastRemoteObject implements ringMember 
{
	
 	// class variables: hosts and ids of this object, and the next object in the ring
	//next node information
 	private String	next_id;
	private String  next_host;
	//this node information
	private String	this_id;
	private String  this_host;
	//critical section of this node
	private criticalSection	c;
	private DrawImage drawing_app;
	//expected tokens ID
	private Date tokenID=null;
	//flag that signalizes that node is ready to be commit seppuku
	private boolean seppuku=false;
	
	
   public ringMemberImpl(String  t_node, 
                         String  t_id, 
                         String  n_node, 
                         String  n_id,
                         DrawImage app) throws RemoteException {
      this_host = t_node ;
      this_id = t_id ;
      next_host = n_node ;
      next_id = n_id ;
      drawing_app = app;
   }   // end of constructor
   
   
   // This method allows one ring node object to pass the circulating token on to another.
   // i.e. allows one object of this class to pass a token object on to another object of this class.
   public synchronized void takeToken(Token ring_token, String fileName) throws RemoteException {
      System.out.println ("Entered method: takeToken ringMemberImpl") ;
      //check if no token is assigned to this node
      if(tokenID == null){
    	  tokenID = ring_token.getTokenID();
      }  
      
      // check if this is token that node expects
      if(tokenID.compareTo(ring_token.getTokenID()) == 0){
    	  // enter critical region and pass token on - use a thread to stop deadlock
    	  c = new criticalSection(this_host, this_id, next_host, next_id, ring_token, drawing_app, fileName);
    	  c.start();
    	  
    	  // check if this is last turn of the token if yes make node ready to any new tokens
    	  if(ring_token.checkLastTurn()){
    		  System.out.println("Last turn");
    		  tokenID = null;
    	  }
    	  
    	  // check if node should kill itself 
    	  if(seppuku == true){
    		  // wait for thread to finish sending token to other node
    		  while(!c.isAlive()){
    			  //if thread still runs wait
    			  try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					System.out.println("Interupted in sleep");
				}
    		  }// end of while
    		  
    		  // node kills itself
    		  System.exit(0);
    		  
    	  }// end of check for node kill
    	  
    	  // if token is kill token node prepares to die next time she gets the token
    	  if(ring_token.order66()){
    		  seppuku = true;
    	  }
    		  
    	  System.out.println ("Exiting method: takeToken: ringMemberImpl") ;
      } else {
    	  System.out.println("Extra token taken out");
      	}
      
   }  // end takeToken
   

      
   // main method
   public static void main(String argv[]) {
    System.setSecurityManager(new SecurityManager());
    
    try {  // first - where is this object? Which host?
        InetAddress host_addr = InetAddress.getLocalHost() ;
        String host_name = host_addr.getHostName() ;
        System.out.println ("ring member hostname is " +host_name ) ;
        
        // allow user to enter 3 non-optional parameters values
        if ((argv.length < 3) || (argv.length > 3))
          { System.out.println("Usage: [this node's id] [next host] [next node id]") ;
            System.out.println(" ... a node id is simply a unique identifying string");
            System.out.println("Only "+argv.length+" parameters entered") ;
            System.exit (1) ;
          }
          
        String this_id =   argv[0] ;
        String next_host = argv[1] ;
        String next_id =   argv[2] ;
        
        // image stuff - set up the image *ready* to run.
        DrawImage app = new DrawImage("Image for hostname: " + this_id);
        app.pack();
        app.setLocation(10, 20);
        app.setVisible(true);
        
        ringMember ring_node = new ringMemberImpl(host_name, this_id, next_host, next_id, app);
        System.out.println ("ring member: " +host_name+ " binding to RMI registry") ;
        Naming.rebind("//"+host_name+ "/" +this_id, ring_node);
        System.out.println("Ring element " +host_name+"/" +this_id+ " is bound with RMIregistry");
    }  // end of try
    catch(Exception e) {
        System.err.println(e);
        System.err.println("error in catch: main: MessageQueueImpl") ;
    }  // end of catch
   }  // end of main() method
 }  // end of class
