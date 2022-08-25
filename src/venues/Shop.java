package venues;

import entities.Merchant;
import gamestates.PlayState;
import main.GameLoop;
import main.GamePanel;
import objects.*;
import ui.GameButton;
import ui.Sound;
import util.UtilityClass;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import static main.GamePanel.*;

public class Shop extends Venue {

    final transient Point defaultSlotPosition;
    private transient final Merchant merchant;
    private transient final int columns = 6;
    private transient final int rows = 2;
    private transient final Point secondWindowPos;
    private transient final BufferedImage coinImg;
    private ArrayList<Item> inventory = new ArrayList<>();
    private transient GameButton purchaseButton;
    private transient Rectangle[] slots;
    private transient int index = -1;
    private transient boolean mouseOver = false;
    private transient Item selectedItem = null;

    public Shop(Point location, PlayState playState, Merchant merchant) {
        super(location, playState);
        this.merchant = merchant;
        windowPos = new Point((int) (tileSize * 5.25f), tileSize * 2);
        secondWindowPos = new Point(windowPos.x, (int) (GamePanel.screenHeight / 2 + 0.25f * tileSize));
        windowWidth = (int) (GamePanel.screenWidth - 10.5f * tileSize);
        defaultSlotPosition = new Point(windowPos.x + tileSize / 2, windowPos.y + tileSize / 2);
        createSlots();
        coinImg = UtilityClass.loadImage("/res/items/Coin.png");
        initializeInventory();
        setupButtons();
    }

    @Override
    public void update() {
        checkIfClose();
        purchaseButton.update();
        exitButton.update();
        helpButton.update();
    }

    @Override
    public void reset() {
        selectedItem = null;
        // clear inventory
        for (int i = inventory.size(); i-- > 0; )
            inventory.remove(i);
        initializeInventory();
    }

    private void addToInventory() {
        Random r = new Random();
        if (r.nextFloat() < 0.8)
            inventory.add(new Coffee());
        if (r.nextFloat() < 0.5)
            inventory.add(new Coffee());
        if (r.nextFloat() < 0.7)
            inventory.add(new Patch());
        if (r.nextFloat() < 0.5)
            inventory.add(new Patch());
        if (r.nextFloat() < 0.33)
            inventory.add(new Syringe());
        if (r.nextFloat() < 0.5)
            inventory.add(new Book());
        if (r.nextFloat() < 0.5)
            inventory.add(new Clover());
        if (r.nextFloat() < 0.5)
            inventory.add(new DumbBell());
        if (r.nextFloat() < 0.5)
            inventory.add(new Mirror());
        if (r.nextFloat() < 0.4)
            inventory.add(new Chest());
    }

    @Override
    protected void nextTurn(int turn) {
        selectedItem = null;
        // clear inventory
        for (int i = inventory.size(); i-- > 0; )
            inventory.remove(i);

        // add new items to inventory
        addToInventory();

        for (Item i : inventory)
            i.setPrice((int) (i.getPrice() * 1.2 * turn));
    }

    @Override
    protected void setupButtons() {
        purchaseButton = new GameButton(new Point(GamePanel.screenWidth / 2, (int) (GamePanel.screenHeight - 1.25f*tileSize)), "BUY") {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (canBuy())
                    super.mouseMoved(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (canBuy())
                    super.mousePressed(e);
                else if (checkBounds(e))
                    GameLoop.playSE(Sound.ERROR_SE);
            }

            @Override
            public void performAction() {
                buy();
            }
        };
        setupExitButton(new Point((int) (windowPos.x + windowWidth + 0.5f * tileSize), windowPos.y + tileSize / 2));
        setupHelpButton(new Point((int) (windowPos.x + windowWidth + 0.5f * tileSize), (int) (windowPos.y + 1.5f * tileSize)));
    }

    @Override
    protected void help(Graphics2D g) {
        int height = (int) (screenHeight - 2.5f*tileSize);
        drawWindow(windowPos, windowWidth, height, g);
        g.setColor(Color.white);
        g.setFont(new Font("Kenney Pixel", Font.PLAIN, (int) (10 * scaling)));
        String text = """
                Here you can buy various items with different perks.
                                
                Disclaimer about stats:
                - Stamina decreases energy cost at all the venues
                - Wisdom makes you a better investor and increases chance for high gains at stock market
                - Charisma enables you to have higher interest rate in bank as well as higher salary at job
                - Luck comes in handy at casino, as it raises you chance for success""";
        Point p = new Point(windowPos.x + tileSize, windowPos.y + tileSize);
        UtilityClass.drawWrappedText(g, text, p.x, p.y, windowWidth - 2 * tileSize, height - 2 * tileSize);
    }

    @Override
    public void draw(Graphics2D g) {
        if (!helpOn) {
            drawWindows(g);
            purchaseButton.draw(g);
            drawInventory(g);
            displayCursor(g);
            displayDescription(g);
            displayCost(g);
            displayStats(g);
        }
        else
            help(g);
        exitButton.draw(g);
        helpButton.draw(g);
    }

    private void initializeInventory() {
        addToInventory();
    }

    private void drawWindows(Graphics2D g) {
        drawWindow(windowPos, windowWidth, GamePanel.screenHeight / 2 - 2 * tileSize, g);
        drawWindow(secondWindowPos, windowWidth, (int) (GamePanel.screenHeight / 2 - 0.75f*tileSize), g);
    }

    private void drawInventory(Graphics2D g) {
        for (int i = 0; i < inventory.size(); i++)
            g.drawImage(inventory.get(i).getImage(), slots[i].x, slots[i].y, null);
    }

    private void createSlots() {
        int slotsNumber = columns * rows;
        slots = new Rectangle[slotsNumber];
        Point slotPosition = new Point(windowPos.x + tileSize / 2, windowPos.y + tileSize / 2);
        for (int i = 0; i < slots.length; i++) {
            slots[i] = new Rectangle(slotPosition.x, slotPosition.y, tileSize, tileSize);
            slotPosition.x += (int) (1.5f * tileSize);
            if (i == columns - 1) {
                slotPosition.x = windowPos.x + tileSize / 2;
                slotPosition.y = windowPos.y + 2 * tileSize;
            }
        }
    }

    private void displayCursor(Graphics2D g) {
        if (mouseOver) {
            g.setColor(Color.white);
            g.setStroke(new BasicStroke(scaling / 4));
            int x = index % columns * (3 * tileSize / 2);
            int y = Math.floorDiv(index, columns) * (3 * tileSize / 2);
            g.drawRoundRect(defaultSlotPosition.x + x, defaultSlotPosition.y + y, tileSize, tileSize, (int) (scaling * 2.5f), (int) (scaling * 2.5f));
        }
        if (selectedItem != null) {
            g.setColor(Color.white);
            g.setStroke(new BasicStroke(scaling * 0.75f));
            int i = inventory.indexOf(selectedItem);
            int x = i % columns * (3 * tileSize / 2);
            int y = Math.floorDiv(i, columns) * (3 * tileSize / 2);
            g.drawRoundRect(defaultSlotPosition.x + x, defaultSlotPosition.y + y, tileSize, tileSize, (int) (scaling * 2.5f), (int) (scaling * 2.5f));
        }
    }

    private void displayDescription(Graphics2D g) {
        if (selectedItem != null) {
            g.setColor(Color.white);
            g.setFont(new Font("Kenney Pixel", Font.BOLD, (int) (11 * scaling)));
            int offset1 = g.getFontMetrics().getAscent();
            g.drawString(selectedItem.getName(), secondWindowPos.x + (int) (tileSize * 0.75f), secondWindowPos.y + (int) (tileSize * 0.75f) + offset1 / 2);
            g.setFont(g.getFont().deriveFont(Font.PLAIN, 9 * scaling));
            //g.drawString(selectedItem.getDescription(), secondWindowPos.x + (int)(tileSize*0.75f), secondWindowPos.y + (int)(tileSize*0.75f) + offset1 + offset2);
            UtilityClass.drawWrappedText(g, selectedItem.getDescription(),
                    secondWindowPos.x + (int) (tileSize * 0.75f), secondWindowPos.y + (int) (tileSize * 0.75f) + offset1,
                    (int) (0.55f * windowWidth), GamePanel.screenHeight / 2 - tileSize);
        }
    }


    private void displayCost(Graphics2D g) {
        if (selectedItem != null) {
            g.setColor(Color.white);
            g.setFont(new Font("Kenney Pixel", Font.BOLD, (int) (9 * scaling)));
            int offset = g.getFontMetrics().getAscent();
            String s = " " + UtilityClass.formatMoney(selectedItem.getPrice());
            Point p = UtilityClass.rightAlign(g, s, new Point((int) (secondWindowPos.x + windowWidth - tileSize * 0.75f), (int) (secondWindowPos.y + tileSize * 0.625f + offset)));
            g.drawString(" " + UtilityClass.formatMoney(selectedItem.getPrice()), p.x, p.y);
            g.drawImage(coinImg, p.x - tileSize / 2, (int) (secondWindowPos.y + tileSize * 0.75f + offset * 0.7f - tileSize * 0.375f), tileSize / 2, tileSize / 2, null);
        }
    }

    private void displayStats(Graphics2D g) {
        if (selectedItem != null) {
            Point pos = new Point((int) (secondWindowPos.x + windowWidth * 0.85f), (int) (secondWindowPos.y + tileSize * 1.75f));
            for (Map.Entry<String, Integer> e : selectedItem.getStats().entrySet()) {
                if (e.getValue() == 0)
                    continue;
                else {
                    g.setFont(g.getFont().deriveFont(Font.PLAIN));
                    String text = e.getKey();
                    g.drawString(text + ":", UtilityClass.rightAlign(g, text, pos).x, pos.y);
                    String value;
                    if (e.getValue() > 0)
                        value = "+" + e.getValue();
                    else
                        value = String.valueOf(e.getValue());
                    g.drawString("   " + value, pos.x, pos.y);
                    pos.y += g.getFontMetrics().getHeight() + 0.15f * tileSize;
                }
            }
        }
    }

    private void buy() {
        if (canBuy()) {
            player.getStats().changeMoney(-selectedItem.getPrice());
            selectedItem.use(player);
            inventory.remove(selectedItem);
            selectedItem = null;
        }
    }

    private boolean canBuy() {
        return selectedItem != null && player.getStats().getMoney() >= selectedItem.getPrice();
    }


    private boolean checkBounds(MouseEvent e, Rectangle r) {   // true if cursor is within bounds
        return r.contains(e.getX(), e.getY());
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
            purchaseButton.mousePressed(e);
            for (int i = 0; i < inventory.size(); i++) {
                if (checkBounds(e, slots[i])) {
                    selectedItem = inventory.get(i);
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        exitButton.mouseReleased(e);
        helpButton.mouseReleased(e);
        if (!helpOn)
            purchaseButton.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        exitButton.mouseMoved(e);
        helpButton.mouseMoved(e);
        if (!helpOn) {
            purchaseButton.mouseMoved(e);
            int j = 0;
            for (int i = 0; i < inventory.size(); i++) {
                if (checkBounds(e, slots[i])) {
                    index = i;
                    j++;
                }
            }
            mouseOver = j != 0;
        }

    }

    @Override
    public void drawPrompt(Graphics2D g) {
        merchant.displayPrompt(g);
    }

    @Override
    public boolean exit() {
        merchant.setVisited(true);
        return super.exit();
    }

    @Override
    public void loadObject(Venue v) {
        this.inventory = ((Shop) v).inventory;
    }
}
