package objects;

public class Book extends Item{
    public Book(){
        super();
    }

    @Override
    protected void initializeItem() {
        name = "Course book";
        loadImage("Book.png");
        wisdom = +1;
        price = 10_000;
        description = "Reading books makes you wiser.";
    }
}
