package main;

import gamestates.GameOver;
import gamestates.MainMenu;
import gamestates.PauseScreen;
import gamestates.PlayState;
import inputs.KeyHandler;
import inputs.MouseHandler;
import ui.Sound;
import util.GameConfiguration;

import java.awt.*;

// class responsible for game loop and thread

public class GameLoop implements Runnable {

    private Thread gameThread;
    private final GamePanel gamePanel;

    public enum gameStateType {PLAY, PAUSE, MAIN_MENU, GAME_OVER}
    private static gameStateType gameState;

    //private final UserInterface UI;
    private PlayState playState;
    private final PauseScreen pauseScreen;
    private final MainMenu mainMenu;
    private final GameOver gameOver;
    public static Sound sound;

    public static final int FPS = 60;
    public static final int UPS = 60;
public GameConfiguration gameConfiguration;

    public GameLoop(GamePanel gamePanel) {  // gamePanel on which game will be displayed

        this.gamePanel = gamePanel;
        sound = new Sound();
        playState = new PlayState();
        pauseScreen = new PauseScreen();
        mainMenu = new MainMenu(this);
        gameOver = new GameOver(this);
        gameConfiguration = new GameConfiguration(playState);

        setGameState(gameStateType.MAIN_MENU);

        KeyHandler keyH = new KeyHandler(this);
        MouseHandler mouseH = new MouseHandler(this);
        gamePanel.addKeyListener(keyH);
        gamePanel.addMouseListener(mouseH);
        gamePanel.addMouseMotionListener(mouseH);
        startGameLoop();
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {


        final double refreshTime = 1e9 / FPS;  // refresh time in ns
        final double updateTime = 1e9 / UPS;  // update time in ns
        double deltaFrames = 0;
        double deltaUpdates = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        int frames = 0, updates = 0;
        long lastDebugTime = 0;

        while (gameThread != null) {

            currentTime = System.nanoTime();
            deltaFrames += currentTime - lastTime;
            deltaUpdates += currentTime - lastTime;
            lastTime = currentTime;

            if (deltaUpdates >= updateTime) {
                update();
                deltaUpdates -= updateTime;
                updates++;
            }

            if (deltaFrames >= refreshTime) {
                gamePanel.repaint();
                deltaFrames -= refreshTime;
                frames++;
            }

            // debug
            if (System.currentTimeMillis() - lastDebugTime >= 1000){
                lastDebugTime = System.currentTimeMillis();
                System.out.println("FPS: "+frames+"    UPS: "+updates);
                frames = 0;
                updates = 0;
            }
        }
    }

    private void update() {

        switch (gameState) {
            case PLAY -> {
                playState.update();
            }
            case PAUSE -> {
                pauseScreen.update();
            }
            case MAIN_MENU -> {
                mainMenu.update();
            }
            case GAME_OVER -> {
                gameOver.update();
            }
        }
    }

    public void paint(Graphics g) {

        Graphics2D g2D = (Graphics2D) g;

        switch (gameState) {
            case PLAY -> {
                playState.draw(g2D);
            }
            case PAUSE -> {
                playState.draw(g2D);
                pauseScreen.draw(g2D);
            }
            case MAIN_MENU -> {
                mainMenu.draw(g2D);
            }
            case GAME_OVER -> {
                gameOver.draw(g2D);
            }
        }

        g2D.dispose();
    }

    public PlayState getPlayState(){
        return playState;
    }

    public PauseScreen getPauseScreen(){
        return pauseScreen;
    }

    public static gameStateType getGameState() {
        return gameState;
    }

    public static void setGameState(gameStateType gameState) {
        gameStateType previousGameState = GameLoop.gameState;
        GameLoop.gameState = gameState;
        setMusic(previousGameState);
    }

    public MainMenu getMainMenu() {
        return mainMenu;
    }

    public Sound getSound() {
        return sound;
    }

    static public void playMusic(int i){
        sound.playMusic(i);
    }

    static public void playSE(int i){
        sound.playSE(i);
    }


    private static void setMusic(gameStateType previousGameState) {
        switch (gameState) {
            case PLAY -> {
                switch (previousGameState){
                    case PLAY -> {
                    }
                    case PAUSE -> {
                        sound.resumeMusic();
                    }
                    case MAIN_MENU -> {
                        sound.playMusic(Sound.GAME_MUSIC);
                    }
                }
            }
            case PAUSE -> {
                sound.stopMusic();
            }
            case MAIN_MENU -> {
                sound.playMusic(Sound.MAIN_MENU_MUSIC);
            }
            case GAME_OVER -> {
                sound.playMusic(Sound.GAME_OVER_MUSIC);
            }
        }
    }

    public void setPlayState(PlayState playState) {
        this.playState = playState;
    }

    public GameOver getGameOver() {
        return gameOver;
    }
}

