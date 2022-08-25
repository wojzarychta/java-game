package objects;

public class Mirror extends Item{
    @Override
    protected void initializeItem() {
        loadImage("mirror.png");
        name = "Mirror";
        price = 15000;
        description = "Little practice in front of mirror increases one's confidence and charisma.";
        charisma = 1;
    }
}
