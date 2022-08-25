package objects;

import entities.Player;
import util.UtilityClass;

import java.awt.image.BufferedImage;

import static main.GamePanel.defaultTileSize;

public class BigMoneyBag extends InteractiveObject{
    public BigMoneyBag(Player p) {
        super(p);
        time = 3;
    }

    @Override
    protected void loadImage() {
        BufferedImage atlas = UtilityClass.loadSpriteAtlas("/res/items/Treasure+.png");
        image = UtilityClass.scaleImage(atlas.getSubimage(6 * defaultTileSize, 8 * defaultTileSize, defaultTileSize, defaultTileSize));
    }

    @Override
    protected void use(Player p) {

            int money = (int) (0.09f * p.getStats().getMoney());
            p.getStats().changeMoney(money);

    }
}
