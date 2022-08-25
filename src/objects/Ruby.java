package objects;

public class Ruby extends Item{
    @Override
    protected void initializeItem() {
        loadImage("Ruby.png");
        name = "Ruby";
        price = 1500;
        maxStack = 14;
    }
}
