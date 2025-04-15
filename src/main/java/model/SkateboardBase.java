package model;

import model.decorator.Skateboard;

public class SkateboardBase implements Skateboard {
    private int id;
    private String description;
    private int price;
    private String name;
    private String size;

    public SkateboardBase(String name, String description, String size, int price){
        this.description = description;
        this.price = price;
        this.name = name;
        this.size = size;
    }

    public SkateboardBase(int id, String name, String description , String size, int price){
        this.name = name;
        this.description = description;
        this.size = size;
        this.price = price;
    }


    public String description() {
        return description;
    }

    public int price() {
        return price;
    }

    public void SetSize(String size) {
        this.size = size;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String size() {
        return size;
    }

    public String name() {
        return name;
    }

    public int skateboardId() {
        return id;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public void setPrice(int price) {
        this.price = price;
    }


}
