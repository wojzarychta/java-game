package gamestates;

import main.GameLoop;
import main.GamePanel;
import ui.GameButton;
import ui.SlideBar;
import ui.SmallButton;
import ui.Sound;
import util.GameConfiguration;
import util.UtilityClass;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import static main.GamePanel.*;

public class Options implements GameStateMethods, Serializable {
    private transient final int panelWidth;
    private transient final int panelHeight;
    private transient final SlideBar[] slideBars = new SlideBar[2];
    private transient final PauseScreen pauseScreen;
    private transient SmallButton tickBox;
    private transient BufferedImage panel;
    private transient GameButton returnButton;
    private boolean fullScreenOn;
    private float musicVolume, SFXVolume;


    public Options(PauseScreen ps) {
        pauseScreen = ps;
        panelWidth = 6 * tileSize;
        panelHeight = 8 * tileSize;
        createPanel();
        setupButtons();
        GameConfiguration.loadSettings(this);
    }

    @Override
    public void update() {
        for (SlideBar slideBar : slideBars)
            slideBar.update();
        tickBox.update();
        returnButton.update();
    }

    @Override
    public void draw(Graphics2D g) {
        g.drawImage(panel, (GamePanel.screenWidth - panelWidth) / 2, (GamePanel.screenHeight - panelHeight) / 2, null);
        returnButton.draw(g);
        for (SlideBar slideBar : slideBars)
            slideBar.draw(g);
        tickBox.draw(g);
        drawText(g);
    }

    private void drawText(Graphics2D g) {
        g.setColor(Color.white);
        g.setFont(new Font("Kenney Pixel", Font.BOLD, (int) (11 * scaling)));
        String s = "FULL SCREEN";
        g.drawString(s, (GamePanel.screenWidth - panelWidth) / 2 + 0.75f * tileSize, (GamePanel.screenHeight - panelHeight) / 2 + 1.15f * tileSize);
        s = "MUSIC";
        Point p = UtilityClass.centerText(g, s, new Point(screenWidth / 2, (int) ((screenHeight - panelHeight) / 2 + 2.25f * tileSize)));
        g.drawString(s, p.x, p.y);
        s = "SFX";
        p = UtilityClass.centerText(g, s, new Point(screenWidth / 2, (int) ((screenHeight - panelHeight) / 2 + 4.25f * tileSize)));
        g.drawString(s, p.x, p.y);
    }

    private void createPanel() {
        panel = new BufferedImage(panelWidth, panelHeight, BufferedImage.TYPE_INT_ARGB);
        BufferedImage atlas = UtilityClass.loadSpriteAtlas("/res/gui/GUI.png");
        BufferedImage img;
        Graphics2D g = panel.createGraphics();
        img = atlas.getSubimage(0, 2 * GamePanel.defaultTileSize, GamePanel.defaultTileSize, GamePanel.defaultTileSize);
        g.drawImage(img, 0, 0, tileSize, tileSize, null);
        for (int i = 1; i < panelWidth / tileSize - 1; i++) {
            img = atlas.getSubimage(defaultTileSize, 2 * GamePanel.defaultTileSize, GamePanel.defaultTileSize, GamePanel.defaultTileSize);
            g.drawImage(img, i * tileSize, 0, tileSize, tileSize, null);
        }
        img = atlas.getSubimage(2 * defaultTileSize, 2 * GamePanel.defaultTileSize, GamePanel.defaultTileSize, GamePanel.defaultTileSize);
        g.drawImage(img, panelWidth - tileSize, 0, tileSize, tileSize, null);

        for (int j = 1; j < panelHeight / tileSize - 1; j++) {
            img = atlas.getSubimage(0, 3 * GamePanel.defaultTileSize, GamePanel.defaultTileSize, GamePanel.defaultTileSize);
            g.drawImage(img, 0, j * tileSize, tileSize, tileSize, null);
            for (int i = 1; i < panelWidth / tileSize - 1; i++) {
                img = atlas.getSubimage(defaultTileSize, 3 * GamePanel.defaultTileSize, GamePanel.defaultTileSize, GamePanel.defaultTileSize);
                g.drawImage(img, i * tileSize, j * tileSize, tileSize, tileSize, null);
            }
            img = atlas.getSubimage(2 * defaultTileSize, 3 * GamePanel.defaultTileSize, GamePanel.defaultTileSize, GamePanel.defaultTileSize);
            g.drawImage(img, panelWidth - tileSize, j * tileSize, tileSize, tileSize, null);
        }

        img = atlas.getSubimage(0, 4 * GamePanel.defaultTileSize, GamePanel.defaultTileSize, GamePanel.defaultTileSize);
        g.drawImage(img, 0, panelHeight - tileSize, tileSize, tileSize, null);
        for (int i = 1; i < panelWidth / tileSize - 1; i++) {
            img = atlas.getSubimage(defaultTileSize, 4 * GamePanel.defaultTileSize, GamePanel.defaultTileSize, GamePanel.defaultTileSize);
            g.drawImage(img, i * tileSize, panelHeight - tileSize, tileSize, tileSize, null);
        }
        img = atlas.getSubimage(2 * defaultTileSize, 4 * GamePanel.defaultTileSize, GamePanel.defaultTileSize, GamePanel.defaultTileSize);
        g.drawImage(img, panelWidth - tileSize, panelHeight - tileSize, tileSize, tileSize, null);

    }

    private void setupButtons() {
        returnButton = new GameButton(new Point(screenWidth / 2, (int) ((screenHeight + panelHeight) / 2 - 1.25f * tileSize)), "RETURN") {
            @Override
            public void performAction() {
                pauseScreen.setOptionsEnabled(false);
            }
        };
        Options o = this;
        tickBox = new SmallButton(new Point((int) ((screenWidth + panelWidth) / 2 - 1.25f * tileSize), (GamePanel.screenHeight - panelHeight) / 2 + tileSize), SmallButton.Function.TICK_BOX) {

            @Override
            public void performAction() {
                fullScreenOn = !fullScreenOn;
                GameConfiguration.saveSettings(o);
            }

        };
        tickBox.setMousePressed(fullScreenOn);
        slideBars[0] = new SlideBar(new Point(screenWidth / 2, (GamePanel.screenHeight - panelHeight) / 2 + 3 * tileSize)) {
            @Override
            public void useValue() {
                musicVolume = (float) getValue() / (float) getMaxValue();
                GameLoop.sound.setMusicVolume(musicVolume);
                GameConfiguration.saveSettings(o);
            }
        };
        slideBars[1] = new SlideBar(new Point(screenWidth / 2, (GamePanel.screenHeight - panelHeight) / 2 + 5 * tileSize)) {
            @Override
            public void useValue() {
                SFXVolume = (float) getValue() / (float) getMaxValue();
                GameLoop.sound.setSEVolume(SFXVolume);
                GameConfiguration.saveSettings(o);
            }
        };
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
        returnButton.mousePressed(e);
        for (SlideBar slideBar : slideBars)
            slideBar.mousePressed(e);
        tickBox.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        returnButton.mouseReleased(e);
        for (SlideBar slideBar : slideBars)
            slideBar.mouseReleased(e);
        tickBox.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        returnButton.mouseMoved(e);
        for (SlideBar slideBar : slideBars)
            slideBar.mouseMoved(e);
        tickBox.mouseMoved(e);
    }

    public void loadOptions(Options o) {
        musicVolume = o.musicVolume;
        SFXVolume = o.SFXVolume;
        fullScreenOn = o.fullScreenOn;
        tickBox.setMousePressed(fullScreenOn);
        slideBars[0].setValue((int) (slideBars[0].getMaxValue() * musicVolume));
        slideBars[1].setValue((int) (slideBars[1].getMaxValue() * SFXVolume));
        GameLoop.sound.setSEVolume(SFXVolume);
        GameLoop.sound.setMusicVolume(musicVolume);
    }

    public boolean isFullScreenOn() {
        return fullScreenOn;
    }
}
