package ui;

import gamestates.PauseScreen;
import main.GamePanel;
import util.UtilityClass;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class PauseButton {
    private Point screenPos;  // center of button
    private BufferedImage[] buttonImgs;

    private PauseScreen.ActionType action;

    //public enum ButtonState {ACTIVE, HOOVER, PRESSED}

    //private ButtonState buttonState;
    private int index = 0;

    private int width = 2*GamePanel.tileSize;
    private int height = GamePanel.tileSize;

    private boolean mouseOver, mousePressed;

    private Rectangle bounds;

    public PauseButton(Point screenPos, PauseScreen.ActionType action) {
        this.screenPos = screenPos;
        this.action = action;
        loadButtonImgs();
        bounds = new Rectangle(screenPos.x - width/2, screenPos.y - height/2, width, height);
    }

    private void loadButtonImgs(){
        buttonImgs = new BufferedImage[3];
        BufferedImage atlas = UtilityClass.loadSpriteAtlas("/res/gui/GUI.png");
        for (int i = 0; i < buttonImgs.length; i++){
            buttonImgs[i] = atlas.getSubimage(7* GamePanel.defaultTileSize, (5+i)*GamePanel.defaultTileSize, 2*GamePanel.defaultTileSize, GamePanel.defaultTileSize);
        }
    }

    public void draw(Graphics2D g){
        g.drawImage(buttonImgs[index], screenPos.x - width/2, screenPos.y - height/2, width, height, null);
        drawText(g);
    }

    public void update(){
        index = 2;
        if (mouseOver)
            index = 0;
        if (mousePressed)
            index = 1;
    }

    private void drawText(Graphics2D g){
        BufferedImage buttonText = null;
        BufferedImage atlas = UtilityClass.loadSpriteAtlas("/res/gui/GUI.png");
        switch(action){
            case BACK -> {
                buttonText = atlas.getSubimage(0, 8*GamePanel.defaultTileSize, 2*GamePanel.defaultTileSize, GamePanel.defaultTileSize);
            }
            case OPTIONS -> {
                buttonText = atlas.getSubimage(6*GamePanel.defaultTileSize, 10*GamePanel.defaultTileSize, 2*GamePanel.defaultTileSize, GamePanel.defaultTileSize);
            }
            case EXIT -> {
                buttonText = atlas.getSubimage(0, 10*GamePanel.defaultTileSize, 2*GamePanel.defaultTileSize, GamePanel.defaultTileSize);
            }
        }
        g.drawImage(buttonText, screenPos.x - width/2, screenPos.y - height/2, width, height, null);
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public boolean checkBounds(MouseEvent e){   // true if cursor is within bounds
        return bounds.contains(e.getX(), e.getY());
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public PauseScreen.ActionType getAction() {
        return action;
    }
}
