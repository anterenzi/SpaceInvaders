/**
 * @author Kavan McEachern
 * SoundPlayer.java
 */

package spaceinvaders;

import java.io.*;
import sun.audio.*;

public class SoundPlayer {

    private String filename;

    public SoundPlayer(String filename) {
        this.filename = filename;
    }

    /**
     * Plays the audio file given by filename
     */
    public void play() {
        try {
                InputStream inStream = new FileInputStream(filename);
                AudioStream aStream = new AudioStream(inStream);
                AudioPlayer.player.start(aStream); 

        } catch (IOException e) {
        }          
    }
}