package model;

public class Product {
    private String name;
    private String description;
    private int cost;


    public Product(String name, String description, Integer cost){
        this.name = name;
        this.description = description;
        this.cost = cost;
    }


    public String getDescription(){
        return this.description;
    }

    public Integer getCost(){
        return this.cost;
    }

    public String getName(){
        return this.name;
    }
}
