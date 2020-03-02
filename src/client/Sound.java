package client;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Class that play sounds
 * @Author André Möller, Ruben Midhall
 */
public class Sound {
    private static boolean sound = true;
    private static Clip musicClip;


    /**
     * Play sound file from path
     * @param path The soundfile
     */
    public static void playSound(String path)  {
        if(sound) {
            try {
                File f = new File(path);
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
            } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * toggles sound effects
     */
    public static void toggleSound(){
        if(sound){
            sound = false;
        }else{
            sound = true;
        }
    }

    /**
     * toggles music
     */
    public static void toggleSoundMusic(){
        if(musicClip.isRunning()) {
            musicClip.stop();
        }else{
            backrgroundMusic();
        }

    }

    public static void shot()  {
        playSound("Sounds/GunFire.wav");
    }

    public static void move()  {
        playSound("Sounds/Move.wav");
    }

    public static void win()  {
        playSound("Sounds/Winning.wav");
    }


    /**
     * plays music
     */
    public static void backrgroundMusic(){
        try {
            File f = new File("Sounds/music.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
            musicClip = AudioSystem.getClip();
            musicClip.open(audioIn);
            musicClip.loop(10000);
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

}
