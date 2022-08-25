package objects;

public class Emerald extends Item{
    @Override
    protected void initializeItem() {
        loadImage("Emerald.png");
        name = "Emerald";
        price = 1000;
        maxStack = 20;
    }
}
