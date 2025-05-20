package model;

import model.decorator.Board;

import java.util.UUID;

public class BoardBase implements Board {
    private String boardCode;
    private String description;
    private int price;
    private String name;
    private String size;

    public BoardBase(String name, String description, String size, int price){
        this.boardCode =  UUID.randomUUID().toString();
        this.description = description;
        this.price = price;
        this.name = name;
        this.size = size;
    }

    public BoardBase(String boardCode, String name, String description , String size, int price){
        this.boardCode = boardCode;
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


    public void setName(String name) {
        this.name = name;
    }

    public String size() {
        return size;
    }

    public String name() {
        return name;
    }

    public String boardCode() {
        return boardCode;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setBoardCode(String boardCode) {
        this.boardCode = boardCode;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public void setPrice(int price) {
        this.price = price;
    }


}
