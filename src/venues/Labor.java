package venues;

import entities.Player;
import games.NumberOrderMiniGame;
import main.GameLoop;
import main.GamePanel;
import ui.IconButton;
import ui.Sound;
import util.UtilityClass;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static main.GamePanel.*;

public class Labor extends Venue {

    private transient final NumberOrderMiniGame miniGame = new NumberOrderMiniGame(new Point(GamePanel.screenWidth / 2, GamePanel.screenHeight / 2 - tileSize));
    private transient IconButton startButton;
    private transient int energyCost = 20;
    private transient int baseEnergyCost = 20;
    private int baseSalary = 5_000;
    private transient int prize = baseSalary;
    private transient boolean gameOn = false;

    public Labor(Point location, Player player) {
        super(location, player);
        windowPos = new Point(tileSize * 5, tileSize * 2);
        windowWidth = GamePanel.screenWidth - 10 * tileSize;
        setupButtons();
    }

    @Override
    protected void nextTurn(int turn) {
        baseSalary += turn * 2500;
    }

    @Override
    protected void setupButtons() {
        setupExitButton(new Point(windowPos.x + windowWidth + tileSize / 2, windowPos.y + tileSize / 2));
        setupHelpButton(new Point(windowPos.x + windowWidth + tileSize / 2, (int) (windowPos.y + 1.5f * tileSize)));
        startButton = new IconButton(new Point(GamePanel.screenWidth / 2, GamePanel.screenHeight / 2 + 4 * tileSize), "PLAY", Integer.toString(energyCost)) {
            @Override
            public void performAction() {
                startGame();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (canPlay())
                    super.mousePressed(e);
                else if (checkBounds(e))
                    GameLoop.playSE(Sound.ERROR_SE);
            }

            @Override
            public void update() {
                super.update();
                updateText(Integer.toString(energyCost));
            }
        };
    }

    @Override
    protected void help(Graphics2D g) {
        g.setColor(Color.white);
        g.setFont(new Font("Kenney Pixel", Font.PLAIN, (int) (11 * scaling)));
        String text = """
                Labor is risk-free option to earn some money, however it is costly when it comes to energy.
                                
                Try to press tiles in numerical order as fast as you can!
                Keep in mind with every second your prize shrinks.""";
        Point p = new Point(windowPos.x + tileSize, windowPos.y + tileSize);
        UtilityClass.drawWrappedText(g, text, p.x, p.y, windowWidth - 2 * tileSize, windowHeight - 2 * tileSize);
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
        if (!helpOn) {
            miniGame.draw(g);
            startButton.draw(g);
            displayPrize(g);
        } else
            help(g);
    }

    @Override
    public void update() {
        super.update();
        if (!helpOn) {
            miniGame.update();
            startButton.update();
            if (miniGame.isGameFinished()) {
                //calculatePrize();
                gameOn = false;
                miniGame.setGameFinished(false);
                cashPrize();
            }
            calculatePrize();
            updateEnergyCost();
        }
    }

    @Override
    public void reset() {
        baseSalary = 5_000;
        gameOn = false;
    }

    private void updateEnergyCost() {
        energyCost = baseEnergyCost - (player.getStats().getStamina() - 1);
        if (energyCost < 1)
            energyCost = 1;
    }

    private void startGame() {
        gameOn = true;
        miniGame.start();
        player.getStats().changeEnergy(-energyCost);
    }

    private void displayPrize(Graphics2D g) {
        Font font = new Font("Kenney Pixel", Font.BOLD, (int) (12 * scaling));
        g.setFont(font);
        g.setColor(Color.WHITE);
        String prize = UtilityClass.formatMoney(this.prize);
        Point p = UtilityClass.centerText(g, prize, new Point(screenWidth / 2 + tileSize, (int) (8.5 * tileSize)));
        g.drawString("PRIZE:", screenWidth / 2 - 2 * tileSize, (int) (8.5 * tileSize));
        g.drawString(prize, p.x, p.y);
    }

    private void calculatePrize() {
        prize = (int) ((int) (baseSalary - 0.02 * baseSalary * miniGame.getTime() / 1000) + (player.getStats().getCharisma() - 1) * baseSalary * 0.2f);
        if (prize < 0) {
            prize = 0;
            gameOn = false;
            miniGame.restart();
        }
    }

    private void cashPrize() {
        player.getStats().changeMoney(prize);
    }

    private boolean canPlay() {
        return player.getStats().getEnergy() >= energyCost && !gameOn;
    }

    @Override
    public boolean exit() {
        miniGame.restart();
        gameOn = false;
        return super.exit();
    }

    @Override
    public void loadObject(Venue v) {
        this.baseSalary = ((Labor) v).baseSalary;
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
        if (!helpOn) {
            miniGame.mousePressed(e);
            startButton.mousePressed(e);
        }
        exitButton.mousePressed(e);
        helpButton.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //miniGame.mouseReleased(e);
        if (!helpOn)
            startButton.mouseReleased(e);
        exitButton.mouseReleased(e);
        helpButton.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!helpOn) {
            miniGame.mouseMoved(e);
            startButton.mouseMoved(e);
        }
        exitButton.mouseMoved(e);
        helpButton.mouseMoved(e);
    }

    @Override
    public void drawPrompt(Graphics2D g) {
        displayPrompt(g, "GO TO WORK");
    }

}
