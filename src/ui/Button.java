package ui;

import main.GameLoop;
import main.GamePanel;
import util.UtilityClass;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Button {
    protected final Point screenPos;  // center of button
    protected Rectangle bounds;
    protected BufferedImage[] buttonImgs;
    protected int index = 0;
    protected int width;
    protected int height;
    protected boolean mouseOver, mousePressed;
    protected BufferedImage atlas;

    protected boolean isRadioButton = false;
    private Button[] radioButtons;

    public Button(Point screenPos) {
        this.screenPos = screenPos;
        setDimensions();
        bounds = new Rectangle(screenPos.x - width / 2, screenPos.y - height / 2, width, height);
        loadAtlas();
        loadButtonImgs();
    }

    protected void setDimensions() {
        width = GamePanel.tileSize;
        height = GamePanel.tileSize;
    }

    protected void loadAtlas() {
        atlas = UtilityClass.loadSpriteAtlas("/res/gui/GUI.png");
    }


    protected void loadButtonImgs() {
        buttonImgs = new BufferedImage[3];
    }


    public void draw(Graphics2D g) {
        g.drawImage(buttonImgs[index], screenPos.x - width / 2, screenPos.y - height / 2, width, height, null);
    }

    public void update() {
        index = 2;
        if (mouseOver)
            index = 0;
        if (mousePressed)
            index = 1;
    }

    public boolean checkBounds(MouseEvent e) {   // true if cursor is within bounds
        return bounds.contains(e.getX(), e.getY());
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public void mousePressed(MouseEvent e) {
        if (checkBounds(e)) {
            mousePressed = true;
            if (isRadioButton) {
                for (Button b : radioButtons)
                    if (b != this)
                        b.setMousePressed(false);
                performAction();
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

    public void mouseMoved(MouseEvent e) {
        if (checkBounds(e)) {
            boolean temp = mouseOver;
            mouseOver = true;
            if (temp != mouseOver)
                GameLoop.playSE(Sound.MOUSE_MOVED);
        } else
            mouseOver = false;
    }

    public void resetButton() {
        mouseOver = false;
        mousePressed = false;
    }

    public void performAction() {

    }

    public void createRadioButtons(Button[] radioButtons) {
        isRadioButton = true;
        this.radioButtons = radioButtons;
    }

}
