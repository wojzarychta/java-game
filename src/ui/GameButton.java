package ui;

import main.GameLoop;
import main.GamePanel;
import util.UtilityClass;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static main.GamePanel.scaling;
import static main.GamePanel.tileSize;

public class GameButton extends Button {
    private final int width;
    private final int height;
    private BufferedImage[] buttonImgs;

    private String text;

    private boolean isRadioButton = false;
    private GameButton[] radioButtons;
    private float fontSize = 10*scaling;

    public GameButton(Point screenPos, String text) {
        super(screenPos);
        width = 2 * tileSize;
        height = tileSize;
        loadButtonImgs();
        bounds = new Rectangle(screenPos.x - width / 2, screenPos.y - height / 2, width, height);
        this.text = text;
    }

    public GameButton(Point screenPos, String text, int width) {
        super(screenPos);
        this.width =width;
        height = width/2;
        loadButtonImgs();
        bounds = new Rectangle(screenPos.x - width / 2, screenPos.y - height / 2, width, height);
        this.text = text;
        fontSize *= (double)width/(2*tileSize);
    }
    public GameButton(Point screenPos, String text, int width, int fontSize) {
        super(screenPos);
        this.width =width;
        height = width/2;
        loadButtonImgs();
        bounds = new Rectangle(screenPos.x - width / 2, screenPos.y - height / 2, width, height);
        this.text = text;
        this.fontSize = fontSize;
    }

    public GameButton(Point screenPos, String text, float fontSize) {
        super(screenPos);
        width = 2 * tileSize;
        height = tileSize;
        this.fontSize = fontSize;
        loadButtonImgs();
        bounds = new Rectangle(screenPos.x - width / 2, screenPos.y - height / 2, width, height);
        this.text = text;
    }


    protected void loadButtonImgs() {
        buttonImgs = new BufferedImage[3];
        BufferedImage atlas = UtilityClass.loadSpriteAtlas("/res/gui/GUI.png");
        for (int i = 0; i < buttonImgs.length; i++) {
            buttonImgs[i] = atlas.getSubimage(7 * GamePanel.defaultTileSize, (5 + i) * GamePanel.defaultTileSize, 2 * GamePanel.defaultTileSize, GamePanel.defaultTileSize);
        }
    }

    public void draw(Graphics2D g) {
        g.drawImage(buttonImgs[index], screenPos.x - width / 2, screenPos.y - height / 2, width, height, null);
        drawText(g);
    }

    public void update() {
        index = 2;
        if (mouseOver)
            index = 0;
        if (mousePressed)
            index = 1;
        //System.out.println(index);
    }

    private void drawText(Graphics2D g) {
        g.setColor(Color.white);
        g.setFont(new Font("Kenney Pixel", Font.BOLD, (int) fontSize));
        Point pos = UtilityClass.centerText(g, text, screenPos);
        if (index == 2 || index == 0)
            g.drawString(text, pos.x, pos.y + height / 10);
        else
            g.drawString(text, pos.x, pos.y + height / 8);
    }


    public void mousePressed(MouseEvent e) {
        if (isRadioButton) {
            if (checkBounds(e)) {
                setMousePressed(true);
                for (GameButton gb : radioButtons)
                    if (gb != this)
                        gb.setMousePressed(false);
            }
        } else {
            if (checkBounds(e)) {
                setMousePressed(true);
            }
        }

    }

    public void mouseReleased(MouseEvent e) {
        if (!isRadioButton) {
            if (mousePressed) {
                mousePressed = false;
                GameLoop.playSE(Sound.MOUSE_CLICKED);
                performAction();
            }
        }
    }

    public void createRadioButtons(GameButton... gb) {
        isRadioButton = true;
        radioButtons = gb;
    }

    public void updateText(String text) {
        this.text = text;
    }

}
