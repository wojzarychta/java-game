package objects;

import entities.Player;

import java.util.Random;

public class Chest extends Item {

    @Override
    protected void initializeItem() {
        loadImage("Chest.png");
        name = "Chest";
        price = 10000;
        description = "You can find unknown amount of money inside. Would you take a bet?";
    }

    @Override
    public void use(Player p) {
        Random r = new Random();
        int range = (int) (1.5f * price);
        int money = (int) (r.nextInt(range) + 0.5f * price);
        p.getStats().changeMoney(money);
    }
}
