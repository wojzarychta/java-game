package objects;

import entities.Player;
import util.UtilityClass;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

import static main.GamePanel.defaultTileSize;
import static main.GamePanel.tileSize;

public class MoneyBag extends InteractiveObject{

    public MoneyBag(Player p) {
        super(p);
        time = 6;
    }

    @Override
    protected void loadImage() {
        BufferedImage atlas = UtilityClass.loadSpriteAtlas("/res/items/Treasure+.png");
        image = UtilityClass.scaleImage(atlas.getSubimage(7 * defaultTileSize, 8 * defaultTileSize, defaultTileSize, defaultTileSize));
    }

    @Override
    public void use(Player p) {
        int money = (int) (0.06f * p.getStats().getMoney());
        p.getStats().changeMoney(money);
    }
}
