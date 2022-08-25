package games;

import util.UtilityClass;

import java.awt.*;
import java.awt.event.MouseEvent;

import static main.GamePanel.scaling;
import static main.GamePanel.tileSize;

public class NumberTile {

    private final int number;
    private final int sideLength = tileSize;
    private Rectangle bounds;
    private Point position;
    private boolean mousePressed = false, mouseOver = false;
    private Color bgColor = Color.WHITE;
    private boolean enable = false;


    public NumberTile(int number) {
        this.number = number;

    }

    public void update(){
        if (mousePressed)
            bgColor = new Color(21, 150, 12);
        else if (mouseOver)
            bgColor = new Color(195, 195, 195);
        else
            bgColor = Color.white;

    }

    public void drawTile(Graphics2D g) {
        g.setStroke(new BasicStroke(1.25f*scaling));
        if (enable) {
            g.setColor(bgColor);
            g.fillRoundRect(position.x, position.y, sideLength, sideLength, (int) (2.5*scaling), (int) (2.5*scaling));
            g.setColor(Color.BLACK);
            g.drawRoundRect(position.x, position.y, sideLength, sideLength, (int) (4*scaling), (int) (4*scaling));
            drawString(g, String.valueOf(number));
        }
        else {
            g.setColor(Color.white);
            g.fillRoundRect(position.x, position.y, sideLength, sideLength, (int) (4*scaling), (int) (4*scaling));
            //g.setStroke(new BasicStroke(1.5f*scaling));
            g.setColor(Color.BLACK);
            g.drawRoundRect(position.x, position.y, sideLength, sideLength, (int) (4*scaling), (int) (4*scaling));
            drawString(g, "?");
        }

    }

    private void drawString(Graphics2D g, String s) {
        Font font = new Font("Kenney Pixel", Font.BOLD, (int) (12*scaling));
        g.setFont(font);
        g.setColor(Color.BLACK);
        Point p = UtilityClass.centerText(g, s, new Point(position.x + tileSize / 2, position.y + tileSize / 2 + g.getFontMetrics().getHeight() / 3));
        g.drawString(s, p.x, p.y);
    }

    public void updatePosition(Point p) {
        this.position = new Point(p);
        bounds = new Rectangle(p.x, p.y, sideLength, sideLength);
    }

    public void reset(){
        mouseOver = false;
        mousePressed = false;
    }


    public int getSideLength() {
        return sideLength;
    }

    public boolean checkBounds(MouseEvent e) {   // true if cursor is within bounds
        return bounds.contains(e.getX(), e.getY());
    }

    public int mousePressed(MouseEvent e) {
        // checks if tile was pressed and if so returns its number, else returns -1
        if (checkBounds(e)) {
            mousePressed = true;
            return number;
        }
        return -1;
    }

    public void mouseMoved(MouseEvent e) {
        mouseOver = checkBounds(e);
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
