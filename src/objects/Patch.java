package objects;

public class Patch extends Item{
    public Patch(){
        super();
    }

    @Override
    protected void initializeItem() {
        name = "First aid kit";
        description = "Restores part of missing health";
        loadImage("patch.png");
        health = 20;
        price = 10_000;
    }
}
