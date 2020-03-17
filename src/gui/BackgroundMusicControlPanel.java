package gui;

import client.Sound;

import javax.swing.*;
import java.awt.*;

/**
 * This is an extension to a JPanel that gives control over the background music.
 *
 * Contains a slider for changing the volume, and a dynamic button for playing and pausing.
 *
 * @author Pontus Laos
 */
public class BackgroundMusicControlPanel extends JPanel {
    public BackgroundMusicControlPanel() {
        super();

        this.setLayout(new BoxLayout(this, 0));

        JButton btnMute = new JButton("Pause");
        JSlider sldLowerVolume = new JSlider(-40 * 10000000, 60206000,  (int) (Sound.getVolume() * 10000000));

        sldLowerVolume.addChangeListener(event -> {
            float volume = sldLowerVolume.getValue() / 10000000.0f;
            Sound.setVolume(volume);
        });

        btnMute.addActionListener(event ->
            btnMute.setText(Sound.toggleSoundMusic() ? "Pause" : "Play")
        );

        this.add(sldLowerVolume, Component.LEFT_ALIGNMENT);
        this.add(btnMute, Component.RIGHT_ALIGNMENT);
    }
}
