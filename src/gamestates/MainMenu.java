package gamestates;

import main.GameLoop;
import main.GamePanel;
import main.GameWindow;
import ui.GameButton;
import ui.Sound;
import util.GameConfiguration;
import util.UtilityClass;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static main.GamePanel.*;

public class MainMenu implements GameStateMethods {

    private GameButton[] buttons;
    private int aniTick, aniIndex;
    private int aniFreq = 9;
    private BufferedImage[] animation;
    private GameLoop gameLoop;

    public MainMenu(GameLoop gl) {
        gameLoop = gl;
        setupButtons();
        animation = loadAnimation();
    }

    private void setupButtons() {
        buttons = new GameButton[3];
        buttons[0] = new GameButton(new Point(GamePanel.screenWidth / 2, (int) (screenHeight / 2 + 1.5f * tileSize)), "New Game", 3 * tileSize, (int) (11 * scaling)) {
            @Override
            public void performAction() {
                gameLoop.getPlayState().restartGame();
                GameLoop.setGameState(GameLoop.gameStateType.PLAY);
                gameLoop.getPlayState().setPlaySubState(PlayState.PlayStateType.RUN);
                gameLoop.getPlayState().update();
                gameLoop.getPlayState().setPlaySubState(PlayState.PlayStateType.TRANSITION);
                GameConfiguration.serializeGame();
            }
        };
        buttons[1] = new GameButton(new Point(GamePanel.screenWidth / 2, (int) (GamePanel.screenHeight / 2 + 3f * tileSize)), "Load Game", 3 * tileSize, (int) (11 * scaling)) {
            @Override
            public void performAction() {

                if (GameConfiguration.deserializeGame())
                    GameLoop.setGameState(GameLoop.gameStateType.PLAY);
                else
                    GameLoop.playSE(Sound.ERROR_SE);

            }
        };
        buttons[2] = new GameButton(new Point(GamePanel.screenWidth / 2, (int) (GamePanel.screenHeight / 2 + 4.5f * tileSize)), "Exit", 3 * tileSize, (int) (12 * scaling)) {
            @Override
            public void performAction() {
                GameConfiguration.serializeGame();
                System.exit(0);
            }
        };
    }

    @Override
    public void update() {
        for (GameButton gb : buttons)
            gb.update();
        updateAnimation();
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.white);
        g.setFont(new Font("Kenney Pixel", Font.BOLD, (int) (24 * scaling)));
        String s = GameWindow.gameName;
        Point p = UtilityClass.centerText(g, s, new Point(GamePanel.screenWidth / 2, (int) (screenHeight * 0.15f)));
        g.drawString(s, p.x, p.y);
        for (GameButton gb : buttons)
            gb.draw(g);
        Point animationPos = new Point((screenWidth - animation[0].getWidth()) / 2, (int) (screenHeight * 0.25f));
        g.drawImage(animation[aniIndex], animationPos.x, animationPos.y, null);
    }

    private BufferedImage[] loadAnimation() {
        BufferedImage[] animation = new BufferedImage[9];
        BufferedImage atlas = UtilityClass.loadSpriteAtlas("/res/animations/coin_animated.png");
        Graphics2D g;
        for (int i = 0; i < animation.length; i++) {
            BufferedImage img = atlas.getSubimage(i * 20, 0, 20, 20);
            animation[i] = new BufferedImage((int) (img.getWidth() * scaling * 2), (int) (img.getHeight() * scaling * 2), BufferedImage.TYPE_INT_ARGB);
            g = animation[i].createGraphics();
            g.drawImage((Image) img, 0, 0, (int) (img.getWidth() * scaling * 2), (int) (img.getHeight() * scaling * 2), null);
        }
        return animation;
    }

    private void updateAnimation() {
        aniTick++;
        if (aniTick >= aniFreq) {
            aniTick = 0;
            aniIndex++;
        }
        if (aniIndex >= animation.length) {
            aniIndex = 0;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (GameButton gb : buttons)
            gb.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (GameButton gb : buttons)
            gb.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (GameButton gb : buttons)
            gb.mouseMoved(e);
    }

}
