package venues;

import gamestates.PlayState;
import main.GameLoop;
import main.GamePanel;
import ui.AmountSelection;
import ui.GameButton;
import ui.IconButton;
import ui.Sound;
import util.UtilityClass;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static main.GamePanel.*;

public class Bank extends Venue {

    private transient final GameButton[] withdrawDepositButtons = new GameButton[2];
    private transient AmountSelection amountSelection;
    private transient IconButton executeButton;
    private int balance;
    private int operationLimit = 10_000;
    private transient int baseEnergyCost = 8;
    private transient int energyCost = 8;
    private transient final double tax = 0.19;
    private transient double rate = 0.05;

    public Bank(Point location, PlayState playState) {
        super(location, playState);
        windowPos = new Point((int) (tileSize * 3.5f), tileSize * 2);
        windowWidth = GamePanel.screenWidth - 7 * tileSize;
        windowHeight = GamePanel.screenHeight - (int) (2.5f * tileSize);
        setupButtons();
    }

    @Override
    protected void nextTurn(int turn) {
        operationLimit += turn * 5000;
        balance *= (1 + rate * (1 - tax));
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
        if (!helpOn) {
            amountSelection.draw(g);
            for (GameButton gb : withdrawDepositButtons)
                gb.draw(g);
            executeButton.draw(g);
            drawBalance(g);
            drawInformation(g);
        }
        else{
            help(g);
        }
    }

    @Override
    public void update() {
        super.update();
        amountSelection.update();
        for (GameButton gb : withdrawDepositButtons)
            gb.update();
        executeButton.update();
        if (balance >= 10_000_000)
            rate = 0.08;
        else if (balance >= 1_000_000)
            rate=0.07;
        else if (balance >= 100_000)
            rate = 0.06;
        else
            rate = 0.05;
        rate += 0.01 * (player.getStats().getCharisma()-1);
        updateEnergyCost();
    }

    @Override
    public void reset() {
        operationLimit = 10_000;
        balance = 0;

    }

    @Override
    public void loadObject(Venue v) {
        this.balance = ((Bank)v).getBalance();
        this.operationLimit = ((Bank)v).getOperationLimit();
    }

    private void updateEnergyCost(){
        energyCost = (int) (baseEnergyCost - 0.75 * (player.getStats().getStamina()-1));
        if (energyCost < 1)
            energyCost = 1;
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
        helpButton.mousePressed(e);
        exitButton.mousePressed(e);
        if (!helpOn) {
            amountSelection.mousePressed(e);
            for (GameButton gb : withdrawDepositButtons)
                gb.mousePressed(e);
            executeButton.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        helpButton.mouseReleased(e);
        exitButton.mouseReleased(e);
        if (!helpOn) {
            amountSelection.mouseReleased(e);
            for (GameButton gb : withdrawDepositButtons)
                gb.mouseReleased(e);
            executeButton.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        helpButton.mouseMoved(e);
        exitButton.mouseMoved(e);
        if (!helpOn) {
            amountSelection.mouseMoved(e);
            for (GameButton gb : withdrawDepositButtons)
                gb.mouseMoved(e);
            executeButton.mouseMoved(e);
        }
    }

    @Override
    public void drawPrompt(Graphics2D g) {
        displayPrompt(g, "ENTER BANK");
    }

    protected void setupButtons() {
        setupExitButton(new Point(windowPos.x + windowWidth + tileSize / 2, windowPos.y + tileSize/2));
        setupHelpButton(new Point(windowPos.x + windowWidth + tileSize / 2, (int) (windowPos.y + 1.5f*tileSize)));
        withdrawDepositButtons[0] = new GameButton(new Point((int) (screenWidth / 2 + 1.75f * tileSize), screenHeight / 2 + 2 * tileSize), "WITHDRAW", 8*scaling);
        withdrawDepositButtons[1] = new GameButton(new Point((int) (screenWidth / 2 + 4 * tileSize), screenHeight / 2 + 2 * tileSize), "DEPOSIT", 9*scaling);
        withdrawDepositButtons[1].setMousePressed(true);
        for (GameButton gb : withdrawDepositButtons)
            gb.createRadioButtons(withdrawDepositButtons);
        amountSelection = new AmountSelection(new Point((int) (screenWidth / 2 - 3 * tileSize), screenHeight / 2 + 2 * tileSize));
        executeButton = new IconButton(new Point(screenWidth / 2, (int) (screenHeight / 2 + 4.25 * tileSize)), "EXECUTE", Integer.toString(energyCost), (int) (9*scaling)) {
            @Override
            public void performAction() {
                super.performAction();
                bankingTransaction();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (isTransactionPossible())
                    super.mouseMoved(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (isTransactionPossible())
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
        g.setFont(new Font("Kenney Pixel", Font.PLAIN, (int) (11*scaling)));
        String text = "Interest rate - reward for saving paid by bank.\n\n" +
                "Interest tax - tax on interest deducted from your income.\n" +
                "As a result, real interest totals: \n'interest rate' * (1 - 'interest tax')\n" +
                "Such system is present in every contemporary bank.\n\n" +
                "Operation limit - max limit of single transaction.";
        Point p = new Point(windowPos.x + tileSize, windowPos.y + tileSize);
        UtilityClass.drawWrappedText(g, text, p.x, p.y, windowWidth - 2*tileSize, windowHeight - 2*tileSize);
    }

    private void bankingTransaction() {
        if (!isTransactionPossible())
            return;
        int amount = amountSelection.getAmount();
        if (withdrawDepositButtons[0].isMousePressed()) {
            player.getStats().changeMoney(amount);
            balance -= amount;
        } else if (withdrawDepositButtons[1].isMousePressed()) {
            player.getStats().changeMoney(-amount);
            balance += amount;
        }
        player.getStats().changeEnergy(-energyCost);
    }

    private boolean isTransactionPossible() {
        int amount = amountSelection.getAmount();
        if (withdrawDepositButtons[0].isMousePressed()) {
            return amount <= operationLimit && player.getStats().getEnergy() >= energyCost && balance >= amount;
        } else if (withdrawDepositButtons[1].isMousePressed()) {
            return amount <= operationLimit && player.getStats().getEnergy() >= energyCost && player.getStats().getMoney() >= amount;
        }
        return false;
    }

    private void drawBalance(Graphics2D g) {
        Point p = new Point((int) (screenWidth / 2 - 5 * tileSize), (int) (3.25f * tileSize));
        String text = "BALANCE:";
        g.setColor(Color.white);
        g.setFont(new Font("Kenney Pixel", Font.BOLD, (int) (13*scaling)));
        g.drawString(text, p.x, p.y);
        g.setFont(g.getFont().deriveFont(24*scaling));
        p.y += g.getFontMetrics().getHeight() + tileSize / 2;
        g.drawString(UtilityClass.formatMoney(balance), p.x, p.y);
    }

    private void drawInformation(Graphics2D g) {
        Point p = new Point((int) (screenWidth / 2 + 5f * tileSize), (int) (3.25f * tileSize));
        String text = "INTEREST RATE:";
        g.setColor(Color.white);
        g.setFont(new Font("Kenney Pixel", Font.PLAIN, (int) (9*scaling)));
        Point p1 = UtilityClass.rightAlign(g, text, p);
        g.drawString(text, p1.x, p1.y);
        p.y += g.getFontMetrics().getHeight();
        text = String.format("%.1f", rate * 100 ) + "%";
        p1 = UtilityClass.rightAlign(g, text, p);
        //g.setFont(g.getFont().deriveFont(56f));
        g.drawString(text, p1.x, p1.y);
        p.y += tileSize * 0.75f;
        text = "INTEREST TAX:";
        p1 = UtilityClass.rightAlign(g, text, p);
        g.drawString(text, p1.x, p1.y);
        p.y += g.getFontMetrics().getHeight();
        text = tax * 100 + "%";
        p1 = UtilityClass.rightAlign(g, text, p);
        g.drawString(text, p1.x, p1.y);
        p.y += tileSize * 0.75f;
        text = "TRANSACTION LIMIT:";
        p1 = UtilityClass.rightAlign(g, text, p);
        g.drawString(text, p1.x, p1.y);
        p.y += g.getFontMetrics().getHeight();
        text = UtilityClass.formatMoney(operationLimit);
        p1 = UtilityClass.rightAlign(g, text, p);
        g.drawString(text, p1.x, p1.y);
    }

    public int getBalance() {
        return balance;
    }

    public int getOperationLimit() {
        return operationLimit;
    }
}
