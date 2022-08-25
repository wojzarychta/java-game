package objects;

public class Clover extends Item{
    @Override
    protected void initializeItem() {
        loadImage("clover.png");
        name = "Clover";
        luck = +1;
        price = 5000;
        description = "Four-leaf clover brings you a lot of luck!";
    }
}
