package ui;

import main.GamePanel;
import util.UtilityClass;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static main.GamePanel.scaling;
import static main.GamePanel.tileSize;

public class IconButton extends Button {

    private BufferedImage iconImg;
    private String text, num;
    private int fontSize = (int) (10*scaling);

    public IconButton(Point screenPos, String text1, String text2) {
        super(screenPos);
        this.text = text1;
        this.num = text2;
        loadIconImg();
    }

    public IconButton(Point screenPos, String text1, String text2, int fontSize) {
        super(screenPos);
        this.text = text1;
        this.num = text2;
        loadIconImg();
        this.fontSize = fontSize;
    }

    @Override
    protected void setDimensions() {
        width = 3 * tileSize;
        height = tileSize;
    }

    @Override
    protected void loadButtonImgs() {
        super.loadButtonImgs();
        BufferedImage img;
        Graphics2D g;
        for (int i = 0; i < buttonImgs.length; i++) {
            buttonImgs[i] = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            g = buttonImgs[i].createGraphics();
            img = atlas.getSubimage(9 * GamePanel.defaultTileSize, (5 + i) * GamePanel.defaultTileSize, 3 * GamePanel.defaultTileSize, GamePanel.defaultTileSize);
            g.drawImage(img, 0, 0, 3 * tileSize, tileSize, null);
            img = atlas.getSubimage(7 * GamePanel.defaultTileSize, (5 + i) * GamePanel.defaultTileSize, 2 * GamePanel.defaultTileSize, GamePanel.defaultTileSize);
            g.drawImage(img, 0, 0, 2 * tileSize, tileSize, null);
        }
    }

    private void loadIconImg() {
        iconImg = new BufferedImage(3*tileSize / 5, 3*tileSize / 5, BufferedImage.TYPE_INT_ARGB);
        BufferedImage img;
        Graphics2D g;
        img = UtilityClass.loadSpriteAtlas("/res/icons/bolt_icon.png");
        g = iconImg.createGraphics();
        g.drawImage(img, 0, 0, iconImg.getWidth(), iconImg.getHeight(), null);
    }

    private void drawText(Graphics2D g) {
        g.setColor(Color.white);
        g.setFont(new Font("Kenney Pixel", Font.BOLD, fontSize));
        Point pos1 = UtilityClass.centerText(g, text, new Point(screenPos.x - width / 6, screenPos.y));
        Point pos2 = UtilityClass.centerText(g, num, new Point((int) (screenPos.x + 0.375f*width), screenPos.y));
        if (index == 2 || index == 0){
            g.drawString(text, pos1.x, pos1.y + height / 10);
        }
        else{
            g.drawString(text, pos1.x, pos1.y + height / 8);
        }
        g.setFont(g.getFont().deriveFont(9*scaling));
        if (index == 2 || index == 0){
            g.drawString(num, pos2.x, pos2.y + height / 10);
        }
        else{
            g.drawString(num, pos2.x, pos2.y + height / 8);
        }
    }

    private void drawIcon(Graphics2D g) {
        if (index == 2 || index == 0)
            g.drawImage(iconImg, screenPos.x + width/8 , screenPos.y - height / 3, null);
        else
            g.drawImage(iconImg, screenPos.x + width/8 , screenPos.y - 7*height / 24, null);

    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
        drawText(g);
        drawIcon(g);
    }

    public void mouseReleased(MouseEvent e) {
        if (mousePressed) {
            mousePressed = false;
            performAction();
        }
    }

    public void updateText(String text) {
        this.num = text;
    }
}
