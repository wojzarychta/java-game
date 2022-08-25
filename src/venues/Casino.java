package venues;

import entities.Player;
import games.CoinFlip;
import main.GameLoop;
import main.GamePanel;
import ui.*;
import ui.Button;
import util.UtilityClass;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import static gamestates.VenueState.VenueSubType.CASINO;
import static main.GamePanel.scaling;
import static main.GamePanel.tileSize;

public class Casino extends Venue {

    private transient final CoinFlip coinFlip = new CoinFlip();
    private transient boolean startAnimation = false;
    private transient GameButton headsButton, tailsButton;
    private transient int energyCost = 5;
    private transient int baseBetAmount = 100;
    private transient int betMultiplier = 1;
    private transient int betAmount = baseBetAmount * betMultiplier;
    private transient IconButton betButton;
    private transient CoinFlip.HeadOrTails chosenSide, outcomeSide;
    private transient final Point coinPos;
    private transient boolean animationOn = false;
    private transient boolean victory = false;
    //private boolean animationEnd = false;

    private transient GameButton amountBaseButton;
    private transient SmallButton[] arrowButtons;
    private transient SmallButton[] betMultiplierButtons;
    private transient AmountSelection amountSelection;

    public Casino(Point location, Player player) {
        super(location, player);
        windowPos = new Point(tileSize * 4, (int) (tileSize * 1.875f));
        windowWidth = (int) (GamePanel.screenWidth - 7.5f * tileSize);
        windowHeight = GamePanel.screenHeight - (int) (2.25f * tileSize);
        coinPos = new Point(windowPos.x + windowWidth / 2 - coinFlip.getCoinDimension().width / 2, windowPos.y + tileSize / 4);
        initializeButtons();
    }

    @Override
    protected void nextTurn(int turn) {

    }

    @Override
    protected void setupButtons() {
        initializeButtons();
    }

    @Override
    protected void help(Graphics2D g) {
        g.setColor(Color.white);
        g.setFont(new Font("Kenney Pixel", Font.PLAIN, (int) (11*scaling)));
        String text = """
                Try your luck in coin toss.

                If you win you double your bet, else you lose your money.""";
        Point p = new Point(windowPos.x + tileSize, windowPos.y + tileSize);
        UtilityClass.drawWrappedText(g, text, p.x, p.y, windowWidth - 2*tileSize, windowHeight - 2*tileSize);
    }

    private float calculateSuccessRate() {
        float successRate;
        int luck = player.getStats().getLuck();
        // algorithm made that starting probability =30% and maximum probability =70%
        successRate = -1.2f / (luck + 2f) + 0.7f;
        return successRate;
    }

    private boolean calculateOutcome() {
        float successRate = calculateSuccessRate();
        Random rand = new Random();
        float randNum = rand.nextFloat();

        return randNum <= successRate;
    }

    private CoinFlip.HeadOrTails determineCoinSide(CoinFlip.HeadOrTails side) {
        CoinFlip.HeadOrTails outcome;
        boolean success = calculateOutcome();
        if (success) {
            if (side == CoinFlip.HeadOrTails.HEADS)
                outcome = CoinFlip.HeadOrTails.HEADS;
            else
                outcome = CoinFlip.HeadOrTails.TAILS;
        } else {
            if (side == CoinFlip.HeadOrTails.HEADS)
                outcome = CoinFlip.HeadOrTails.TAILS;
            else
                outcome = CoinFlip.HeadOrTails.HEADS;
        }
        return outcome;
    }

    public void update() {
        super.update();
        if (!helpOn) {
            tailsButton.update();
            headsButton.update();
            betButton.update();
            if (startAnimation) {
                if (coinFlip.isAnimationEnd()) {
                    coinFlip.setAnimationEnd(false);
                    animationOn = false;
                    updateStats();
                    coinFlip.resetAnimation();
                }
            }
            amountSelection.update();
            betAmount = amountSelection.getAmount();
            updateEnergyCost();
        }
    }


    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
        if (!helpOn) {
            if (!startAnimation) {
                coinFlip.displayBlankCoin(coinPos, g);
            } else {
                if (animationOn)
                    coinFlip.animateCoinFlip(coinPos, g, outcomeSide);
                else {
                    coinFlip.displayFinalFrame(coinPos, g, outcomeSide);
//                headsButton.resetButton();
//                tailsButton.resetButton();
                    //coinFlip.resetAnimation();
                }
            }
            headsButton.draw(g);
            tailsButton.draw(g);
            betButton.draw(g);
            amountSelection.draw(g);
        }
        else
            help(g);
    }

    private void updateEnergyCost(){
        energyCost = (int) (5 - 0.75 * (player.getStats().getStamina()-1));
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
        exitButton.mousePressed(e);
        if (!helpOn) {
            headsButton.mousePressed(e);
            tailsButton.mousePressed(e);
            betButton.mousePressed(e);
            amountSelection.mousePressed(e);
        }
        helpButton.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        exitButton.mouseReleased(e);
        if (!helpOn) {
            headsButton.mouseReleased(e);
            tailsButton.mouseReleased(e);
            betButton.mouseReleased(e);
            amountSelection.mouseReleased(e);
        }
        helpButton.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        exitButton.mouseMoved(e);
        if (!helpOn) {
            tailsButton.mouseMoved(e);
            headsButton.mouseMoved(e);
            betButton.mouseMoved(e);
            amountSelection.mouseMoved(e);
        }
        helpButton.mouseMoved(e);
    }

    @Override
    public void drawPrompt(Graphics2D g) {
        displayPrompt(g, "ENTER CASINO");
    }

    protected void enter() {
        super.enter();
    }

    private void pickSide() {

    }

    public void reset() {
        headsButton.resetButton();
        tailsButton.resetButton();
        animationOn = false;
        startAnimation = false;
        coinFlip.resetAnimation();
    }

    @Override
    public boolean exit() {
        reset();
        return super.exit();
    }

    @Override
    public void loadObject(Venue v) {

    }

    private void updateStats() {
        player.getStats().changeEnergy(-energyCost);
        if (victory)
            player.getStats().changeMoney(betAmount);
        else
            player.getStats().changeMoney(-betAmount);
    }

    private boolean isReady() {
        return (headsButton.isMousePressed() || tailsButton.isMousePressed())
                && player.getStats().isEnergySufficient(energyCost) && player.getStats().isMoneySufficient(betAmount);
    }

    private void changeBetAmount(int direction) { // +1 for positive change and -1 for negative
        int change = 0;
        if (direction == 1) {
            if (baseBetAmount < 10)
                change = 1;
            else if (baseBetAmount < 100)
                change = 10;
            else if (baseBetAmount < 1000)
                change = 100;
        } else if (direction == -1) {
            if (baseBetAmount <= 10)
                change = -1;
            else if (baseBetAmount <= 100)
                change = -10;
            else if (baseBetAmount <= 1000)
                change = -100;
        }

        baseBetAmount += change;
        if (baseBetAmount > 1000)
            baseBetAmount = 1000;
        if (baseBetAmount < 1)
            baseBetAmount = 1;
    }

    public void initializeButtons() {
        setupExitButton(new Point(windowPos.x + windowWidth + tileSize / 2, windowPos.y + tileSize/2));
        setupHelpButton(new Point(windowPos.x + windowWidth + tileSize / 2, (int) (windowPos.y + 1.5f*tileSize)));
        headsButton = new GameButton(new Point(windowPos.x + 9 * tileSize, windowPos.y + (int) (tileSize * 6.5f)), "HEADS");
        tailsButton = new GameButton(new Point(windowPos.x + 11 * tileSize, windowPos.y + (int) (tileSize * 6.5f)), "TAILS");
        tailsButton.createRadioButtons(headsButton);
        headsButton.createRadioButtons(tailsButton);

        betButton = new IconButton(new Point(windowPos.x + windowWidth / 2, windowPos.y + (int) (8.25f * tileSize)), "BET", Integer.toString(energyCost)) {
            @Override
            public void performAction() {
                if (isReady()) {
                    startAnimation = true;
                    if (headsButton.isMousePressed())
                        chosenSide = CoinFlip.HeadOrTails.HEADS;
                    else
                        chosenSide = CoinFlip.HeadOrTails.TAILS;
                    outcomeSide = determineCoinSide(chosenSide);
                    victory = outcomeSide == chosenSide;
                    animationOn = true;
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (isReady())
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

        amountSelection = new AmountSelection(new Point((int) (windowPos.x + 3.75f * tileSize), windowPos.y + (int) (tileSize * 6.375f)));
    }
}
