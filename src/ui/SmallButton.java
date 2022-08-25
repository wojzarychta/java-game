package ui;

import main.GameLoop;
import main.GamePanel;
import util.UtilityClass;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static main.GamePanel.scaling;
import static main.GamePanel.tileSize;

public class SmallButton extends Button {
    private BufferedImage functionImg;
    private boolean isPushButton;

    public enum Function {
        EXIT, RIGHT_ARROW, LEFT_ARROW, TICK_BOX, QUESTION_MARK;
    }

    private String text = null;

    private Function function = null;

    public SmallButton(Point screenPos, Function function) {
        super(screenPos);
        this.function = function;
        loadIconImgs();
    }

    public SmallButton(Point screenPos, String text) {
        super(screenPos);
        this.text = text;
    }


    protected void loadButtonImgs() {
        super.loadButtonImgs();
        for (int i = 0; i < buttonImgs.length; i++) {
            buttonImgs[i] = atlas.getSubimage(i * GamePanel.defaultTileSize, 5 * GamePanel.defaultTileSize, GamePanel.defaultTileSize, GamePanel.defaultTileSize);
        }
    }

    private void loadIconImgs() {
        switch (function) {
            case EXIT ->
                    functionImg = atlas.getSubimage(3 * GamePanel.defaultTileSize, 4 * GamePanel.defaultTileSize, GamePanel.defaultTileSize, GamePanel.defaultTileSize);
            case RIGHT_ARROW -> {
                functionImg = atlas.getSubimage(9 * GamePanel.defaultTileSize, 2 * GamePanel.defaultTileSize, GamePanel.defaultTileSize, GamePanel.defaultTileSize);
            }
            case LEFT_ARROW -> {
                functionImg = atlas.getSubimage(8 * GamePanel.defaultTileSize, 2 * GamePanel.defaultTileSize, GamePanel.defaultTileSize, GamePanel.defaultTileSize);
            }
            case TICK_BOX -> {
                functionImg = atlas.getSubimage(4 * GamePanel.defaultTileSize, 4 * GamePanel.defaultTileSize, GamePanel.defaultTileSize, GamePanel.defaultTileSize);
                isPushButton = true;
            }
            case QUESTION_MARK -> {
                functionImg = atlas.getSubimage(4 * GamePanel.defaultTileSize, 5 * GamePanel.defaultTileSize, GamePanel.defaultTileSize, GamePanel.defaultTileSize);
                isPushButton = true;
            }
        }

    }

    private void drawText(Graphics2D g) {
        g.setColor(Color.white);
        g.setFont(new Font("Kenney Pixel", Font.PLAIN, (int) (9*scaling)));
        Point pos = UtilityClass.centerText(g, text, screenPos);
        if (index == 2 || index == 0)
            g.drawString(text, pos.x, pos.y + height / 10);
        else
            g.drawString(text, pos.x, pos.y + height / 8);
    }

    public void draw(Graphics2D g) {
        super.draw(g);

        if (function == Function.TICK_BOX){
            if (index == 1)
                g.drawImage(functionImg, screenPos.x - width / 2, screenPos.y - height / 2, width, height, null);
        }
        else if (function != null) {
            if (index == 2 || index == 0)
                g.drawImage(functionImg, screenPos.x - width / 2, screenPos.y - height / 2 , width, height, null);
            else
                g.drawImage(functionImg, screenPos.x - width / 2, screenPos.y - height / 2 + tileSize/12, width, height, null);
        }
        if (text != null)
            drawText(g);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isPushButton) {
            if (checkBounds(e)) {
                mousePressed = !mousePressed;
                performAction();
                GameLoop.playSE(Sound.MOUSE_CLICKED);
            }
        }
        else
            super.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!isPushButton)
            super.mouseReleased(e);
    }
}
