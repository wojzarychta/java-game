package ui;

import java.awt.*;
import java.awt.event.MouseEvent;

import static main.GamePanel.tileSize;

public class AmountSelection {
    private Point pos;
    private GameButton amountButton;
    private SmallButton[] arrowButtons;
    private SmallButton[] multiplierButtons;
    private int baseAmount = 100;
    private int multiplier = 1;
    private int amount = 100;

    public AmountSelection(Point pos){
        this.pos = pos;
        setupButtons();
    }

    public void draw(Graphics2D g){
        for (SmallButton sb: arrowButtons)
            sb.draw(g);
        for (SmallButton sb: multiplierButtons)
            sb.draw(g);
        amountButton.draw(g);
    }

    public void update(){
        for (SmallButton sb: arrowButtons)
            sb.update();
        for (SmallButton sb: multiplierButtons)
            sb.update();
        amountButton.update();
        amount = baseAmount * multiplier;
    }

    public void mousePressed(MouseEvent e){
        for (SmallButton sb: arrowButtons)
            sb.mousePressed(e);
        for (SmallButton sb: multiplierButtons)
            sb.mousePressed(e);
    }

    public void mouseReleased(MouseEvent e){
        for (SmallButton sb: arrowButtons)
            sb.mouseReleased(e);
        for (SmallButton sb: multiplierButtons)
            sb.mouseReleased(e);
    }

    public void mouseMoved(MouseEvent e){
        for (SmallButton sb: arrowButtons)
            sb.mouseMoved(e);
        for (SmallButton sb: multiplierButtons)
            sb.mouseMoved(e);
    }

    private void setupButtons(){
        amountButton = new GameButton(new Point((int) (pos.x -0.75 * tileSize), pos.y), Integer.toString(baseAmount)) {
            @Override
            public void update() {
                super.update();
                updateText(Integer.toString(baseAmount));
            }
        };
        arrowButtons = new SmallButton[2];
        arrowButtons[0] = new SmallButton(new Point(pos.x + (int) (-2.25f * tileSize), pos.y), SmallButton.Function.LEFT_ARROW) {
            @Override
            public void performAction() {
                changeAmount(-1);
            }
        };
        arrowButtons[1] = new SmallButton(new Point(pos.x + (int) (0.75f * tileSize), pos.y), SmallButton.Function.RIGHT_ARROW) {
            @Override
            public void performAction() {
                changeAmount(+1);
            }
        };

        multiplierButtons = new SmallButton[3];
        multiplierButtons[0] = new SmallButton(new Point(pos.x + (int) (2.25f * tileSize), pos.y + (int) (-tileSize * 0.9f)), "x 1") {
            @Override
            public void performAction() {
                multiplier = 1;
            }
        };
        multiplierButtons[1] = new SmallButton(new Point(pos.x + (int) (2.25f * tileSize), pos.y), "x 1K") {
            @Override
            public void performAction() {
                multiplier = 1000;
            }
        };
        multiplierButtons[2] = new SmallButton(new Point(pos.x + (int) (2.25f * tileSize), pos.y + (int) (tileSize * 0.9f)), "x 1M") {
            @Override
            public void performAction() {
                multiplier = 1_000_000;
            }
        };
        multiplierButtons[0].setMousePressed(true);
        for (Button b : multiplierButtons)
            b.createRadioButtons(multiplierButtons);

    }

    private void changeAmount(int direction) { // +1 for positive change and -1 for negative
        int change = 0;
        if (direction == 1) {
            if (baseAmount < 10)
                change = 1;
            else if (baseAmount < 100)
                change = 10;
            else if (baseAmount < 1000)
                change = 100;
        } else if (direction == -1) {
            if (baseAmount <= 10)
                change = -1;
            else if (baseAmount <= 100)
                change = -10;
            else if (baseAmount <= 1000)
                change = -100;
        }

        baseAmount += change;
        if (baseAmount > 1000)
            baseAmount = 1000;
        if (baseAmount < 1)
            baseAmount = 1;
    }

    public int getAmount() {
        return amount;
    }
}
