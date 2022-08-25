package entities;

import gamestates.PlayState;
import main.GamePanel;
import objects.Book;
import objects.Coffee;
import objects.Patch;
import objects.Syringe;
import util.UtilityClass;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import static main.GamePanel.scaling;
import static main.GamePanel.tileSize;

public class Merchant extends Entity {

    private final Player player;
    private transient BufferedImage idleDown, idleRight, idleLeft;
    private boolean visited = false;
    private final String[] dialogues = {"Good day to you, sir!", "Come back soon!"};

    public Merchant(Point position, PlayState playState) {
        super(playState);
        direction = directions.DOWN;
        worldPos.x = position.x;
        worldPos.y = position.y;
        this.player = playState.getPlayer();
        hitbox = new Rectangle((int) (2 * GamePanel.scaling), 0, (int) (11 * GamePanel.scaling), (int) (16 * GamePanel.scaling));   //setting up hitbox
        loadSprites();
        addInventory();
    }

    public void update() {
        updateSpritePosition();
        updateDirection();
        currentSprite = getSprite();
    }

    public void paint(Graphics2D g) {
        displayDialog(g);
        g.drawImage(currentSprite, screenPos.x, screenPos.y, GamePanel.tileSize, GamePanel.tileSize, null);
    }

    @Override
    protected int getAction() {
        return 0;
    }

    @Override
    protected int getMaxFrames(int action) {
        return 0;
    }

    private void loadSprites() {
        try {
            idleDown = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/npc_sprites/merchant_down.png")));
            idleRight = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/npc_sprites/merchant_right.png")));
            idleLeft = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/npc_sprites/merchant_left.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage getSprite() {
        return switch (direction) {
            case LEFT -> idleLeft;
            case RIGHT -> idleRight;
            default -> idleDown;
        };
    }

    private void updateSpritePosition() {
        screenPos.x = worldPos.x - player.getWorldPosition().x + player.getScreenPosition().x;
        screenPos.y = worldPos.y - player.getWorldPosition().y + player.getScreenPosition().y;
    }

    private void updateDirection() {
        double x = player.getWorldPosition().x - worldPos.x;
        double y = player.getWorldPosition().y - worldPos.y;
        double ctgx = x / y;
        if ((x >= 0 && y <= 0) || (ctgx > 1 && x > 0 && y > 0))
            direction = directions.RIGHT;
        else if ((x <= 0 && y <= 0) || (ctgx < -1 && x < 0 && y > 0))
            direction = directions.LEFT;
        else
            direction = directions.DOWN;
    }

    public void displayPrompt(Graphics2D g) {
        if (calculateDistance() < 3 * tileSize) {
            Point promptPos = new Point(screenPos.x, screenPos.y + tileSize);
            // display prompt img
            BufferedImage promptImg = UtilityClass.loadPromptKeyImg("F");
            g.drawImage(promptImg, promptPos.x + tileSize / 2 - promptImg.getWidth() / 2, promptPos.y, null);
            //display prompt text
            String prompt = "TRADE";
            Font font = new Font("Kenney Pixel", Font.PLAIN, 30);
            g.setFont(font);
            g.setColor(Color.white);
            Point p = UtilityClass.centerText(g, prompt, new Point(promptPos.x + tileSize / 2, promptPos.y + tileSize / 2 + g.getFontMetrics().getHeight() / 2));
            g.drawString(prompt, p.x, p.y);
        }
    }

    private int calculateDistance() {
        int deltaX = player.getWorldPosition().x - worldPos.x;
        int deltaY = player.getWorldPosition().y - worldPos.y;
        return (int) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
    }

    private void displayDialog(Graphics2D g) {
        if (calculateDistance() < 3 * tileSize) {
            BufferedImage bubble = getDialogBubble();
            Point dialogPosition = new Point(screenPos.x + tileSize / 2, screenPos.y - tileSize);
            g.drawImage(bubble, dialogPosition.x, dialogPosition.y, 3 * tileSize, tileSize, null);
            // text:
            g.setColor(Color.black);
            g.setFont(new Font("Kenney Pixel", Font.PLAIN, (int) (6.5f*scaling)));
            Point pos = new Point(screenPos.x + 3 * tileSize / 4, screenPos.y - tileSize / 2);
            if (visited)
                g.drawString(dialogues[1], pos.x, pos.y);
            else
                g.drawString(dialogues[0], pos.x, pos.y);
        }
        else
            visited = false;
    }

    private BufferedImage getDialogBubble() {
        BufferedImage atlas = UtilityClass.loadSpriteAtlas("/res/gui/GUI.png");
        return atlas.getSubimage(9 * GamePanel.defaultTileSize, 4 * GamePanel.defaultTileSize, 3 * GamePanel.defaultTileSize, GamePanel.defaultTileSize);
    }

    public void addInventory() {
        inventory.add(new Coffee());
        inventory.add(new Patch());
        inventory.add(new Syringe());
        inventory.add(new Book());
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

}
