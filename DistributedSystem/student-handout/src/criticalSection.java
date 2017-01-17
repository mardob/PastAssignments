/**
 * criticalSection.java  -- based upon Connection.java in distr lab 1
 * This is the separate thread that services each request.
 *  2224717
 */

import java.io.*;
import java.rmi.*;
import java.util.*;

public class criticalSection extends Thread
{
	
    // class variables
    private String  this_id;		// this node network info
    private String  this_host;
    private String  next_id;		// next node network info
    private String  next_host;
    private Token   ring_token;		// reference to token
    private DrawImage app; 			// reference to drawing object
    String fileName="";
	
	
    public criticalSection(String t_host, String t_id, String n_host, String n_id, 
                           Token token, DrawImage drawing_app, String fileName) {
        
    	//setting up global variable values
    	//data for this node
        this_host = 	t_host;
        this_id = 		t_id;
        //data for next node
        next_host = 	n_host;
        next_id = 		n_id;
        //token
        ring_token = 	token ;
        //drawing object for moving picture
        app = 			drawing_app ;
        //name of the file
        this.fileName = fileName;

    } // end of constructor 

    public void run() {
    	
        // check if max cycles have been used if no continue with execution
    	if(!ring_token.checkMaxCircles(this_id)){
            System.out.println("Token received: entering critical region");
            System.out.println("Token id is: "+ring_token.getTokenID());
            
            // check if it should skip this time or not
            if( !ring_token.shouldSkip(this_id) && !ring_token.order66()){
            	//try to open the file and write to it
            	try { System.out.println("Writing to file: "+fileName );
            		Date timestmp = new Date() ;
            		String timestamp = timestmp.toString() ;
            		int i;
            		//check if it should write 2 times or only once
            		if(this_id.equals(ring_token.getMoreTimeNodeID()) ){
            			i=0;
            		}else {i=1;}
            		// loop that prints once or twice
            		for(;i<2;i++){
            			// Next create fileWriter - true means writer *appends* - v.important!
            			FileWriter file_writer_id = new  FileWriter(fileName, true) ;
            			// Next create PrintWriter - true means it flushes buffer on each println
            			PrintWriter print_writer_id = new PrintWriter (file_writer_id, true) ;
            			// println means adds a newline (as distinct from print)
            			print_writer_id.println ("Record from ring node on host " +this_host+ ", id " +this_id+ ", is " +timestamp) ; 
            			print_writer_id.close() ;
            			file_writer_id.close() ; 
            		}// end for loop 
            	}// end try
            	catch(java.io.IOException e) 
            		{System.out.println("Error writing to file: "+fileName+" in takeToken method:- "+e);}
                
            	// check if this node should go twice do drawing twice otherwise draw it only once
            	if( this_id.equals(ring_token.getMoreTimeNodeID()) ){
            		//draw twice
            		app.startDrawing();
            		app.startDrawing();
            	} else {
            		//draw once
            		app.startDrawing();
            	}
              
            }// end of skip area
            
            try // get remote reference to next ring element, increment token count, and pass token on to next node
             { System.out.println("Look up RMIregistry with: rmi://"+next_host+"/"+next_id) ;
               ringMember remoteRef_to_ringMember = (ringMember)Naming.lookup("rmi://"+next_host+"/"+next_id);
               
               //increment count inside token
               int token_count = ring_token.incrCount();  
               System.out.println("Received token count value is: " + token_count );
               
               //pass the token to the next node
               remoteRef_to_ringMember.takeToken(ring_token, fileName); }  // end try
            
            catch (java.lang.Exception e) 
             { System.err.println("RMIregistry lookup failed: "+    e); }
 
            
           System.out.println("Token released: exiting critical region");
           System.out.println("----------------------------------------");
           System.out.println("");
           
           return;   
    	}// end of check for max steps

    } // end of run() method
    
} // end of class

