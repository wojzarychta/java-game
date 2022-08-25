package main;

import gamestates.Options;
import gamestates.PauseScreen;
import util.GameConfiguration;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    public static final int defaultTileSize = 16;  // default size of one tile = 16x16 pixels
    public static float scaling = 4;
    public static int tileSize = (int) (defaultTileSize * scaling);
    public static int screenWidth = 1280;  // 1024 px
    public static int screenHeight = 720; // 768 px
    public static Point screenCenter = new Point((GamePanel.screenWidth - GamePanel.tileSize) / 2, (GamePanel.screenHeight - GamePanel.tileSize) / 2); // center of the screen
    GameLoop gameLoop;  // creating object controlling display of the game
    private GameWindow gameWindow;
    private boolean fullScreenOn;


    public GamePanel(GameWindow gameWindow) {
        fullScreenOn = GameConfiguration.isFullScreenOn();

        this.gameWindow = gameWindow;
        if (fullScreenOn)
            setFullScreen();
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));

        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);


        gameLoop = new GameLoop(this);
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        gameLoop.paint(g);
        g.dispose();
    }

    public void setFullScreen() {
        gameWindow.setUndecorated(true);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        //gd.setFullScreenWindow(gameWindow);
        gameWindow.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        Dimension size
                = Toolkit.getDefaultToolkit().getScreenSize();


        scaling = 4 * (float) size.width / screenWidth;
        tileSize = (int) (defaultTileSize * scaling);
        screenHeight = size.height;
        screenWidth = size.width;
        screenCenter = new Point((GamePanel.screenWidth - GamePanel.tileSize) / 2, (GamePanel.screenHeight - GamePanel.tileSize) / 2); // center of the screen

    }
}
