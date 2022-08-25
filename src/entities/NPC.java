package entities;

import gamestates.PlayState;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public abstract class NPC extends Entity {

    protected final int RUN_DOWN = 0;
    protected final int RUN_RIGHT = 2;
    protected final int RUN_LEFT = 1;
    protected final int RUN_UP = 3;

    protected transient int directionCounter = 0;
    protected transient int maxDirectionCounter = 200;

    public NPC(PlayState playState, Point worldPos) {
        super(playState);
        this.worldPos = new Point(worldPos);
        setup();
        loadAnimations();
    }

    protected abstract void setup();


    @Override
    public void update() {
        updateSpritePosition();
        move();
        updateAnimation();
    }

    protected int getAction() {
        return switch (direction) {
            case UP -> RUN_UP;
            case DOWN -> RUN_DOWN;
            case LEFT -> RUN_LEFT;
            case RIGHT -> RUN_RIGHT;
        };
    }

    public void move() {
        // moves NPC in random direction
        directionCounter++;
        if (directionCounter == maxDirectionCounter) {
            Random random = new Random();
            int i = random.nextInt(4);
            switch (i) {
                case 0 -> direction = directions.UP;
                case 1 -> direction = directions.DOWN;
                case 2 -> direction = directions.RIGHT;
                case 3 -> direction = directions.LEFT;
            }
            directionCounter = 0;
            maxDirectionCounter = random.nextInt(251)+100; // <100;350>
        }

        if (!checkCollision()) {
            switch (direction) {
                case UP -> worldPos.y -= speed;
                case DOWN -> worldPos.y += speed;
                case LEFT -> worldPos.x -= speed;
                case RIGHT -> worldPos.x += speed;
            }
        } else
            directionCounter = maxDirectionCounter - 1;
    }

    protected void updateSpritePosition() {
        screenPos.x = worldPos.x - playState.getPlayer().getWorldPosition().x + playState.getPlayer().getScreenPosition().x;
        screenPos.y = worldPos.y - playState.getPlayer().getWorldPosition().y + playState.getPlayer().getScreenPosition().y;
    }

    protected boolean checkPlayerCollision() {
        Rectangle hitbox = new Rectangle(worldPos.x + this.hitbox.x, worldPos.y + this.hitbox.y,
                this.hitbox.width, this.hitbox.height);

        Player p = playState.getPlayer();
        Rectangle playerHitbox = new Rectangle(p.getWorldPos().x + p.getHitbox().x, p.getWorldPos().y + p.getHitbox().y,
                p.getHitbox().width, p.getHitbox().height);
        switch (direction) {
            case UP -> {
                hitbox.y -= speed;
                if (hitbox.intersects(playerHitbox))
                    return true;
            }
            case DOWN -> {
                hitbox.y += speed;
                if (hitbox.intersects(playerHitbox))
                    return true;
            }
            case RIGHT -> {
                hitbox.x += speed;
                if (hitbox.intersects(playerHitbox))
                    return true;
            }
            case LEFT -> {
                hitbox.x -= speed;
                if (hitbox.intersects(playerHitbox))
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkCollision() {
        return super.checkCollision() || checkPlayerCollision();
    }

    public void reset(Point worldPos) {
        super.reset();
        directionCounter = 0;
        this.worldPos = new Point(worldPos);
        updateSpritePosition();
    }

    public void loadObject(Entity e){
        this.worldPos = new Point(e.getWorldPos());
        this.updateSpritePosition();
    }
}
