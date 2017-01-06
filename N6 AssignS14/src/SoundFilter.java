import java.io.*;

import javax.sound.sampled.AudioInputStream;


public class SoundFilter extends FilterInputStream {

	
	String filename;	// The name of the file to play
	AudioInputStream stream;
	float volume = 0.01f;
	float maxVolume = 30.5f;
	
	public SoundFilter(InputStream stream) {
		super(stream);
	}
	
	
	
	// Get a value from the array 'buffer' at the given 'position'
	// and convert it into short big-endian format
	public short getSample(byte[] buffer, int position)
	{
		return (short) (((buffer[position+1] & 0xff) << 8) |
					     (buffer[position] & 0xff));
	}

	// Set a short value 'sample' in the array 'buffer' at the
	// given 'position' in little-endian format
	public void setSample(byte[] buffer, int position, short sample)
	{
		buffer[position] = (byte)(sample & 0xFF);
		buffer[position+1] = (byte)((sample >> 8) & 0xFF);
	}
	
	
	public int read(byte [] sample, int offset, int length) throws IOException
	{
		
		// Get the number of bytes in the data stream
		int bytesRead = super.read(sample,offset,length);
		// Work out a rate of change in volume per sample
		// (multiplied by 2 because we are move at 2 bytes per loop cycle)
		float change = 2.0f * (1.5f / (float)bytesRead);

		short amp=0;

		//	Loop through the sample 2 bytes at a time
		for (int p=0; p<bytesRead; p = p + 2)
		{
			// Read the current amplitutude (volume)
			amp = getSample(sample,p);
			// Reduce it by the relevant volume factor
			amp = (short)((float)amp * volume);
			// Set the new amplitude value
			setSample(sample,p,amp);
			volume = volume + change;
			
			//Check for max volume as I slowly raise volume and don't want
			//data overflow or too noisy sound
			if(volume >= maxVolume){volume = volume-change;}
		}
		return length;

	}
	
	
}
