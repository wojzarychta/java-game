package ui;

import main.GameLoop;
import main.GamePanel;

import java.awt.*;

public class UserInterface {

    GamePanel gp;
    Graphics2D g;
    Font font;

    public UserInterface (GamePanel gp){
        this.gp = gp;
        font = new Font("Arial", Font.PLAIN, 40);
    }

    public void draw(Graphics2D g){
//        g.setFont(font);
//        g.setColor(Color.white);
//        g.drawString("cos", 50, 50);
        this.g = g;
        g.setFont(font);
        g.setColor(Color.white);

        switch (GameLoop.getGameState()) {
            case PLAY -> {

            }
            case PAUSE -> {

            }
        }
    }

    private void drawPauseScreen(){
        String text = "PAUSE";
        Point pos;

    }
}
