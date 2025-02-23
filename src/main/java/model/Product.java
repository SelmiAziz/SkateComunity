package model;

public class Product {
    private String name;
    private String description;
    private String seller;
    private int cost;


    public Product(String name, String description, String seller, Integer cost){
        this.name = name;
        this.description = description;
        this.seller = seller;
        this.cost = cost;
    }

    public String getSeller(){
        return this.seller;
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
