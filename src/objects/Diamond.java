package objects;

public class Diamond extends Item{

    @Override
    protected void initializeItem() {
        loadImage("Diamond.png");
        name = "Diamond";
        price = 5_000;
        maxStack = 4;
    }
}
