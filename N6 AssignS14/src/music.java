import java.io.*;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import java.lang.Runnable;

/**
*	Class that runs musical clip
*/
public class music  implements Runnable    {
	
	AudioInputStream audio;
	SoundFilter filter;
	
	
	/**
	 *	Constructor for music that doesn't loop
	 */
	public music(){
	// tries to open file and creates filter stream 
		try{
			File file = new File("sounds/theme1.wav");
			AudioInputStream stream = AudioSystem.getAudioInputStream(file);
			filter = new SoundFilter(stream);
			this.audio = stream;
		}
		catch(Exception e){
			System.out.println("File not found .... most likely");
		}		
	}//music
	
	
	/**
	 * Overriden method run that plays the sound clip
	 */
	public void run(){
		// creates Uaudio clip 
		AudioFormat	format = audio.getFormat();
		AudioInputStream f = new AudioInputStream(filter,format,audio.getFrameLength());
		DataLine.Info info = new DataLine.Info(Clip.class, format);
		try{
			Clip clip = (Clip)AudioSystem.getLine(info);
			clip.open(f);
			// plays audio clip 
			clip.start();
			Thread.sleep(200);
			while (clip.isRunning()) {
				Thread.sleep(100); 
			}
			clip.close();
		} catch (Exception e){}
	}// run
	
}//Music
