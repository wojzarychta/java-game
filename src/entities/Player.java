package entities;

import gamestates.PlayState;
import main.GamePanel;
import map.Map;
import objects.Item;
import util.UtilityClass;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static main.GamePanel.*;


public class Player extends Entity {

    // CONSTANTS
    protected transient final int IDLE_DOWN = 0;
    protected transient final int IDLE_RIGHT = 1;
    protected transient final int IDLE_LEFT = 2;
    protected transient final int IDLE_UP = 3;
    protected transient final int RUN_DOWN = 4;
    protected transient final int RUN_RIGHT = 5;
    protected transient final int RUN_LEFT = 6;
    protected transient final int RUN_UP = 7;
    private Stats stats = new Stats();
    private final int inventoryLength = 6;
    private transient boolean up, down, right, left;

    public Player(PlayState playState) {
        super(playState);
        //stats = new Stats();

        direction = directions.DOWN;

        // default position on the map
        worldPos.x = 20 * GamePanel.tileSize;
        worldPos.y = 9 * GamePanel.tileSize;
        screenPos.x = screenCenter.x;
        screenPos.y = screenCenter.y;

        speed = tileSize/16;

        hitbox = new Rectangle((int) (3 * GamePanel.scaling), (int) (9 * GamePanel.scaling), (int) (8 * GamePanel.scaling), (int) (6 * GamePanel.scaling));   //setting up hitbox
        loadAnimations();
    }

    public void update() {
        updateSpritePosition();
        move();
        updateAnimation();
        updateFreq();
    }

    public void nextTurn(int turn) {
        if (turn >= 8) {
            stats.changeHealth(-(turn - 7) * 5);
        }
        int energyChange = 80;
        if (turn >= 10) {
            energyChange -= (turn - 9) * 4;
        }
        stats.changeEnergy(energyChange);
    }

    protected int getMaxFrames(int action) {
        switch (action) {
            case 0, 1, 2, 3 -> {
                return 3;
            }
            case 4, 5, 6, 7 -> {
                return 4;
            }
            default -> throw new IllegalStateException("Unexpected value: " + action);
        }
    }

    private void updateFreq() {
        switch (action) {
            case 0, 1, 2, 3 -> aniFreq = 23;
            case 4, 5, 6, 7 -> aniFreq = 18;
            default -> throw new IllegalStateException("Unexpected value: " + action);
        }
    }

    protected int getAction() {
        switch (direction) {
            case UP:
                if (up)
                    return RUN_UP;
                else
                    return IDLE_UP;
            case DOWN:
                if (down)
                    return RUN_DOWN;
                else
                    return IDLE_DOWN;
            case LEFT:
                if (left)
                    return RUN_LEFT;
                else
                    return IDLE_LEFT;
            case RIGHT:
                if (right)
                    return RUN_RIGHT;
                else
                    return IDLE_RIGHT;
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }
    }

    public void move() {
        if (up) {
            direction = directions.UP;
            if (!checkCollision())
                worldPos.y -= speed;
        } else if (down) {
            direction = directions.DOWN;
            if (!checkCollision())
                worldPos.y += speed;
        } else if (right) {
            direction = directions.RIGHT;
            if (!checkCollision())
                worldPos.x += speed;
        } else if (left) {
            direction = directions.LEFT;
            if (!checkCollision())
                worldPos.x -= speed;
        }
    }

    @Override
    protected void loadAnimations() {
        BufferedImage atlas = UtilityClass.loadSpriteAtlas("/res/hero_sprites/hero.png");
        animations = new BufferedImage[8][4];
        for (int j = 0; j < animations.length; j++)
            for (int i = 0; i < animations[0].length; i++) {
                animations[j][i] = UtilityClass.scaleImage(atlas.getSubimage(i * defaultTileSize, j * defaultTileSize, defaultTileSize, defaultTileSize));
            }
    }

    private void updateSpritePosition() {
        screenPos.x = screenCenter.x;
        screenPos.y = screenCenter.y;

        if (worldPos.x < screenCenter.x)
            screenPos.x = worldPos.x;
        if (worldPos.y < screenCenter.y)
            screenPos.y = worldPos.y;
        if (GamePanel.screenWidth - screenCenter.x > Map.worldWidth - worldPos.x)
            screenPos.x = GamePanel.screenWidth - (Map.worldWidth - worldPos.x);
        if (GamePanel.screenHeight - screenCenter.y > Map.worldHeight - worldPos.y)
            screenPos.y = GamePanel.screenHeight - (Map.worldHeight - worldPos.y);
    }

    public void setUp(boolean dir) {
        up = dir;
    }

    public void setDown(boolean dir) {
        down = dir;
    }

    public void setRight(boolean dir) {
        right = dir;
    }

    public void setLeft(boolean dir) {
        left = dir;
    }

    public Point getWorldPosition() {
        return worldPos;
    }

    public Point getScreenPosition() {
        return screenPos;
    }

    public Stats getStats() {
        return stats;
    }

    public void resetBooleans() {
        setLeft(false);
        setRight(false);
        setDown(false);
        setUp(false);
    }

    @Override
    public void reset() {
        super.reset();
        resetBooleans();
        worldPos.x = 20 * GamePanel.tileSize;
        worldPos.y = 9 * GamePanel.tileSize;
        screenPos.x = screenCenter.x;
        screenPos.y = screenCenter.y;
        stats.resetStats();
        inventory = new ArrayList<>();
    }

    public void addToInventory(Item item, int quantity) {
        //check for item in inventory
        for (Item i : inventory) {
            if (i.getClass().equals(item.getClass())) {
                if (i.getQuantity() < i.getMaxStack()) {
                    int j = i.getMaxStack() - i.getQuantity();
                    if (quantity <= j) {
                        i.changeQuantity(quantity);
                        return;
                    } else {
                        i.changeQuantity(j);
                        quantity -= j;
                    }
                }
            }
        }
        // if there isn't such item in inventory create new slot
        while (quantity > 0) {
            if (inventory.size() < inventoryLength) {
                int q;
                Item i;
                try {
                    i = (Item) item.clone();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
                if (quantity <= item.getMaxStack()) {
                    q = quantity;
                    quantity = 0;
                } else {
                    q = item.getMaxStack();
                    quantity -= item.getMaxStack();
                }
                inventory.add(i);
                inventory.get(inventory.size() - 1).changeQuantity(q - 1);
            } else
                return;
        }
    }

    public void removeFromInventory(Item item, int quantity) {
        for (int k = inventory.size(); k-- > 0; ) {
            Item i = inventory.get(k);
            if (i.getClass().equals(item.getClass())) {
                if (i.getQuantity() >= quantity) {
                    i.changeQuantity(-quantity);
                    if (i.getQuantity() == 0)
                        inventory.remove(k);
                    return;
                } else {
                    quantity -= i.getQuantity();
                    inventory.remove(k);
                }
            }
        }
    }

    public int getInventoryLength() {
        return inventoryLength;
    }

    public void loadObject(Player player){
        this.stats = player.getStats();
        this.worldPos = new Point(player.getWorldPosition());
        this.updateSpritePosition();
        this.inventory = player.getInventory();
    }
}

