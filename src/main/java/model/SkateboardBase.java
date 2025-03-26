package model;

public class SkateboardBase {
    private String description;
    private int price;
    private String color;
    private String dimension;

    public SkateboardBase(String description, int price, String color, String dimension){
        this.description = description;
        this.price = price;
        this.color = color;
        this.dimension = dimension;
    }


    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public String getColor() {
        return color;
    }

    public String getDimension() {
        return dimension;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public void setPrice(int price) {
        this.price = price;
    }


}
