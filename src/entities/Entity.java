package entities;

import gamestates.PlayState;
import main.GamePanel;
import map.Map;
import objects.Item;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class Entity implements Serializable {
    public Point worldPos = new Point();
    public Point screenPos = new Point();
    protected int speed;
    protected PlayState playState;
    enum directions {UP, DOWN, RIGHT, LEFT}
    protected transient directions direction = directions.DOWN;
    protected transient BufferedImage currentSprite;
    protected transient BufferedImage[][] animations;

    protected Rectangle hitbox = new Rectangle();
    protected ArrayList<Item> inventory = new ArrayList<>();

    // animation variables
    protected transient int aniTick, aniIndex;
    protected int aniFreq = 18;
    protected transient int action = 0;


    public Entity(PlayState playState) {
        this.playState = playState;
    }

    public void update() {
    }

    public void paint(Graphics2D g) {
        g.drawImage(animations[action][aniIndex], screenPos.x, screenPos.y, null);
    }

    protected abstract int getAction();
    protected abstract int getMaxFrames(int action);

    protected void updateAnimation() {
        action = getAction();
        aniTick++;
        if (aniTick >= aniFreq) {
            aniTick = 0;
            aniIndex++;
        }
        if (aniIndex >= getMaxFrames(action)) {
            aniIndex = 0;
        }
    }

    protected void loadAnimations(){}


    public boolean checkCollision() {
        // returns true if there is collision
        // ! dodać dosunięcie jednostki do ściany aby nie było przerwy
        return checkMapCollision() || checkEntityCollision();
    }

    protected boolean checkMapCollision() {
        boolean collision = false;

        int leftHitboxWall = worldPos.x + hitbox.x;
        int rightHitboxWall = worldPos.x + hitbox.x + hitbox.width;
        int topHitboxWall = worldPos.y + hitbox.y;
        int bottomHitboxWall = worldPos.y + hitbox.y + hitbox.height;

        // current column and row of entity which determines on which tiles it is located
        int topRow = topHitboxWall / GamePanel.tileSize;
        int bottomRow = bottomHitboxWall / GamePanel.tileSize;
        int rightCol = rightHitboxWall / GamePanel.tileSize;
        int leftCol = leftHitboxWall / GamePanel.tileSize;


        int tile1, tile2;

        // determining on which tile would entity be after move() action
        for (int i = 0; i < Map.tileLayerMap.size(); i++) {
            switch (direction) {
                case UP -> {
                    topRow = (topHitboxWall - speed) / GamePanel.tileSize;
                    if (topHitboxWall - speed < 0)
                        return true;
                    tile1 = Map.tileLayerMap.get(i)[leftCol][topRow];
                    tile2 = Map.tileLayerMap.get(i)[rightCol][topRow];
                    if ((tile1 != -1 && Map.tiles[tile1].collision) || (tile2 != -1 && Map.tiles[tile2].collision))
                        collision = true;
                }
                case DOWN -> {
                    bottomRow = (bottomHitboxWall + speed) / GamePanel.tileSize;
                    if (bottomRow > Map.worldSizeVer - 1)
                        return true;
                    tile1 = Map.tileLayerMap.get(i)[leftCol][bottomRow];
                    tile2 = Map.tileLayerMap.get(i)[rightCol][bottomRow];
                    if ((tile1 != -1 && Map.tiles[tile1].collision) || (tile2 != -1 && Map.tiles[tile2].collision))
                        collision = true;
                }
                case LEFT -> {
                    leftCol = (leftHitboxWall - speed) / GamePanel.tileSize;
                    if (leftHitboxWall - speed < 0)
                        return true;
                    tile1 = Map.tileLayerMap.get(i)[leftCol][topRow];
                    tile2 = Map.tileLayerMap.get(i)[leftCol][bottomRow];
                    if ((tile1 != -1 && Map.tiles[tile1].collision) || (tile2 != -1 && Map.tiles[tile2].collision))
                        collision = true;
                }
                case RIGHT -> {
                    rightCol = (rightHitboxWall + speed) / GamePanel.tileSize;
                    if (rightCol > Map.worldSizeHor - 1)
                        return true;
                    tile1 = Map.tileLayerMap.get(i)[rightCol][topRow];
                    tile2 = Map.tileLayerMap.get(i)[rightCol][bottomRow];
                    if ((tile1 != -1 && Map.tiles[tile1].collision) || (tile2 != -1 && Map.tiles[tile2].collision))
                        collision = true;
                }
            }
        }

        return collision;
    }

    protected boolean checkEntityCollision() {

        Rectangle hitbox = new Rectangle(worldPos.x + this.hitbox.x, worldPos.y + this.hitbox.y,
                this.hitbox.width, this.hitbox.height);

        Entity[] entities = playState.getNPCs();

        for (Entity e : entities) {
            if (e != this) {
                Rectangle entityHitbox = new Rectangle(e.getWorldPos().x + e.getHitbox().x, e.getWorldPos().y + e.getHitbox().y,
                        e.getHitbox().width, e.getHitbox().height);
                switch (direction) {
                    case UP -> {
                        hitbox.y -= speed;
                        if (hitbox.intersects(entityHitbox))
                            return true;
                    }
                    case DOWN -> {
                        hitbox.y += speed;
                        if (hitbox.intersects(entityHitbox))
                            return true;
                    }
                    case RIGHT -> {
                        hitbox.x += speed;
                        if (hitbox.intersects(entityHitbox))
                            return true;
                    }
                    case LEFT -> {
                        hitbox.x -= speed;
                        if (hitbox.intersects(entityHitbox))
                            return true;
                    }
                }
            }
        }

        return false;
    }


    public Rectangle getHitbox() {
        return hitbox;
    }

    public Point getWorldPos() {
        return worldPos;
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }

    public void reset(){
        direction = directions.DOWN;
    }
    public void loadObject(Entity e){}
}
