package objects;

public class DumbBell extends Item {
    @Override
    protected void initializeItem() {
        loadImage("dumb-bell.png");
        name = "Dumb-bell";
        price = 20000;
        stamina = 1;
        description = "Dumb-bells are really good way to increase your stamina!";
    }
}
