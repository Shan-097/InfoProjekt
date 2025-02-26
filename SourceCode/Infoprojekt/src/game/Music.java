package game;


import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Music {
    
    public static void LoopMusic(String location) {
        try {
            File musicPath = new File(location);

            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);

                clip.loop(Clip.LOOP_CONTINUOUSLY);

            } else {
                System.out.println("Can't find file");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

