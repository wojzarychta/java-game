package games;

import main.GameLoop;
import ui.Sound;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Random;

import static main.GamePanel.tileSize;

public class NumberOrderMiniGame {

    public final int sideLength;
    private final int spacing = tileSize / 2;
    private final Point position;  // center of the game
    long time = 0;  // time of completion in ms
    long startTime = 0;
    private int nextNumber = 1;
    private final NumberTile[] numberTiles = new NumberTile[9];
    private boolean gameFinished = false;
    private boolean gameOn = false;

    public NumberOrderMiniGame(Point position) {

        Arrays.setAll(numberTiles, i -> new NumberTile(i + 1));
        sideLength = numberTiles[0].getSideLength() * 3 + 2 * spacing;
        this.position = new Point(position.x - sideLength / 2, position.y - sideLength / 2);
        onEnable();
    }

    public void update() {
        for (NumberTile numberTile : numberTiles)
            numberTile.update();
        if (gameOn)
            calculateTime();
    }

    public void start() {
        gameFinished = false;
        gameOn = true;
        startTime = System.currentTimeMillis();
        onEnable();
        for (NumberTile numberTile : numberTiles)
            numberTile.setEnable(true);
    }

    public void onEnable() {
        shuffle();
        for (NumberTile numberTile : numberTiles)
            numberTile.reset();
        Point p = new Point(position);
        for (int i = 0; i < numberTiles.length; i++) {
            numberTiles[i].updatePosition(p);

            if (i % 3 == 2) {
                p.x = position.x;
                p.y += spacing + numberTiles[i].getSideLength();
            } else
                p.x += spacing + numberTiles[i].getSideLength();


        }
    }

    private void shuffle() {
        // Fisher–Yates algorithm
        Random r = new Random();
        for (int i = numberTiles.length - 1; i > 0; i--) {
            int j = r.nextInt(i + 1);
            NumberTile temp = numberTiles[i];
            numberTiles[i] = numberTiles[j];
            numberTiles[j] = temp;
        }
    }

    private void buttonOrder(int pressedNumber) {
        if (pressedNumber == nextNumber) {
            if (nextNumber == 9) {
                nextNumber = 1;
                GameLoop.playSE(Sound.MOUSE_CLICKED);
                finish();
                System.out.println("finish");
            } else
                nextNumber++;
        } else {
            nextNumber = 1;
            onEnable();
        }
    }

    private void drawTiles(Graphics2D g) {

        for (NumberTile numberTile : numberTiles) {
            numberTile.drawTile(g);
            //System.out.println(numberTile.getPosition());
        }
    }

    public void draw(Graphics2D g) {
        drawTiles(g);
    }

    private void finish() {
        gameFinished = true;
        gameOn = false;
        startTime = 0;
    }

    private void calculateTime() {
        time = System.currentTimeMillis() - startTime;
    }

    public void restart() {
        for (NumberTile numberTile : numberTiles)
            numberTile.setEnable(false);
        time = 0;
        gameOn = false;
    }

    public void mousePressed(MouseEvent e) {
        if (gameOn) {
            for (NumberTile numberTile : numberTiles) {
                int numberPressed = numberTile.mousePressed(e);
                if (numberPressed != -1)
                    buttonOrder(numberPressed);
            }
        }
    }


    public void mouseMoved(MouseEvent e) {
        for (NumberTile numberTile : numberTiles)
            numberTile.mouseMoved(e);
    }

    public long getTime() {
        return time;
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    public void setGameFinished(boolean gameFinished) {
        this.gameFinished = gameFinished;
    }
}
