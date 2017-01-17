/*
    A very simple application that reads in a single image
    from a file and repeatedly slides it across the window.

    Customise the global declarations of title, imageLocation/Width/Height and
    speed to suit.

    SBJ November 2008  -- modified EHM Nov'16
    2224717
*/

import java.awt.*;
import javax.swing.*;
import java.net.*;     // For the URL class

public class DrawImage extends JFrame {

    // Choose an image name .... or add your own ....
    // private final String title = "Sliding arrow" ;
    private final String title = "Sliding horse" ;  
        
    // Choose an image ... or add your own.
    // Will need permission in the java.policy file; currently using permission java.security.AllPermission "", "";
    //private final String imageLocation = "http://www.cs.stir.ac.uk/~sbj/images/rightarrow.gif";
    private final String imageLocation = "http://www.supershareware.com/images/icons/TSPhotoFinish___Horse_Racing-15361.jpg";

    private final int imageWidth = 50, imageHeight = 60;  // Force it to display this size

    private final int speed = 5;    // Milliseconds per pixel across the screen, roughly

    private final int panelWidth = 1000, panelHeight = imageHeight+10;  // Pixels

    private Image im;               // To hold the fetched image
    private JPanel panel;           // For drawing on

    // Constructor: creates GUI and starts the image fetch, ready for display later
    public DrawImage(String new_id) {
        System.out.println ("DrawImage constructor started") ;
        
        setTitle(title + "  -  " + new_id);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Container window = getContentPane();
        window.setLayout(new FlowLayout() );

        panel = new JPanel();
        panel.setPreferredSize(new Dimension(panelWidth, panelHeight));
        panel.setBackground(Color.white);
        window.add(panel);

        // Fetch image from a remote file accessible via URL:
        try {
            im = getToolkit().getImage(new URL(imageLocation));
        }
        catch (MalformedURLException e) { /* Do nothing */ }
        
        System.out.println ("DrawImage constructor finished") ;
    } // end of DrawImage constructor

    // And this actually draws the image
    // No double-buffering, so it flickers, but never mind
    public void startDrawing() {

        Graphics g = panel.getGraphics();

        // Slide the image from left to right
        // Forces the image to be imageWidth pixels wide, and imageHeight high
        for (int i = 0; i<panelWidth-imageWidth; i++) {
            g.drawImage(im, i, 5, imageWidth, imageHeight, this);
            pause(speed);
            g.clearRect(0, 0, panelWidth, panelHeight);
        }  // end of for loop

    } // end of startDrawing method

    // Pause n milliseconds
    private void pause(int n) {

        try {
            Thread.sleep(n);
        }
        catch (Exception e) { /* Do nothing */ }

    } // end of pause method
} // end of class DrawImage
