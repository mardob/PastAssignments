/**
 * ringManager.java
 * This allows a user to inject a token into an existing ring.
 * 2224717
 * @version Nov'16
 */

import java.rmi.*;
import java.net.*;
import java.io.*;
 
public class ringManager
{  
	// class variable fileName contains name of the file that becomes synchronized
	private String fileName;
	
	/***
	 * 
	 * @param ring_node_host	
	 * @param ring_node_id
	 * @param fileName
	 * @param maxSteps
	 * @param doubleTimeID
	 * @param missTurnID
	 * @param seppuku
	 */
   public ringManager(	String ring_node_host, String ring_node_id,String fileName, 
		   				int maxSteps, String doubleTimeID, String missTurnID, boolean seppuku){
	   
	// remote ring node object
	ringMember remoteRef_to_ringMember;
	//sets global filename to one form parameters
	this.fileName=fileName;
	
	System.setSecurityManager(new SecurityManager());

	try { InetAddress host_addr = InetAddress.getLocalHost() ; // where is the manager running?
	      String host_name = host_addr.getHostName() ;         // ... extract this host as a String
	      System.out.println ("Ring manager host is "+host_name) ;
	      System.out.println ("Ring element host is "+ring_node_host) ;
	      
	      //check if this is kill token, if it is don't wipe the file
	      if(!seppuku){
	    	  // clear out file used by ring nodes	
	    	  System.out.println("Clearing "+fileName+" file");
	    	  try { // delete file contents
	    		  // create fileWriter - false means new file and so contents lost/deleted
	    		  FileWriter file_writer_id = new  FileWriter(fileName, false) ;
	    		  // that's it - all now cleaned up
	    		  file_writer_id.close() ;
		      } // end try
	    	  catch (java.io.IOException e) {System.err.println("Exception in startManager clearing file : main: " +e) ;}
	      }//end 
          
	      // get remote reference to ring element/node
          remoteRef_to_ringMember = (ringMember)Naming.lookup("rmi://"+ring_node_host+"/" +ring_node_id);
          
          // create token and pass it
        	  Token token = new Token(0, maxSteps, ring_node_id, doubleTimeID, missTurnID, seppuku) ;   // initial count is zero.
        	  remoteRef_to_ringMember.takeToken(token, fileName);
	    }  // end try
	catch (Exception e) {
		System.err.println(e);
		System.err.println("Error in catch: constructor : ringManager.java") ;
	    }  // end catch
   }  // end of constructor


   /***
    * Main method that makes new manager accepts either 6 arguments or 2  depending on type of token you want to create. 
    * 6 arguments creates traditional token that goes around circle of nodes and allows them execution. 2 arguments will create
    * cleanup token. This token has no effect other than cleaning up nodes. 
    * 
    * @param argv
    */
   public static void main(String argv[]) {
	   
		//flag that is true if token is cleanup token
	   boolean order66 = false;
		//file to be used as shared resource
	   String fileName =	"";
	   // amount of times token should circle around
	   int maxSteps =		2;
	   // id of a node to run twice 
	   String doubleTimeID=	"";
	   // id of a node to miss it's turn every other time
	   String missTurnID=	"";
	   
	   //check if amount of arguments is same as for normal command
	  if ((argv.length < 6) || (argv.length > 6))
		  { 
		  	//check if amount of arguments is same as for kill command message
	   		if(argv.length==2){
	   			order66=			true;
	   		//kill the process if amount of arguments is not same as in either of the commands
	   		}else{
	   			System.out.println("Usage: [ring element host] [ring element id] filename.txt");
	   			System.exit (1) ;
	   		}
		  }
		  
   		// Data read from cmd arguments
		String ring_node_host	= argv[0];
		String ring_node_id   	= argv[1];

		if(!order66){
			 fileName		= argv[2];			
			 maxSteps		= Integer.parseInt(argv[3]);			
			 doubleTimeID	= argv[4];			
			 missTurnID		= argv[5];
		}
		
      // now start ringManager
      ringManager client = new ringManager(ring_node_host, ring_node_id, fileName, maxSteps, doubleTimeID, missTurnID, order66  );
      
      
   }   // end of main() method 
}  // end of class

