package venues;

import entities.Player;
import main.GameLoop;
import main.GamePanel;
import objects.*;
import ui.GameButton;
import ui.IconButton;
import ui.Sound;
import util.UtilityClass;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static main.GamePanel.*;

public class Market extends Venue {

    private transient final Point secondWindowPos;
    private transient final int inventoryWidth;
    private transient final int BUYING = 0, SELLING = 1;
    private transient final int baseEnergyCost = 6;
    private ArrayList<Item> merch = new ArrayList<>();
    private transient ArrayList<Item> inventory = new ArrayList<>();
    private transient GameButton[] buyButtons;
    private transient GameButton[] sellButtons;
    private transient GameButton[] amountSelection;
    private transient int quantity = 1;
    private transient Item selectedItem;
    private transient int action = -1; // 0 for buying, 1 for selling
    private transient int energyCost = 6;
    private transient IconButton confirmButton;

    public Market(Point location, Player player) {
        super(location, player);
        windowPos = new Point((int) (tileSize * 3f), tileSize * 2);
        secondWindowPos = new Point(screenWidth / 2 + tileSize * 3, tileSize * 2);
        windowWidth = (int) (GamePanel.screenWidth / 2 - 0.5f * tileSize);
        inventoryWidth = (int) (GamePanel.screenWidth / 2 - 6.5f * tileSize);
        createInventory();
        setPrices();
        setupButtons();
    }

    @Override
    protected void nextTurn(int turn) {
        selectedItem = null;
        setPrices();
    }

    @Override
    public void draw(Graphics2D g) {
        if (!helpOn) {
            drawWindows(g);
            drawMerch(g);
            for (GameButton gb : buyButtons) {
                gb.draw(g);
            }
            for (GameButton gb : sellButtons) {
                gb.draw(g);
            }
            if (selectedItem != null)
                drawTransactionScreen(g);
        }
        else
            help(g);
        exitButton.draw(g);
        helpButton.draw(g);
    }

    @Override
    public void update() {
        super.update();
        exitButton.update();
        helpButton.update();
        for (GameButton gb : sellButtons) {
            gb.update();
        }
        for (GameButton gb : buyButtons) {
            gb.update();
        }
        for (GameButton gb : amountSelection) {
            gb.update();
        }
        confirmButton.update();
        if (selectedItem != null)
            updateEnergyCost();
    }

    @Override
    public void reset() {
        setPrices();
    }

    private void setPrices() {
        Random random = new Random();
        float maxChange = 0.05f;
        for (Item i : merch) {
            float multiplier = random.nextFloat();
            float change = maxChange * (2 * multiplier - 1);
            i.setPrice((int) (i.getPrice() * (1 + change)));
            i.setPrice((int) (i.getPrice() * 1.2f));
        }

    }

    private void drawWindows(Graphics2D g) {
        drawWindow(windowPos, windowWidth, windowHeight, g);
        drawWindow(secondWindowPos, inventoryWidth, 6 * tileSize, g);
        drawInventory(g);
    }

    private void createInventory() {
        merch.add(new Diamond());
        merch.add(new Gold());
        merch.add(new Emerald());
        merch.add(new Ruby());
        merch.add(new Pearl());

    }

    private void buy() {
        canBuy();
        int amount = quantity * selectedItem.getPrice();
        player.getStats().changeMoney(-amount);
        player.getStats().changeEnergy(-energyCost);
        player.addToInventory(selectedItem, quantity);

    }

    private void sell() {
        int amount = quantity * selectedItem.getPrice();
        player.getStats().changeMoney(amount);
        player.getStats().changeEnergy(-energyCost);
        player.removeFromInventory(selectedItem, quantity);
    }

    private boolean canBuy() {
        int inventorySpace = 0;
        for (int k = 0; k < player.getInventoryLength(); k++) {
            if (k < inventory.size()) {
                Item i = inventory.get(k);
                if (i.getClass().equals(selectedItem.getClass())) {
                    inventorySpace += i.getMaxStack() - i.getQuantity();
                }
            } else
                inventorySpace += selectedItem.getMaxStack();
        }
        return player.getStats().getEnergy() >= energyCost &&
                player.getStats().getMoney() >= quantity * selectedItem.getPrice() &&
                inventorySpace >= quantity;
    }

    private boolean canSell() {
        int itemsCount = 0;
        for (Item i : inventory)
            if (i.getClass().equals(selectedItem.getClass()))
                itemsCount += i.getQuantity();
        return player.getStats().getEnergy() >= energyCost &&
                itemsCount >= quantity;
    }

    private void drawTransactionScreen(Graphics2D g) {
        for (GameButton gb : amountSelection) {
            gb.draw(g);
        }
        //
        int amount = quantity * selectedItem.getPrice();
        //
        if ((action == BUYING && !canBuy()) || (action == SELLING && !canSell()))
            g.setColor(Color.red);
        else
            g.setColor(Color.white);
        String text = (action == BUYING ? "Buying " : "Selling ") + quantity + " unit/s of " + selectedItem.getName();
        Point p = new Point((int) (7.5f * tileSize), (int) (7.625f * tileSize));
        p = UtilityClass.centerText(g, text, p);
        g.setFont(new Font("Kenney Pixel", Font.PLAIN, (int) (8 * scaling)));
        g.drawString(text, p.x, p.y);
        text = (action == BUYING ? "- " : "+ ") + UtilityClass.formatMoney(amount);
        p = new Point((int) (7.5f * tileSize), (int) (9.125f * tileSize));
        BufferedImage coinImg = UtilityClass.loadImage("/res/items/Coin.png");
        p = UtilityClass.centerText(g, text, p);
        g.drawImage(coinImg, p.x + g.getFontMetrics().stringWidth(text) + tileSize / 8, p.y - g.getFontMetrics().getHeight(), tileSize / 2, tileSize / 2, null);
        g.drawString(text, p.x, p.y);
        confirmButton.draw(g);
    }

    private void drawMerch(Graphics2D g) {
        Point defaultPos = new Point((int) (tileSize * 3.25f), (int) (tileSize * 3.125f));
        Point p = new Point((int) (tileSize * 4.5f), (int) (tileSize * 2.75f));
        String text;
        g.setColor(Color.white);
        g.setFont(new Font("Kenney Pixel", Font.BOLD, (int) (9 * scaling)));
        text = "Item";
        g.drawString(text, p.x, p.y);
        g.drawLine(p.x, (int) (p.y + 2.5 * scaling), p.x + g.getFontMetrics().stringWidth(text), (int) (p.y + 2.5 * scaling));
        p.x += tileSize * 3.25;
        text = "Price";
        g.drawString(text, UtilityClass.centerText(g, text, p).x, p.y);
        g.drawLine(UtilityClass.centerText(g, text, p).x, (int) (p.y + 2.5 * scaling), UtilityClass.centerText(g, text, p).x + g.getFontMetrics().stringWidth(text), (int) (p.y + 2.5 * scaling));
        p = new Point(defaultPos);
        for (Item i : merch) {
            g.drawImage(i.getImage(), p.x, p.y, (int) (tileSize * 0.75f), (int) (tileSize * 0.75f), null);
            p.x += tileSize * 1.25f;
            p.y += g.getFontMetrics().getHeight() * 1.15f;
            text = i.getName();
            g.drawString(text, p.x, p.y);
            p.x += tileSize * 3.25;
            text = UtilityClass.formatMoney(i.getPrice());
            g.drawString(text, UtilityClass.centerText(g, text, p).x, p.y);

            p.x = defaultPos.x;
            p.y += 0.35f * tileSize;
        }
    }

    private void drawInventory(Graphics2D g) {
        Point p = new Point(secondWindowPos.x + inventoryWidth / 2, (int) (tileSize * 2.75f));
        String text = "INVENTORY";
        g.setColor(Color.white);
        g.setFont(new Font("Kenney Pixel", Font.BOLD, (int) (9 * scaling)));
        g.drawString(text, UtilityClass.centerText(g, text, p).x, p.y);
        Point defaultSlotPos = new Point((int) (secondWindowPos.x + tileSize * 0.5f), (int) (tileSize * 3.25f));
        p = new Point(defaultSlotPos);
        inventory = player.getInventory();
        for (int i = 0; i < player.getInventoryLength(); i++) {
            g.setColor(new Color(58, 68, 102));
            g.fillRect(p.x, p.y, tileSize, tileSize);
            if (i <= inventory.size() - 1) {
                g.drawImage(inventory.get(i).getImage(), p.x, p.y, null);
                String q = String.valueOf(inventory.get(i).getQuantity());
                g.setColor(Color.white);
                g.drawString(q, UtilityClass.rightAlign(g, q, new Point(p.x + tileSize, p.y + tileSize)).x, p.y + tileSize);
            }
            p.x += 1.5f * tileSize;
            if (i % 2 == 1) {
                p.x = defaultSlotPos.x;
                p.y += 1.5f * tileSize;
            }
        }

    }

    private void changeQuantity(int i) {

        if (i == 999) { //max
            quantity = 0;
            if (action == BUYING)
                while (canBuy()) {
                    quantity++;
                    updateEnergyCost();
                }
            if (action == SELLING)
                while (canSell()) {
                    quantity++;
                    updateEnergyCost();
                }
            quantity--;
        } else
            quantity += i;
        if (quantity <= 0 || i == 0)
            quantity = 1;

    }

    private void updateEnergyCost() {
        energyCost = (int) (baseEnergyCost - 0.5f * player.getStats().getStamina() + 1);
        if (energyCost < 1)
            energyCost = 1;
        energyCost = (int) (energyCost * 0.5 * Math.ceil((double) quantity / (double) selectedItem.getMaxStack()));
    }

    @Override
    public boolean exit() {
        selectedItem = null;
        quantity = 1;
        return super.exit();
    }

    @Override
    public void loadObject(Venue v) {
        this.merch = ((Market) v).merch;
    }

    protected void setupButtons() {
        setupExitButton(new Point((int) (secondWindowPos.x + inventoryWidth + 0.5f * tileSize), windowPos.y + tileSize / 2));
        setupHelpButton(new Point((int) (secondWindowPos.x + inventoryWidth + 0.5f * tileSize), (int) (windowPos.y + 1.5f * tileSize)));
        buyButtons = new GameButton[merch.size()];
        sellButtons = new GameButton[merch.size()];
        for (int i = 0; i < merch.size(); i++) {
            buyButtons[i] = new GameButton(new Point(tileSize * 10, (int) (tileSize * 3.5f + i * 0.85 * tileSize)), "BUY", (int) (1.5f * tileSize)) {
                @Override
                public void performAction() {
                    int i = Arrays.asList(buyButtons).indexOf(this);
                    selectedItem = merch.get(i);
                    action = BUYING;
                    quantity = 1;
                }
            };
            sellButtons[i] = new GameButton(new Point((int) (tileSize * 11.5), (int) (tileSize * 3.5f + i * 0.85 * tileSize)), "SELL", (int) (1.5f * tileSize)) {
                @Override
                public void performAction() {
                    int i = Arrays.asList(sellButtons).indexOf(this);
                    selectedItem = merch.get(i);
                    action = SELLING;
                    quantity = 1;
                }
            };
        }
        amountSelection = new GameButton[6];
        String[] text = {"MIN", "-10", "-1", "+1", "+10", "MAX"};
        int[] q = {0, -10, -1, 1, 10, 999};
        for (int i = 0; i < amountSelection.length; i++) {
            int finalI = i;
            amountSelection[i] = new GameButton(new Point((int) (tileSize * (4.5 + finalI * 1.25)), (int) (tileSize * 8.25f)), text[finalI], (int) (1.25f * tileSize)) {
                @Override
                public void performAction() {
                    changeQuantity(q[finalI]);
                }
            };
        }
        confirmButton = new IconButton(new Point((int) (tileSize * 7.5), (int) (tileSize * 10)), "CONFIRM", String.valueOf(energyCost), (int) (9 * scaling)) {
            @Override
            public void performAction() {
                if (action == BUYING)
                    buy();
                else
                    sell();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if ((action == BUYING && canBuy()) || (action == SELLING && canSell()))
                    super.mousePressed(e);
                else if (checkBounds(e))
                    GameLoop.playSE(Sound.ERROR_SE);

            }

            @Override
            public void update() {
                super.update();
                updateText(String.valueOf(energyCost));
            }
        };
    }

    @Override
    protected void help(Graphics2D g) {
        int width = windowWidth + inventoryWidth + (secondWindowPos.x-windowPos.x-windowWidth);
        drawWindow(windowPos, width, windowHeight, g);
        g.setColor(Color.white);
        g.setFont(new Font("Kenney Pixel", Font.PLAIN, (int) (10 * scaling)));
        String text = """
                Prove yourself as a trader!
                You have various gems to purchase and to sell. Try to buy as cheapest as possible and sell as expensive as you can.
                
                Mind that every gem has different max amount in one stack:
                Diamond  -  4
                Gold  -  10
                Emerald  -  20
                Ruby  -  14
                Pearl  -  40
                
                Energy cost is calculated as 'base energy cost' per every stack.""";
        Point p = new Point(windowPos.x + tileSize, windowPos.y + tileSize);
        UtilityClass.drawWrappedText(g, text, p.x, p.y, width - 2 * tileSize, windowHeight - 2 * tileSize);
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
            for (GameButton gb : buyButtons) {
                gb.mousePressed(e);
            }
            for (GameButton gb : sellButtons) {
                gb.mousePressed(e);
            }
            if (selectedItem != null) {
                for (GameButton gb : amountSelection) {
                    gb.mousePressed(e);
                }
                confirmButton.mousePressed(e);
            }
        }
        helpButton.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        exitButton.mouseReleased(e);
        helpButton.mouseReleased(e);
        if (!helpOn) {
            for (GameButton gb : buyButtons) {
                gb.mouseReleased(e);
            }
            for (GameButton gb : sellButtons) {
                gb.mouseReleased(e);
            }
            if (selectedItem != null) {
                for (GameButton gb : amountSelection) {
                    gb.mouseReleased(e);
                }
                confirmButton.mouseReleased(e);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        exitButton.mouseMoved(e);
        helpButton.mouseMoved(e);
        if (!helpOn) {
            for (GameButton gb : buyButtons) {
                gb.mouseMoved(e);
            }
            for (GameButton gb : sellButtons) {
                gb.mouseMoved(e);
            }
            if (selectedItem != null) {
                for (GameButton gb : amountSelection) {
                    gb.mouseMoved(e);
                }
                confirmButton.mouseMoved(e);
            }
        }
    }

    @Override
    public void drawPrompt(Graphics2D g) {
        displayPrompt(g, "ENTER MARKET");
    }
}
