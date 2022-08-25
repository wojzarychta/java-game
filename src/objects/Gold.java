package objects;

public class Gold extends Item{
    @Override
    protected void initializeItem() {
        loadImage("Gold.png");
        name = "Gold";
        price = 2000;
        maxStack = 10;
    }
}
