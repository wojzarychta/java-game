package objects;

import entities.Player;
import main.GameLoop;
import map.Map;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

import static main.GamePanel.tileSize;

public abstract class InteractiveObject {
    protected Rectangle hitbox = new Rectangle(0, 0, tileSize, tileSize);
    protected Point worldPos;
    protected Point screenPos = new Point();
    private final Player player;
    private boolean pickedUp = false;
    private int counter = 0;
    protected BufferedImage image;
    // time in sec when object would change its place
    protected int time = 10;


    public InteractiveObject(Player p, InteractiveObject ... io) {
        player = p;
        loadImage();
        placeOnMap(io);
    }

    protected abstract void loadImage();

    protected boolean checkMapCollision() {
        int column = worldPos.x / tileSize;
        int row = worldPos.y / tileSize;
        for (int i = 0; i < Map.tileLayerMap.size(); i++) {
            int tile = Map.tileLayerMap.get(i)[column][row];
            if (tile != -1 && Map.tiles[tile].collision)
                return true;
        }
        return false;
    }

    protected boolean checkIfCanPickUp() {
        Rectangle hitbox = new Rectangle(worldPos.x + this.hitbox.x, worldPos.y + this.hitbox.y,
                this.hitbox.width, this.hitbox.height);
        Rectangle playerHitbox = new Rectangle(player.getWorldPos().x + player.getHitbox().x, player.getWorldPos().y + player.getHitbox().y,
                player.getHitbox().width, player.getHitbox().height);

        return hitbox.intersects(playerHitbox);
    }

    protected boolean checkObjectCollision(InteractiveObject ... interactiveObjects) {
        for (InteractiveObject io: interactiveObjects ){
            if (io != this){
                if (worldPos.x == io.worldPos.x && worldPos.y == io.worldPos.y)
                    return true;
            }
        }
        return false;
    }

    public void placeOnMap(InteractiveObject ... io) {
        Random r = new Random();
        do {
            worldPos = new Point(r.nextInt(Map.worldSizeHor) * tileSize, r.nextInt(Map.worldSizeVer) * tileSize);
        }
        while (checkMapCollision() || checkIfCanPickUp() || checkObjectCollision(io));
    }

    public void draw(Graphics2D g) {
        if (!pickedUp)
            g.drawImage(image, screenPos.x, screenPos.y, null);
    }

    public void update(InteractiveObject ... io) {
        if (!pickedUp) {
            counter++;

            screenPos.x = worldPos.x - player.getWorldPosition().x + player.getScreenPosition().x;
            screenPos.y = worldPos.y - player.getWorldPosition().y + player.getScreenPosition().y;

            if (checkIfCanPickUp()) {
                pickedUp = true;
                use(player);
            }

            final int treshold = time * GameLoop.UPS;

            if (counter == treshold) {
                placeOnMap(io);
                counter = 0;
            }
        }
    }

    protected abstract void use(Player p);

    public void nextTurn() {
        pickedUp = false;
    }
}
