package entities;

import gamestates.PlayState;
import util.UtilityClass;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GamePanel.*;

public class NPC3 extends NPC{

    public NPC3(PlayState playState, Point worldPos) {
        super(playState, worldPos);
    }

    @Override
    protected void setup() {
        speed = tileSize/64;
        hitbox = new Rectangle((int) (2 * scaling), (int) (4 * scaling), (int) (12 * scaling), (int) (11 * scaling));
        aniFreq = 20;
    }

    @Override
    protected int getMaxFrames(int action) {
        return animations[0].length;
    }

    @Override
    protected void loadAnimations() {
        BufferedImage atlas = UtilityClass.loadSpriteAtlas("/res/npc_sprites/01-generic.png");
        animations = new BufferedImage[4][4];
        for (int j = 0; j < animations.length; j++){
            for (int i = 0; i < animations[0].length; i++) {
                animations[j][i] = UtilityClass.scaleImage(atlas.getSubimage((i + 6) * defaultTileSize, j * defaultTileSize, defaultTileSize, defaultTileSize));
            }
            animations[j][3] = UtilityClass.scaleImage(atlas.getSubimage(7 * defaultTileSize, j * defaultTileSize, defaultTileSize, defaultTileSize));
        }
    }
}
