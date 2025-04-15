package model;

import model.decorator.Skateboard;

import java.util.UUID;

public class SkateboardBase implements Skateboard {
    private String id;
    private String description;
    private int price;
    private String name;
    private String size;

    public SkateboardBase(String name, String description, String size, int price){
        this.id =  UUID.randomUUID().toString();
        this.description = description;
        this.price = price;
        this.name = name;
        this.size = size;
    }

    public SkateboardBase(String id, String name, String description , String size, int price){
        this.id = id;
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

    public String skateboardId() {
        return id;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public void setPrice(int price) {
        this.price = price;
    }


}
