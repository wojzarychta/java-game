package objects;

public class Syringe extends Item{
    public Syringe(){
        super();
    }


    @Override
    protected void initializeItem() {
        name = "Drugs";
        description = "Very unhealthy, though gives you powerful boost of energy";
        loadImage("syringe.png");
        energy = 50;
        health = -50;
        price = 20_000;
    }
}
