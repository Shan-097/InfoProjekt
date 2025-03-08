package game;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;

public class Music {
    private static Clip clip;
    private static float Volume = 6.0f;
    private static FloatControl fc;

    public static void LoopMusic(String location) {
        try {
            File musicPath = new File(location);

            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
                fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                fc.setValue(Volume);

                clip.loop(3);

            } else {
                System.out.println("Can't find file");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public static void SpawnMusic(String location) {
       try {
        File musicPath = new File(location);
        if(musicPath.exists()) {
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
            clip = AudioSystem.getClip();
            
            clip.open(audioInput);
            clip.start();
        }
    }catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void stopMusic() {
        clip.stop();
    }

    public static void setVolumeHigher() {
        Volume = Volume + 1.0f;
        if (Volume > 6.0f) {
            Volume = 6.0f;
        }
        fc.setValue(Volume);
    }

    public static void setVolumeLower() {
        Volume = Volume - 1.0f;
        if (Volume < -80.0f) {
            Volume = -80.0f;
        }
        fc.setValue(Volume);
    }

}
