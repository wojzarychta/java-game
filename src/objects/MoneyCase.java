package objects;

import entities.Player;
import util.UtilityClass;

import java.awt.image.BufferedImage;

import static main.GamePanel.defaultTileSize;

public class MoneyCase extends InteractiveObject {
    public MoneyCase(Player p) {
        super(p);
        time = 9;
    }

    @Override
    protected void loadImage() {
        BufferedImage atlas = UtilityClass.loadSpriteAtlas("/res/items/Treasure+.png");
        image = UtilityClass.scaleImage(atlas.getSubimage(8 * defaultTileSize, 8 * defaultTileSize, defaultTileSize, defaultTileSize));
    }

    @Override
    protected void use(Player p) {
        int money = (int) (0.03f * p.getStats().getMoney());
        p.getStats().changeMoney(money);
    }
}
