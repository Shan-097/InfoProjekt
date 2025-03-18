package util;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import java.io.File;

/**
 * to be done
 */
public class Music {
    /**
     * to be done
     */
    private static Clip clip;
    /**
     * to be done
     */
    private static float Volume = 6.0f;
    /**
     * to be done
     */
    private static FloatControl fc;

    /**
     * Empty and unused constructor of Music
     */
    public Music(){
    }

    /**
     * to be done
     * 
     * @param location to be done
     */
    public static void LoopMusic(String location) {
        try {
            File musicPath = new File(location);

            AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
            clip = AudioSystem.getClip();
            clip.open(audioInput);
            fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            fc.setValue(Volume);

            clip.loop(3);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * to be done
     * 
     * @param location to be done
     */
    public static void SpawnMusic(String location) {
        try {
            File musicPath = new File(location);
            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                clip = AudioSystem.getClip();

                clip.open(audioInput);
                clip.start();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * to be done
     */
    public static void stopMusic() {
        if (clip == null) {
            return;
        } 
        clip.stop();
    }

    /**
     * to be done
     */
    public static void setVolumeHigher() {
        Volume = Volume + 5.0f;
        if (Volume > 6.0f) {
            Volume = 6.0f;
        }
        fc.setValue(Volume);
    }

    /**
     * to be done
     */
    public static void setVolumeLower() {
        Volume = Volume - 5.0f;
        if (Volume < -80.0f) {
            Volume = -80.0f;
        }
        fc.setValue(Volume);
    }
}
