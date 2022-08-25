package gamestates;

import main.GameLoop;
import main.GamePanel;
import ui.PauseButton;
import util.GameConfiguration;
import util.UtilityClass;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class PauseScreen implements GameStateMethods {
    private final int panelWidth;
    private final int panelHeight;
    private final PauseButton[] buttons;
    private final Options options;
    private BufferedImage panel;
    private boolean optionsEnabled = false;

    public PauseScreen() {
        buttons = new PauseButton[3];
        setupButtons();

        panelWidth = 3 * GamePanel.tileSize;
        panelHeight = 4 * GamePanel.tileSize;
        createPanel();
        options = new Options(this);
    }

    @Override
    public void update() {
        if (!optionsEnabled)
            for (PauseButton gb : buttons)
                gb.update();
        else
            options.update();
    }

    @Override
    public void draw(Graphics2D g) {
        dimBackground(g);
        if (!optionsEnabled) {
            g.drawImage(panel, (GamePanel.screenWidth - panelWidth) / 2, (GamePanel.screenHeight - panelHeight) / 2, null);
            for (PauseButton gb : buttons)
                gb.draw(g);
        } else
            options.draw(g);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_ESCAPE -> GameLoop.setGameState(GameLoop.gameStateType.PLAY);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!optionsEnabled) {
            for (PauseButton gb : buttons) {
                if (gb.checkBounds(e)) {
                    gb.setMousePressed(true);
                    break;
                }
            }
        } else
            options.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!optionsEnabled) {
            for (PauseButton gb : buttons) {
                if (gb.isMousePressed()) {
                    gb.setMousePressed(false);
                    performAction(gb.getAction());
                    break;
                }
            }
        } else
            options.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!optionsEnabled) {
            for (PauseButton gb : buttons) {
                gb.setMouseOver(false);
            }
            for (PauseButton gb : buttons) {
                if (gb.checkBounds(e)) {
                    gb.setMouseOver(true);
                    break;
                }
            }
        } else
            options.mouseMoved(e);
    }


    private void createPanel() {
        panel = new BufferedImage(panelWidth, panelHeight, BufferedImage.TYPE_INT_ARGB);
        BufferedImage atlas = UtilityClass.loadSpriteAtlas("/res/gui/GUI.png");
        BufferedImage img;
        Graphics2D g = panel.createGraphics();
        for (int i = 0; i < panelWidth / GamePanel.tileSize; i++) {
            img = atlas.getSubimage(i * GamePanel.defaultTileSize, 2 * GamePanel.defaultTileSize, GamePanel.defaultTileSize, GamePanel.defaultTileSize);
            g.drawImage(img, i * GamePanel.tileSize, 0, GamePanel.tileSize, GamePanel.tileSize, null);
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < panelWidth / GamePanel.tileSize; j++) {
                img = atlas.getSubimage(j * GamePanel.defaultTileSize, 3 * GamePanel.defaultTileSize, GamePanel.defaultTileSize, GamePanel.defaultTileSize);
                g.drawImage(img, j * GamePanel.tileSize, (i + 1) * GamePanel.tileSize, GamePanel.tileSize, GamePanel.tileSize, null);
            }
        }
        for (int i = 0; i < panelWidth / GamePanel.tileSize; i++) {
            img = atlas.getSubimage(i * GamePanel.defaultTileSize, 4 * GamePanel.defaultTileSize, GamePanel.defaultTileSize, GamePanel.defaultTileSize);
            g.drawImage(img, i * GamePanel.tileSize, 3 * GamePanel.tileSize, GamePanel.tileSize, GamePanel.tileSize, null);
        }
    }

    private void setupButtons() {
        buttons[0] = new PauseButton(new Point(GamePanel.screenWidth / 2, GamePanel.screenHeight / 2 - GamePanel.tileSize), ActionType.BACK);
        buttons[1] = new PauseButton(new Point(GamePanel.screenWidth / 2, GamePanel.screenHeight / 2), ActionType.OPTIONS);
        buttons[2] = new PauseButton(new Point(GamePanel.screenWidth / 2, GamePanel.screenHeight / 2 + GamePanel.tileSize), ActionType.EXIT);
    }

    private void performAction(ActionType action) {
        switch (action) {

            case BACK -> {
                GameLoop.setGameState(GameLoop.gameStateType.PLAY);
            }
            case OPTIONS -> {
                optionsEnabled = true;
            }
            case EXIT -> {
                GameConfiguration.serializeGame();
                GameLoop.setGameState(GameLoop.gameStateType.MAIN_MENU);
            }
        }
    }

    private void dimBackground(Graphics2D g) {
        Color color = new Color(0, 0, 0, 0.7f);
        g.setColor(color);
        g.fillRect(0, 0, GamePanel.screenWidth, GamePanel.screenHeight);
    }

    public enum ActionType {BACK, OPTIONS, EXIT}

    public void setOptionsEnabled(boolean optionsEnabled) {
        this.optionsEnabled = optionsEnabled;
    }
}
