package gamestates;

import main.GameLoop;
import main.GamePanel;
import main.GameWindow;
import ui.GameButton;
import util.GameConfiguration;
import util.UtilityClass;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static main.GamePanel.*;
import static main.GamePanel.tileSize;

public class GameOver implements GameStateMethods {
    private int money = 0;
    private final GameButton exitButton;
    private GameLoop gl;
    public GameOver(GameLoop gl){
        this.gl = gl;
        exitButton = new GameButton(new Point(screenWidth/2, (int) (screenHeight*0.85f)), "MAIN MENU", (int) (3.5*tileSize), (int) (13*scaling)){
            @Override
            public void performAction() {
                GameLoop.setGameState(GameLoop.gameStateType.MAIN_MENU);
                gl.getPlayState().restartGame();
                GameConfiguration.eraseConfig();
            }
        };
    }

    @Override
    public void update() {
        exitButton.update();
        money = gl.getPlayState().getPlayer().getStats().getMoney();
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(new Color(183, 1, 1));
        g.setFont(new Font("Kenney Pixel", Font.BOLD, (int) (50*scaling)));
        String s = "GAME";
        Point p = UtilityClass.centerText(g, s, new Point(GamePanel.screenWidth / 2, (int) (screenHeight*0.2f)));
        g.drawString(s, p.x, p.y);
        s = "OVER";
        p = UtilityClass.centerText(g, s, new Point(GamePanel.screenWidth / 2, (int) (screenHeight*0.35f)));
        g.drawString(s, p.x, p.y);
        g.setColor(Color.white);
        g.setFont(g.getFont().deriveFont(24*scaling));
        s = "You earned:";
        p = UtilityClass.centerText(g, s, new Point(GamePanel.screenWidth / 2, (int) (screenHeight*0.55f)));
        g.drawString(s, p.x, p.y);
        s = UtilityClass.formatMoney(money);
        p = UtilityClass.centerText(g, s, new Point(GamePanel.screenWidth / 2, (int) (screenHeight*0.65f)));
        g.drawString(s, p.x, p.y);
        BufferedImage coinImg = UtilityClass.loadImage("/res/items/Coin.png");
        g.drawImage(coinImg, p.x + g.getFontMetrics().stringWidth(s) + tileSize / 8, (int) (p.y - g.getFontMetrics().getHeight()*0.8f), g.getFontMetrics().getHeight(), g.getFontMetrics().getHeight(), null);

        exitButton.draw(g);
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        exitButton.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        exitButton.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        exitButton.mouseMoved(e);
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
