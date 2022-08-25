package objects;

public class Coffee extends Item{

    public Coffee(){
        super();
    }

    @Override
    protected void initializeItem() {
        name = "Cup of Coffee";
        description = "Gives you boost of energy";
        loadImage("CoffeeCup.png");
        energy = 10;
        price = 2500;
        health = -5;
    }
}
