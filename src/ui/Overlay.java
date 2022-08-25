package ui;

import entities.Player;
import gamestates.PlayState;
import main.GamePanel;
import util.UtilityClass;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static main.GamePanel.*;

public class Overlay {

    private final Point screenPos;
    private final Player player;
    private final PlayState playState;
    private final GameButton nextTurnButton;
    private final Rectangle healthBarRec, energyBarRec;
    private BufferedImage longBar;
    private BufferedImage shortBar;
    private BufferedImage moneyBar;
    private BufferedImage healthBar;
    private BufferedImage energyBar;
    private int money, health, energy, turn;
    private boolean showExactHealth, showExactEnergy;

    public Overlay(PlayState playState) {
        this.player = playState.getPlayer();
        this.playState = playState;
        screenPos = new Point(tileSize / 4, 0);
        loadImages();
        nextTurnButton = new GameButton(new Point((int) (screenWidth - 1.25f * tileSize), (int) (0.75f * tileSize)), "END TURN", 9*scaling) {
            @Override
            public void performAction() {
                playState.nextTurn();
            }
        };
        healthBarRec = new Rectangle((int) (screenPos.x + tileSize + 11*scaling), (int) (screenPos.y + 4.5f*scaling), healthBar.getWidth(), (int) (6*scaling));
        energyBarRec = new Rectangle((int) (screenPos.x + tileSize + 11*scaling), (int) (screenPos.y + 10.5f*scaling), energyBar.getWidth(), (int) (6*scaling));
    }

    public void draw(Graphics2D g) {
        createLayout(g);
        displayStatus(g);
        nextTurnButton.draw(g);
    }

    public void update() {
        money = player.getStats().getMoney();
        health = player.getStats().getHealth();
        energy = player.getStats().getEnergy();
        turn = playState.getTurn();
        nextTurnButton.update();
    }

    private void loadImages() {
        longBar = new BufferedImage(4 * tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
        moneyBar = new BufferedImage(4 * tileSize, 2 * tileSize, BufferedImage.TYPE_INT_ARGB);
        healthBar = new BufferedImage(3 * tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
        energyBar = new BufferedImage((int) (41 * scaling), tileSize, BufferedImage.TYPE_INT_ARGB);
        shortBar = new BufferedImage(4 * tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
        BufferedImage atlas = UtilityClass.loadSpriteAtlas("/res/gui/GUI.png");
        BufferedImage img;

        Graphics2D g = longBar.createGraphics();
        for (int i = 0; i < longBar.getWidth() / tileSize; i++) {
            img = atlas.getSubimage((i + 8) * GamePanel.defaultTileSize, GamePanel.defaultTileSize, GamePanel.defaultTileSize, GamePanel.defaultTileSize);
            g.drawImage(img, i * GamePanel.tileSize, 0, GamePanel.tileSize, GamePanel.tileSize, null);
        }

        g = moneyBar.createGraphics();
        for (int i = 0; i < 2; i++) {
            img = atlas.getSubimage((i + 6) * GamePanel.defaultTileSize, 2 * GamePanel.defaultTileSize, GamePanel.defaultTileSize, GamePanel.defaultTileSize);
            g.drawImage(img, i * GamePanel.tileSize, 0, GamePanel.tileSize, GamePanel.tileSize, null);
        }
        for (int i = 0; i < moneyBar.getWidth() / tileSize; i++) {
            img = atlas.getSubimage((i + 6) * GamePanel.defaultTileSize, 3 * GamePanel.defaultTileSize, GamePanel.defaultTileSize, GamePanel.defaultTileSize);
            g.drawImage(img, i * GamePanel.tileSize, tileSize, GamePanel.tileSize, GamePanel.tileSize, null);
        }

        g = healthBar.createGraphics();
        img = atlas.getSubimage(4 * GamePanel.defaultTileSize + 9, GamePanel.defaultTileSize, 48, GamePanel.defaultTileSize);
        g.drawImage(img, 0, 0, healthBar.getWidth(), GamePanel.tileSize, null);

        g = energyBar.createGraphics();
        img = atlas.getSubimage(14, GamePanel.defaultTileSize, 41, GamePanel.defaultTileSize);
        g.drawImage(img, 0, 0, energyBar.getWidth(), GamePanel.tileSize, null);

        g = shortBar.createGraphics();
        for (int i = 0; i < shortBar.getWidth() / tileSize; i++) {
            img = atlas.getSubimage((i + 8) * GamePanel.defaultTileSize, 0, GamePanel.defaultTileSize, GamePanel.defaultTileSize);
            g.drawImage(img, i * GamePanel.tileSize, 0, GamePanel.tileSize, GamePanel.tileSize, null);
        }
    }

    private void createLayout(Graphics2D g) {
        g.drawImage(moneyBar, screenPos.x, screenPos.y, null);
        g.drawImage(longBar, (int) (screenPos.x + tileSize + scaling*2), screenPos.y, null);
        g.drawImage(shortBar, (int) (screenPos.x + tileSize + scaling*2), (int) (screenPos.y + 6*scaling), null);
    }

    private void displayStatus(Graphics2D g) {
        displayMoney(g);
        displayHealth(g);
        displayEnergy(g);
        displayTurn(g);
    }

    private void displayHealth(Graphics2D g) {
        BufferedImage healthBar = new BufferedImage(this.healthBar.getWidth(), this.healthBar.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g1 = healthBar.createGraphics();
        BufferedImage img;
        double healthStatus = 1 - (double) health / player.getStats().getMaxHealth();
        int barLength = (int) (this.healthBar.getWidth() * (1 - healthStatus));
        if (barLength > 0) {
            img = this.healthBar.getSubimage(this.healthBar.getWidth() - barLength, 0, barLength, this.healthBar.getHeight());
            g1.drawImage(img, 0, 0, barLength, this.healthBar.getHeight(), null);
            if (showExactHealth) {
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                        0.5f));
                g.drawImage(healthBar, (int) (screenPos.x + tileSize + 11*scaling), screenPos.y, null);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                        1f));
                Point p = new Point((int) (screenPos.x + tileSize + 11*scaling + healthBar.getWidth() / 2), (int) (screenPos.y + 9*scaling));
                p = UtilityClass.centerText(g, String.valueOf(energy), p);
                g.setFont(g.getFont().deriveFont(6*scaling));
                g.drawString(String.valueOf(health), p.x, p.y);
            } else
                g.drawImage(healthBar, (int) (screenPos.x + tileSize + 11*scaling), screenPos.y, null);
        }
    }

    private void displayMoney(Graphics2D g) {
        // displaying amount of money
        Font font = new Font("Kenney Pixel", Font.PLAIN, (int) (9.5f*scaling));
        g.setFont(font);
        g.setColor(Color.white);
        FontMetrics fontMetrics = g.getFontMetrics();
        String moneyAmount = "";
        if (money >= 1e9)
            moneyAmount = String.format("%.3f", money / 1e9) + "B";
        else if (money >= 1e6)
            moneyAmount = String.format("%.3f", money / 1e6) + "M";
        else if (money >= 1e3)
            moneyAmount = String.format("%.3f", money / 1e3) + "K";
        else
            moneyAmount = String.valueOf(money);

        //String money = String.valueOf(this.money);
        g.drawString(moneyAmount, (screenPos.x + 4 * tileSize - 3*scaling) - fontMetrics.stringWidth(moneyAmount), screenPos.y + tileSize + 8*scaling);
    }

    private void displayEnergy(Graphics2D g) {
        BufferedImage energyBar = new BufferedImage(this.energyBar.getWidth(), this.energyBar.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g1 = energyBar.createGraphics();
        BufferedImage img;
        double energyStatus = 1 - (double) energy / player.getStats().getMaxEnergy();
        int barLength = (int) (this.energyBar.getWidth() * (1 - energyStatus));
        if (barLength > 0) {
            img = this.energyBar.getSubimage(this.energyBar.getWidth() - barLength, 0, barLength, this.energyBar.getHeight());
            g1.drawImage(img, 0, 0, barLength, this.energyBar.getHeight(), null);
            if (showExactEnergy)
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g.drawImage(energyBar, (int) (screenPos.x + tileSize + 11*scaling), (int) (screenPos.y + 6*scaling), null);
        }
        if (showExactEnergy) {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                    1f));
            Point p = new Point((int) (screenPos.x + tileSize + 11*scaling + energyBar.getWidth() / 2), (int) (screenPos.y + 15*scaling));
            p = UtilityClass.centerText(g, String.valueOf(energy), p);
            g.setFont(g.getFont().deriveFont(6*scaling));
            g.drawString(String.valueOf(energy), p.x, p.y);
        }
    }

    private void displayTurn(Graphics2D g) {
        Font font = new Font("Kenney Pixel", Font.PLAIN, (int) (10*scaling));
        g.setFont(font);
        g.setColor(Color.white);
        FontMetrics fontMetrics = g.getFontMetrics();
        String s = "TURN:";
        g.drawString(s, (screenPos.x + 13.5f*scaling) - fontMetrics.stringWidth(s) / 2, screenPos.y + 12.5f*scaling);
        s = String.valueOf(turn);
        font = font.deriveFont(14*scaling);
        g.setFont(font);
        fontMetrics = g.getFontMetrics();
        g.drawString(s, (screenPos.x + 13.5f*scaling) - fontMetrics.stringWidth(s) / 2, screenPos.y + 7 * tileSize / 5);
    }

    public void mousePressed(MouseEvent e) {
        nextTurnButton.mousePressed(e);
    }

    public void mouseReleased(MouseEvent e) {
        nextTurnButton.mouseReleased(e);
    }

    public void mouseMoved(MouseEvent e) {
        nextTurnButton.mouseMoved(e);
        showExactHealth = healthBarRec.contains(e.getX(), e.getY());
        showExactEnergy = energyBarRec.contains(e.getX(), e.getY());
    }
}
