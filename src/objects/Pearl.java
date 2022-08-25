package objects;

public class Pearl extends Item{
    @Override
    protected void initializeItem() {
        loadImage("Pearl.png");
        name = "Pearl";
        price = 500;
        maxStack = 40;
    }
}
