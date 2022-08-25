package ui;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sound {

    // CONSTANTS //
    // MUSIC
    public static final int MAIN_MENU_MUSIC = 0;
    public static final int GAME_MUSIC = 1;
    public static final int GAME_OVER_MUSIC = 2;
    // SOUND EFFECTS
    public static final int EARNING_SE = 0;
    public static final int SPENDING_SE = 1;
    public static final int MOUSE_MOVED = 2;
    public static final int MOUSE_CLICKED = 3;
    public static final int GAME_OVER = 4;
    public static final int ENTER_SE = 5;
    public static final int ERROR_SE = 6;

    Clip musicClip, seClip;
    URL[] musicURL = new URL[10];
    URL[] SE_URL = new URL[10];
    FloatControl music_fc, SE_fc;
    private float SE_volume = setVolume(0.5f);
    private float musicVolume = setVolume(0.5f);


    public Sound() {
        loadSounds();
    }

    private void loadSounds() {
        // music
        musicURL[MAIN_MENU_MUSIC] = getClass().getResource("/res/sounds/music/main_menu_music.wav");
        musicURL[GAME_MUSIC] = getClass().getResource("/res/sounds/music/game_music.wav");
        musicURL[GAME_OVER_MUSIC] = getClass().getResource("/res/sounds/music/game_over_music.wav");
        // SE
        SE_URL[EARNING_SE] = getClass().getResource("/res/sounds/SE/SE_earning.wav");
        SE_URL[SPENDING_SE] = getClass().getResource("/res/sounds/SE/SE_spending.wav");
        SE_URL[MOUSE_MOVED] = getClass().getResource("/res/sounds/SE/SE_mouse.wav");
        SE_URL[MOUSE_CLICKED] = getClass().getResource("/res/sounds/SE/SE_click.wav");
        SE_URL[GAME_OVER] = getClass().getResource("/res/sounds/SE/game_over_SE.wav");
        SE_URL[ENTER_SE] = getClass().getResource("/res/sounds/SE/enter_SE.wav");
        SE_URL[ERROR_SE] = getClass().getResource("/res/sounds/SE/error_SE.wav");

    }

    private void setMusic(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(musicURL[i]);
            musicClip = AudioSystem.getClip();
            musicClip.open(ais);
            music_fc = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
            music_fc.setValue(musicVolume);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    private void setSE(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(SE_URL[i]);
            seClip = AudioSystem.getClip();
            seClip.open(ais);
            SE_fc = (FloatControl) seClip.getControl(FloatControl.Type.MASTER_GAIN);
            SE_fc.setValue(SE_volume);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    public void playMusic(int i) {
        if (musicClip != null)
            stopMusic();
        setMusic(i);
        musicClip.start();
        musicClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void playSE(int i) {
        setSE(i);
        seClip.start();
    }

    public void stopMusic() {
        musicClip.stop();
    }

    public void resumeMusic() {
        musicClip.start();
        musicClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void setMusicVolume(float volume) {
        musicVolume = setVolume(volume);
        if (music_fc != null)
            music_fc.setValue(musicVolume);

    }

    public void setSEVolume(float volume) {
        SE_volume = setVolume(volume);
    }

    public float setVolume(float volume) {
        // volume in linear scale form 0.0f to 1.0f
        if (volume < 0f || volume > 1f)
            throw new IllegalArgumentException("Volume not valid: " + volume);
        return 20f * (float) Math.log10(volume);
    }
}
