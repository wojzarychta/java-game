package venues;

import java.io.Serializable;

public class Stock implements Serializable {
    public final String name;
    public final int risk;
    private double price, change;
    private double units;
    private int invested, value;
    private double averageOpen;
    // 0 - low risk, 1 - medium, 2 - high

    public Stock(String name, double price, int risk) {
        this.name = name;
        this.price = price;
        this.risk = risk;
    }

    public double getPrice() {
        return price;
    }

    public void changePrice(double change) {
        this.change = change;
        price *= (1 + change);
        if (price < 0)
            price = 0;
    }

    public double getChange() {
        return change;
    }

    public int getInvested() {
        return invested;
    }

    public void setInvested(int invested) {
        this.invested = invested;
    }

    public int getValue() {
        value = (int) (units * price);
        return value;
    }

    public void buy(int amount) {
        double i = averageOpen * units;
        units += (double) (amount) / price;
        invested += amount;
        averageOpen = (i + (double) (amount) ) / units;
    }

    public void sell(int amount) {
        units -= (double) (amount) / price;
        invested -= (double) (amount) / price*averageOpen;
    }

    public double getUnits() {
        return units;
    }
}
