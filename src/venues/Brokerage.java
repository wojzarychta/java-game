package venues;

import entities.Player;
import main.GameLoop;
import main.GamePanel;
import ui.AmountSelection;
import ui.IconButton;
import ui.Sound;
import util.UtilityClass;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Random;

import static main.GamePanel.*;

public class Brokerage extends Venue {

    private final transient double[] sp500 = new double[]{
            -0.13, 0.27, 0.16, 0.29, -0.06, 0.19, 0.10, -0.01, 0.11, 0.30, 0.13, 0.00, 0.13, 0.23, -0.38, 0.04, 0.14, 0.03, 0.09, 0.26, -0.23, -0.13, -0.10, 0.20, 0.27, 0.31, 0.20, 0.34, -0.02, 0.07, 0.04, 0.26, -0.07, 0.27, 0.12, 0.02, 0.15, 0.26, 0.01, 0.17, 0.15, -0.10, 0.26, 0.12, 0.01, -0.12, 0.19, 0.32, -0.30, -0.17, 0.16, 0.11, 0.00, -0.11, 0.08, 0.20, -0.13, 0.09, 0.13, 0.19, -0.12, 0.23, -0.03, 0.08, 0.38, -0.14, 0.03, 0.26, 0.45, -0.07, 0.12, 0.16, 0.22, 0.10, -0.01, 0.00, -0.12, 0.31, 0.14, 0.19, 0.12, -0.18, -0.15, -0.05, 0.25, -0.39, 0.28, 0.41, -0.06, 0.47, -0.15, -0.47, -0.28, -0.12, 0.38
    };
    private Stock[] stocks;
    private transient AmountSelection amountSelection;
    private transient IconButton buyButton;
    private transient IconButton closeButton;
    private transient Stock selectedStock = null;
    private transient Rectangle[] slots;
    private transient int buyCost = 8;
    private transient int closeCost = 6;

    public Brokerage(Point location, Player player) {
        super(location, player);
        windowPos = new Point(tileSize * 3, tileSize * 2);
        windowWidth = GamePanel.screenWidth - 6 * tileSize;
        setupStocks();
        setupButtons();
        createSlots();
    }

    @Override
    protected void nextTurn(int turn) {
        float wisdomBonus = player.getStats().getWisdom()-1;
        Random r = new Random();
        int i = r.nextInt(sp500.length);
        stocks[0].changePrice(sp500[i] + wisdomBonus * 0.01f);
        double change;
        double tslaMaxChange = 0.2;
        if (r.nextFloat() <= 0.66f + wisdomBonus * 0.015f)
            change = tslaMaxChange * r.nextFloat();
        else
            change = -tslaMaxChange * r.nextFloat();
        stocks[1].changePrice(change);
        double btcMaxChange = 0.5;
        if (r.nextFloat() <= 0.55f + wisdomBonus * 0.02f)
            change = btcMaxChange * r.nextFloat();
        else
            change = -btcMaxChange * r.nextFloat();
        stocks[2].changePrice(change);
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
        if (!helpOn) {
            amountSelection.draw(g);
            createTable(g);
            buyButton.draw(g);
            closeButton.draw(g);
        }
        else
            help(g);
    }

    @Override
    public void update() {
        super.update();
        amountSelection.update();
        buyButton.update();
        closeButton.update();
        updateEnergyCost();
    }

    @Override
    public void reset() {
        setupStocks();
    }

    @Override
    public void loadObject(Venue v) {
        this.stocks = ((Brokerage)v).stocks;
        selectedStock = stocks[0];
    }

    private void updateEnergyCost() {
        int baseBuyCost = 8, baseCloseCost = 6;
        buyCost = baseBuyCost - (player.getStats().getStamina()-1);
        if (buyCost < 1)
            buyCost = 1;
        closeCost = baseCloseCost - (player.getStats().getStamina()-1);
        if (closeCost < 1)
            closeCost = 1;
    }

    private void createSlots() {
        slots = new Rectangle[stocks.length];
        Point p = new Point((int) (screenWidth / 2 - 6f * tileSize), (int) (tileSize * 3.375f));
        for (int i = 0; i < slots.length; i++) {
            slots[i] = new Rectangle(p.x, p.y, 12 * tileSize, tileSize);
            p.y += tileSize * 1.25;
        }

    }

    private void buyStock() {
        selectedStock.buy(amountSelection.getAmount());
        player.getStats().changeEnergy(-buyCost);
        player.getStats().changeMoney(-amountSelection.getAmount());
    }

    private void closeTrade() {
        selectedStock.sell(amountSelection.getAmount());
        player.getStats().changeEnergy(-buyCost);
        player.getStats().changeMoney(amountSelection.getAmount());
    }

    private boolean canBuy() {
        return amountSelection.getAmount() <= player.getStats().getMoney() && player.getStats().getEnergy() >= buyCost;
    }

    private boolean canClose() {
        return selectedStock.getValue() >= amountSelection.getAmount() && player.getStats().getEnergy() >= closeCost;
    }

    private void setupStocks() {
        stocks = new Stock[3];
        stocks[0] = new Stock("S&P 500", 1000, 0);
        stocks[1] = new Stock("TSLA", 500, 1);
        stocks[2] = new Stock("BTC", 10_000, 2);
        selectedStock = stocks[0];
    }

    private void createTable(Graphics2D g) {
        g.setColor(Color.white);
        g.setFont(new Font("Kenney Pixel", Font.BOLD, (int) (9*scaling)));

        int[] columns = new int[5];
        int x = (int) (screenWidth / 2 - 5f * tileSize);
        for (int i = 0; i < columns.length; i++) {
            columns[i] = x;
            x += tileSize * 2.35f;
        }
        Point p = new Point(columns[0], (int) (3f * tileSize));

        String text;
        text = "Name";
        p = UtilityClass.centerText(g, text, p);
        g.drawString(text, p.x, p.y);
        text = "Price";
        p.x = columns[1];
        p = UtilityClass.centerText(g, text, p);
        g.drawString(text, p.x, p.y);
        text = "Units";
        p.x = columns[2];
        p = UtilityClass.centerText(g, text, p);
        g.drawString(text, p.x, p.y);
        text = "Invested";
        p.x = columns[3];
        p = UtilityClass.centerText(g, text, p);
        g.drawString(text, p.x, p.y);
        text = "Value";
        p.x = columns[4];
        p = UtilityClass.centerText(g, text, p);
        g.drawString(text, p.x, p.y);

        g.setFont(g.getFont().deriveFont(Font.PLAIN));
        Point p1 = new Point((int) (screenWidth / 2 - 6f * tileSize), (int) (p.y + tileSize * 0.375f));
        for (Stock s : stocks) {
            if (s == selectedStock)
                g.setColor(Color.white);
            else
                g.setColor(Color.gray);
            g.setStroke(new BasicStroke(scaling));
            g.drawRoundRect(p1.x, p1.y, (int) (12f * tileSize), (int) (tileSize), (int) (4*scaling), (int) (4*scaling));
            p.y = (int) (p1.y + 0.6 * tileSize);
            p.x = columns[0];
            text = s.name;
            switch (s.risk) {
                case 0 -> g.setColor(Color.green);
                case 1 -> g.setColor(Color.yellow);
                case 2 -> g.setColor(Color.red);
                default -> throw new IllegalStateException("Unexpected value: " + s.risk);
            }
            p = UtilityClass.centerText(g, text, p);
            g.drawString(text, p.x, p.y);
            g.setColor(Color.white);
            p.x = columns[1];
            text = UtilityClass.formatMoney((int) s.getPrice()) + (s.getChange() >= 0 ? " +" : " ") + String.format("%.2f", s.getChange() * 100) + "%";
            p = UtilityClass.centerText(g, text, p);
            g.drawString(text, p.x, p.y);
            p.x = columns[2];
            text = String.format("%.2f", s.getUnits());
            p = UtilityClass.centerText(g, text, p);
            g.drawString(text, p.x, p.y);
            p.x = columns[3];
            text = UtilityClass.formatMoney(s.getInvested());
            p = UtilityClass.centerText(g, text, p);
            g.drawString(text, p.x, p.y);
            p.x = columns[4];
            text = UtilityClass.formatMoney(s.getValue());
            p = UtilityClass.centerText(g, text, p);
            g.drawString(text, p.x, p.y);


            p1.y += tileSize * 1.25;
        }
    }

    protected void setupButtons() {
        setupExitButton(new Point(windowPos.x + windowWidth + tileSize / 2, windowPos.y + tileSize/2));
        setupHelpButton(new Point(windowPos.x + windowWidth + tileSize / 2, (int) (windowPos.y + 1.5f*tileSize)));
        amountSelection = new AmountSelection(new Point((int) (screenWidth / 2 - 2.5 * tileSize), screenHeight / 2 + 3 * tileSize));
        buyButton = new IconButton(new Point((int) (screenWidth / 2 + windowWidth / 4), (int) (screenHeight / 2 + 2.25f * tileSize)), "BUY", String.valueOf(buyCost)) {
            @Override
            public void performAction() {
                if (canBuy())
                    buyStock();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (canBuy())
                    super.mousePressed(e);
                else if (checkBounds(e))
                    GameLoop.playSE(Sound.ERROR_SE);
            }
            @Override
            public void update() {
                super.update();
                updateText(Integer.toString(buyCost));
            }
        };
        closeButton = new IconButton(new Point((int) (screenWidth / 2 + windowWidth / 4), (int) (screenHeight / 2 + 3.75f * tileSize)), "CLOSE", String.valueOf(closeCost)) {
            @Override
            public void performAction() {
                if (canClose())
                    closeTrade();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (canClose())
                    super.mousePressed(e);
                else if (checkBounds(e))
                    GameLoop.playSE(Sound.ERROR_SE);
            }
            @Override
            public void update() {
                super.update();
                updateText(Integer.toString(closeCost));
            }
        };

    }

    @Override
    protected void help(Graphics2D g) {
        g.setColor(Color.white);
        g.setFont(new Font("Kenney Pixel", Font.PLAIN, (int) (9*scaling)));
        String text = """
                Try your best investing in stocks!
                
                You have 3 stocks to choose, each with different risk level (green - low risk, red - high risk).                
                You can pick one and invest certain amount of money to buy some of the stock.
                
                Bought units are calculated as 'money'/'price'. You can also see how much money you invested and how much is this money worth at the moment (which is calculated 'units'*'price')
                                
                At the end of each turn price of the stocks changes hence value of your stocks alters as well. You can see percentage change of price next to price of stock.""";
        Point p = new Point(windowPos.x + tileSize, windowPos.y + tileSize);
        UtilityClass.drawWrappedText(g, text, p.x, p.y, windowWidth - 2*tileSize, windowHeight - 2*tileSize);
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
        helpButton.mousePressed(e);
        if (!helpOn) {
            amountSelection.mousePressed(e);
            buyButton.mousePressed(e);
            closeButton.mousePressed(e);
            for (int i = 0; i < slots.length; i++) {
                if (slots[i].contains(e.getX(), e.getY())) {
                    selectedStock = stocks[i];
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        exitButton.mouseReleased(e);
        helpButton.mouseReleased(e);
        if (!helpOn) {
            amountSelection.mouseReleased(e);
            buyButton.mouseReleased(e);
            closeButton.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        exitButton.mouseMoved(e);
        helpButton.mouseMoved(e);
        if (!helpOn) {
            amountSelection.mouseMoved(e);
            buyButton.mouseMoved(e);
            closeButton.mouseMoved(e);
        }
    }

    @Override
    public void drawPrompt(Graphics2D g) {
        displayPrompt(g, "BROKERAGE");
    }
}
