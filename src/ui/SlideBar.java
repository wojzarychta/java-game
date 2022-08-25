package ui;

import main.GamePanel;
import util.UtilityClass;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static main.GamePanel.tileSize;

public class SlideBar {
    private final int maxValue = 6;
    private final Point pos;
    //private GameButton amountButton;
    private SmallButton[] arrowButtons;
    private int value = 3;
    private BufferedImage[] sliders = new BufferedImage[2];

    public SlideBar(Point pos) {
        this.pos = pos;
        loadSliders();
        setupButtons();
    }

    public void draw(Graphics2D g) {
        for (SmallButton sb : arrowButtons)
            sb.draw(g);
        displaySlideBar(g);
    }

    public void update() {
        for (SmallButton sb : arrowButtons)
            sb.update();
    }

    private void displaySlideBar(Graphics2D g){
        g.drawImage(sliders[0], (int) (pos.x-tileSize*1.5f), pos.y-tileSize/2, null);
        int barLength = (int) (this.sliders[1].getWidth() * value/maxValue);
        BufferedImage bar = new BufferedImage(this.sliders[1].getWidth(), this.sliders[1].getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g1 = bar.createGraphics();
        BufferedImage img;
        if (barLength > 0) {
            img = this.sliders[1].getSubimage(0, 0, barLength, this.sliders[1].getHeight());
            g1.drawImage(img, 0, 0, barLength, this.sliders[1].getHeight(), null);
            g.drawImage(bar, (int) (pos.x-tileSize*1.5f), pos.y-tileSize/2, null);
        }
    }

    private void loadSliders(){
        BufferedImage atlas = UtilityClass.loadSpriteAtlas("/res/gui/GUI.png");
        sliders[0] = new BufferedImage(3*tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
        sliders[1] = new BufferedImage(3*tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
        BufferedImage img;
        Graphics2D g = sliders[0].createGraphics();
        img = atlas.getSubimage(3 * GamePanel.defaultTileSize, 2 * GamePanel.defaultTileSize, 3*GamePanel.defaultTileSize, GamePanel.defaultTileSize);
        g.drawImage(img, 0, 0, 3*GamePanel.tileSize, GamePanel.tileSize, null);
        g=sliders[1].createGraphics();
        img = atlas.getSubimage(3 * GamePanel.defaultTileSize, 3 * GamePanel.defaultTileSize, 3*GamePanel.defaultTileSize, GamePanel.defaultTileSize);
        g.drawImage(img, 0, 0, 3*GamePanel.tileSize, GamePanel.tileSize, null);
    }

    public void mousePressed(MouseEvent e) {
        for (SmallButton sb : arrowButtons)
            sb.mousePressed(e);
    }

    public void mouseReleased(MouseEvent e) {
        for (SmallButton sb : arrowButtons)
            sb.mouseReleased(e);
    }

    public void mouseMoved(MouseEvent e) {
        for (SmallButton sb : arrowButtons)
            sb.mouseMoved(e);
    }

    private void setupButtons() {
        arrowButtons = new SmallButton[2];
        arrowButtons[0] = new SmallButton(new Point(pos.x + (int) (-2 * tileSize), pos.y), SmallButton.Function.LEFT_ARROW) {
            @Override
            public void performAction() {
                changeValue(-1);
                useValue();
            }
        };
        arrowButtons[1] = new SmallButton(new Point(pos.x + (int) (2 * tileSize), pos.y), SmallButton.Function.RIGHT_ARROW) {
            @Override
            public void performAction() {
                changeValue(+1);
                useValue();
            }
        };

    }

    private void changeValue(int change) {
        value += change;
        if (value > maxValue)
            value = maxValue;
        if (value < 0)
            value = 0;
    }

    public void setValue(int i){
        value = i;
        if (value > maxValue)
            value = maxValue;
        if (value < 0)
            value = 0;
    }

    public void useValue() {
    }

    public int getValue() {
        return value;
    }

    public int getMaxValue() {
        return maxValue;
    }
}
