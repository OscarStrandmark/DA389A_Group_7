package client;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Class that play sounds
 * @Author André Möller, Ruben Midhall, Pontus Laos
 */
public class Sound {
    private static boolean sound = true;
    private static boolean isBackgroundMusicPlaying = false;
    private static Clip musicClip;
    private static FloatControl volumeControl;

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
    public static boolean toggleSoundMusic(){
        if (isBackgroundMusicPlaying) {
            musicClip.stop();
        }
        else {
            musicClip.start();
        }

        isBackgroundMusicPlaying ^= true;
        return isBackgroundMusicPlaying;
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
    public static void backgroundMusic(){
        try {
            File f = new File("Sounds/music.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
            musicClip = AudioSystem.getClip();
            musicClip.open(audioIn);

            volumeControl = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
            setVolume((volumeControl.getMinimum() + volumeControl.getMaximum()) * .5f);

            musicClip.loop(10000);
            isBackgroundMusicPlaying = true;
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the volume of the background music.
     *
     * The range is [-80f..6.0206f].
     * @param value The value to set the volume equal to.
     */
    public static void setVolume(float value) {
        volumeControl.setValue(value);
    }

    /**
     * Gets the volume of the background music.
     *
     * The range is [-80f..6-0206f].
     * @return The volume of the background music as a float.
     */
    public static float getVolume() {
        return volumeControl.getValue();
    }
}
