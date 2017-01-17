import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.lang.System;
import java.io.FileNotFoundException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.util.Random;


//Student number 2224717

public class DrawColours extends JFrame {
	
	private static final int lineHeight = 100; 	// height of vertical line for each colour
	
	private int size; 							// number of colours or width of image
	private float[][] colours; 					// a size x 3 matrix representing colours
	private int[] permutation;					// the order in which to display the colours
	
		
	
	
	/** CONSTRUCTOR  for DrawColours **/
	public DrawColours(float[][] colours, int[] permutation) {
        super("Colours"); 						// JFrame constructor with "Colours" as title
        assert colours.length == permutation.length; 
        assert colours[0].length == 3; 			// a colour needs to be defined by 3 values
        this.colours = colours;
        this.size = colours.length;
        this.permutation = permutation;
        setSize(size, lineHeight); 				// set width and height of window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
	
	
	
	
	/**
	 * Draw the colours as vertical lines according to the permutation order.
	 * @param g
	 */
	public void drawLines(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < size; ++i) {
        	g2d.setColor(new Color(colours[permutation[i]][0], colours[permutation[i]][1], colours[permutation[i]][2]));
        	g2d.drawLine(i, 0, i, lineHeight);
        }
    }
	
	
 
    public void paint(Graphics g) {
        super.paint(g);
        drawLines(g);
    }
    
    
    
    
    /**
     * Save the colour permutation to a PNG file
     */    
    public void savePNG() {
    	// Save image
        BufferedImage bImg = new BufferedImage(size, lineHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D cg = bImg.createGraphics();
        drawLines(cg);
        try {
            if (ImageIO.write(bImg, "png", new File("output_image.png")))
            {
                System.out.println("Image Saved");
            }
	    } catch (IOException e) {
	            e.printStackTrace();
	    }
    }

    
    
    
    /**
     * Method that generates random solution of given length
     * @param	length
     * @return	int[] solution
     */
    public int[] randomSollution(int length){
    	Random randomGen = new Random();
    	int generated[] = new int[length];
    	int temp;
    	int i;
    	int randomInt;
    	for (i=0; i < length; i++){
    		generated[i] = i;
    	}
    	
    	for (i=0; i < length; i++){
    		randomInt = randomGen.nextInt(colours.length);
    		temp=generated[i];
    		generated[i]=generated[randomInt];
    		generated[randomInt]=temp;
    	}
    	return generated;
    }// randomSollution end
    
    
    
    
    
    /**	
     * Method that calculates fitness function of given solution 
     * @param 	permutation
     * @return 	double fitnessValue
     */
    public double fitnessFunction(int[] permutation){
    	double distance=0;
    	for(int i=0; i<permutation.length-1; i++){
    		distance = distance + Math.sqrt( Math.pow(colours[permutation[i]][0]-colours[permutation[i+1]][0],2) +
    				Math.pow(colours[permutation[i]][1]-colours[permutation[i+1]][1],2)  +
    				Math.pow(colours[permutation[i]][2]-colours[permutation[i+1]][2],2) );		
    	}
    	return distance;
    }// fitnessFunction end

    
    
    
    
   /**
    * Method that implements random search and returns best solution found
    * @return int[] solution
    */
    public int[] randomSearch(){
    	System.out.println("Starting random search");
    	int[] bestSollution = new int[colours.length];
    	double bestFitness=99999999.0;
    	int[] temp = new int[colours.length];
    	double tempFitness=0.0;
    	
    	// generates random solutions and checks if they are better than best found so far
    	for(int i=0;i<100000;i++){
    		temp = randomSollution(colours.length);
    		tempFitness = fitnessFunction(temp);
    		if(tempFitness < bestFitness){
    			bestSollution = temp;
    			bestFitness = tempFitness;
    		}
    	}
    	 
    	System.out.println("\n Final fitness value is: "+bestFitness);
    	return bestSollution;
    }// randomSearch end
    
    
    
    
    
    /**
     * Method that implements hillCLimbing search
     * @param  startValue
     * @return int[] solution
     */
    public int[] hillClimbing(int[] startValue){
    	int length= startValue.length;
    	double startFitness;
    	startFitness = fitnessFunction(startValue);
    	int[] attempt = new int[length];
    	double attemptFitness = 0.0;
    	Random randomGen = new Random();
    	
    	for(int j=0;j<5000;j++){
    		System.arraycopy(startValue, 0, attempt, 0, length);
    		optMove(attempt,randomGen);
    			attemptFitness = fitnessFunction(attempt);
    			//Check if attempt is better than original
    			if(attemptFitness < startFitness ){
    	    		attempt = hillClimbing(attempt);
    				return attempt;
    	    	}
    	}
    	System.out.println("Fitness: "+startFitness);
    	return startValue;
    }
       
    
    
    
    /**
     * Does single opt move on given solution, requires random generator so it can be reused
     * @param value
     * @param randomGen
     */
    public void optMove(int[] value,Random randomGen){
    	int i; int temp;
    	boolean correct=false;
    	int end=0; int start=1;
    	//generates random number
    	while(!correct){
			end = randomGen.nextInt(colours.length);
    		start = randomGen.nextInt(colours.length);
    		//if end is smaller than start swap
    		if(start>end){
    			temp=start;
    			start=end;
    			end=temp;
    		}//check if generated numbers are not neighbours
    		if(start+1!=end){correct=true;}
		}
    	
    	//move operation itself
    	for(i=0; i<(end-start)/2; i++){
			temp = value[start+i];
			value[start+i] = value[end-i];
			value[end-i] = temp;
		}
    }// optMove end
    
    
    
    
    
    /**
     * Method that implements restart hill climbing and returns best solution found
     * @return int[] solution
     */
	 public int[] restartClimbing(){
		 System.out.println("Starting restart hill climber");
		 int[] best = randomSollution(colours.length);
		 double bestFitness= fitnessFunction(best);
		 int[] temp;
		 double tempFitness;
		 
		 for(int i=0; i<10;i++){
			 temp = hillClimbing(best);
			 tempFitness = fitnessFunction(temp);
			 if(bestFitness  > tempFitness){
				 bestFitness = tempFitness;
				 best = temp;
			 }
		 }
		 
		 System.out.println("Best fitness: " + bestFitness);
		 return best;
	 }// restartCLimbing end
	 
	 
	 
	 
	 
	 /**
	  * Method that implements iterated local search and returns best solution found
	  * @param 	startValue
	  * @return int[] solution
	  */
	public int[] iterLocalSearch(int[] startValue){
		System.out.println("Starting iterated local search");
    	int length= startValue.length;
    	double startFitness=0.0;
    	int[] attempt = new int[length];
    	double attemptFitness = 0.0;
    	Random randomGen = new Random();
    	// first hill-climber
    	startValue=hillClimbing(startValue);
    	startFitness = fitnessFunction(startValue);
    	
    	for(int j=0;j<150000;j++){
    		System.arraycopy(startValue, 0, attempt, 0, length);
    		// 2 opt move
    		optMove(attempt,randomGen);
    		optMove(attempt,randomGen);
    		attemptFitness = fitnessFunction(attempt);
    		//check of best fitness
    		if(attemptFitness < startFitness  ){
    			startValue=hillClimbing(attempt);
    			startFitness = fitnessFunction(startValue);
    		}
    	} 
    
    	System.out.println(startFitness);
    	return startValue;
		
	}// iterLocalSearch end
	 
	 
	
    
    /**
     * Method that sets permutation of a object to given parameter
     * @param permutation
     */
    public void setPermutation(int[] permutation){
    	this.permutation = permutation;
    }// setPermutation end
    
    
    
    
    /***
     * Method for loading specified file containing colours
     * @param filename
     * @param amount
     * @param colours
     */
    public static void loadFile(String filename, int amount, float[][] colours) {
    	
		Scanner scan;
		File file = new File(filename);
		try {
			 scan = new Scanner(file);
			 //loads size of the file
			 int size = scan.nextInt();
			 System.out.println("Size of the file is: "+size+"\n"+"Amount of colours used is first "+ amount);
			 int j;
			 		//reads form the file and fills colours array
		        	for(int i=0;i<amount;i++)
		        		for(j=0;j<3;j++){
		        			colours[i][j]= scan.nextFloat();
		        		}
		     colours[0][0]=colours[1][0];
		     colours[0][1]=colours[1][1];
		     colours[0][2]=colours[1][2];
		     
		// Error cases	
		} catch (FileNotFoundException e) {
			System.out.println("File "+filename+" was not found");
		} catch (NumberFormatException e) {
			System.out.println("Incountered numberFormatExeption while trying to read in file "+filename);
		} 
	}//loadFile end 
    
       
    
    
    
	/** STATIC MAIN **/
    public static void main(String[] args) { 
    	String searchType="";
    	int numberColours=1000;
    	if(args.length > 1){
    		searchType = args[0];
    		numberColours = Integer.parseInt(args[1]);
    	}else{
    		System.out.print("Please use 2 arguments, first name of algorithm second amount of colours to use.");
    		System.exit(1);
    	}
    	
    	float[][] colours = new float[numberColours][3];
    	int[] permutation = new int[numberColours];
    	loadFile("colours.txt", numberColours, colours);
    	DrawColours displayColours = new DrawColours(colours, permutation);    
    	long startTime;
    	long endTime;
    	int[] temp;
    	
    	
    	//Switch statement that decides algorithm used
    	switch (searchType) {
    	//case for random search
        case "randomSearch": 
        	startTime = System.currentTimeMillis();	 
        	permutation = displayColours.randomSearch();
        	endTime = System.currentTimeMillis();
        	System.out.print("Execution took(ms): ");
    		System.out.println(endTime-startTime);
    		displayColours.setPermutation(permutation);
            break;
            
          //case for restarting hill-climber  
        case "restartHillClimber":
        	startTime = System.currentTimeMillis();	 
			permutation = displayColours.restartClimbing();
			endTime = System.currentTimeMillis();
			System.out.print("Execution took(ms): ");
			System.out.println(endTime-startTime);
			displayColours.setPermutation(permutation); 
			break;
			
		//case for iterated local search
        case "iterLocalSearch":
        	temp = displayColours.randomSollution(permutation.length);
			startTime = System.currentTimeMillis();	
			permutation = displayColours.iterLocalSearch(temp);
			endTime = System.currentTimeMillis();
			System.out.print("Execution took(ms): ");
			System.out.println(endTime-startTime);
			displayColours.setPermutation(permutation);	
			break;
			
		// 	cases for singular hill climber and singular random solution
        case "singleHillClimber":
        	temp	= displayColours.randomSollution(permutation.length);
			startTime = System.currentTimeMillis();	 
			permutation = displayColours.hillClimbing(temp);
			endTime = System.currentTimeMillis();
			System.out.print("Execution took(ms): ");
			System.out.println(endTime-startTime);
			displayColours.setPermutation(permutation);	
			break;
		
		//case for random solution	
        case "randomSollution":
        	permutation = displayColours.randomSollution(permutation.length);
			displayColours.setPermutation(permutation);
			break;  
			
		//default case	
        default: 
        	System.out.print("Unknown algorithm, please try again");
        	System.exit(1);
                 break;
    }
    
    	// Display image
    	SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                displayColours.setVisible(true);
            }
        });
        
        // Save image
    	displayColours.savePNG();       
    }
    
    
    
    
}