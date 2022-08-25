package objects;

import entities.Player;
import util.UtilityClass;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

import static main.GamePanel.tileSize;

public abstract class Item implements Cloneable, Serializable {

    protected String name = "item";
    protected transient BufferedImage image;
    protected int price = 0;
    protected int energy = 0;
    protected int health = 0;
    protected int wisdom, luck, charisma, stamina = 0;

    protected String description = "description";
    protected HashMap<String, Integer> stats = new HashMap<>();
    protected int maxStack = 10;
    protected int quantity = 1;



    public Item(){
        initializeItem();
        initializeHashMap();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    protected void loadImage(String fileName){
        Graphics2D g;
        BufferedImage originalImage;
        try {
            originalImage = ImageIO.read(Objects.requireNonNull(UtilityClass.class.getResourceAsStream("/res/items/"+fileName)));
            // scaling images
            BufferedImage scaledImage = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
            g = scaledImage.createGraphics();
            g.drawImage(originalImage, 0, 0, tileSize, tileSize, null);
            image = scaledImage;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public void use(Player p){
        if (health != 0)
            p.getStats().changeHealth(health);
        if(energy != 0)
            p.getStats().changeEnergy(energy);
        if (wisdom != 0)
            p.getStats().changeWisdom(wisdom);
        if(luck != 0)
            p.getStats().changeLuck(luck);
        if(stamina != 0)
            p.getStats().changeStamina(stamina);
        if (charisma != 0)
            p.getStats().changeCharisma(charisma);
    }
    protected void initializeHashMap(){
        stats.put("Health", health);
        stats.put("Energy", energy);
        stats.put("Wisdom", wisdom);
        stats.put("Luck", luck);
        stats.put("Charisma", charisma);
        stats.put("Stamina", stamina);
    }

    public HashMap<String, Integer> getStats() {
        return stats;
    }
    protected abstract void initializeItem();

    public int getQuantity() {
        return quantity;
    }

    public void changeQuantity(int quantity) {
        this.quantity += quantity;
    }

    public int getMaxStack() {
        return maxStack;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
