package entities;

import gamestates.PlayState;
import main.GameLoop;
import main.GamePanel;
import ui.Sound;

import java.awt.*;
import java.io.Serializable;

import static main.GamePanel.scaling;

public class Stats implements Serializable {
    public final int maxHealth = 100;
    public final int maxEnergy = 100;
    private int health = 100;
    private int energy = 100;
    private int money = 1000;
    private int wisdom = 1;
    private int luck = 1;
    private int charisma = 1;
    private int stamina = 1;

    private boolean statsShown = false;


    public void showStats(Graphics2D g) {
        createFrame(g);
    }

    private void createFrame(Graphics2D g) {
        Point framePos = new Point(GamePanel.tileSize / 2, GamePanel.tileSize * 8);
        int frameWidth = GamePanel.tileSize * 4;
        int frameHeight = GamePanel.tileSize * 3;

        drawFrame(framePos, frameWidth, frameHeight, g);
        drawStatsText(framePos, frameWidth, frameHeight, g);

    }

    private void drawFrame(Point pos, int width, int height, Graphics2D g) {
        Color c = new Color(0, 0, 0, 220);
        g.setColor(c);
        g.fillRoundRect(pos.x, pos.y, width, height, (int) (9*scaling), (int) (9*scaling));

        c = Color.white;
        g.setColor(c);
        g.setStroke(new BasicStroke(5));
        g.drawRoundRect((int) (pos.x + 1.5f*scaling), (int) (pos.y + 1.5f*scaling), (int) (width - 3*scaling), (int) (height - 3*scaling), (int) (6*scaling), (int) (6*scaling));
    }

    private void drawStatsText(Point pos, int width, int height, Graphics2D g) {
        g.setColor(Color.white);
        g.setFont(new Font("Kenney Pixel", Font.PLAIN, (int) (11*scaling)));
        FontMetrics fm = g.getFontMetrics();
        // draw names of stats
        Point namesPos = new Point(pos.x + GamePanel.tileSize / 3, pos.y + fm.getHeight() / 2 + GamePanel.tileSize * 2 / 5);
        int spacing = fm.getHeight() + GamePanel.tileSize / 8;
        String[] names = {"WISDOM", "LUCK", "CHARISMA", "STAMINA"};
        for (String s : names) {
            g.drawString(s, namesPos.x, namesPos.y);
            namesPos.y += spacing;
        }
        // draw values of stats
        Point valuesPos = new Point(pos.x + width - GamePanel.tileSize / 3, pos.y + g.getFontMetrics().getHeight() / 2 + GamePanel.tileSize * 2 / 5);
        int[] values = {wisdom, luck, charisma, stamina};
        for (int i : values) {
            String s = String.valueOf(i);
            g.drawString(s, valuesPos.x - fm.stringWidth(s), valuesPos.y);
            valuesPos.y += spacing;
        }
    }

    public int getHealth() {
        return health;
    }

    public void changeHealth(int health) {
        if (health > 0)
            if (this.health <= maxHealth - health)
                this.health += health;
            else
                this.health = maxHealth;
        else {
            if (this.health > -health)
                this.health += health;
            else
                PlayState.setGameOver();
        }
    }

    public int getEnergy() {
        return energy;
    }

    public void changeEnergy(int energy) {
        if (energy > 0)
            if (this.energy <= maxEnergy - energy)
                this.energy += energy;
            else
                this.energy = maxEnergy;
        else {
            if (this.energy >= -energy)
                this.energy += energy;
            else
                this.energy = 0;
        }
    }

    public int getMoney() {
        return money;
    }

    public void changeMoney(int money) {
        this.money += money;
        if (money > 0)
            GameLoop.playSE(Sound.EARNING_SE);
        else
            GameLoop.playSE(Sound.SPENDING_SE);
    }

    public int getWisdom() {
        return wisdom;
    }

    public void changeWisdom(int wisdom) {
        this.wisdom += wisdom;
    }

    public int getLuck() {
        return luck;
    }

    public void changeLuck(int luck) {
        this.luck += luck;
    }

    public int getCharisma() {
        return charisma;
    }

    public void changeCharisma(int charisma) {
        this.charisma += charisma;
    }

    public int getStamina() {
        return stamina;
    }

    public void changeStamina(int stamina) {
        this.stamina += stamina;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public boolean isStatsShown() {
        return statsShown;
    }

    public void setStatsShown(boolean statsShown) {
        this.statsShown = statsShown;
    }

    public boolean isEnergySufficient(int cost) {
        return energy >= cost;

    }

    public boolean isMoneySufficient(int cost) {
        return money >= cost;
    }

    public void resetStats() {
        health = maxHealth;
        energy = maxEnergy;
        stamina = 1;
        luck = 1;
        wisdom = 1;
        charisma = 1;
        money = 1000;
    }

}
