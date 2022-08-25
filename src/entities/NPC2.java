package entities;

import gamestates.PlayState;
import util.UtilityClass;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GamePanel.*;

public class NPC2 extends NPC {

    public NPC2(PlayState playState, Point worldPos) {
        super(playState, worldPos);
    }

    @Override
    protected void setup() {
        speed = tileSize/32;
        hitbox = new Rectangle((int) (2 * scaling), (int) (4 * scaling), (int) (12 * scaling), (int) (11 * scaling));
        aniFreq = 22;
    }

    @Override
    protected int getMaxFrames(int action) {
        return 3;
    }

    @Override
    protected void loadAnimations() {
        BufferedImage atlas = UtilityClass.loadSpriteAtlas("/res/npc_sprites/NPCS.png");
        animations = new BufferedImage[4][3];
        for (int j = 0; j < animations.length; j++)
            for (int i = 0; i < animations[0].length; i++) {
                animations[j][i] = UtilityClass.scaleImage(atlas.getSubimage((i + 3) * defaultTileSize, (j+4) * defaultTileSize, defaultTileSize, defaultTileSize));
            }
    }
}
