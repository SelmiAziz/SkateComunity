package beans;

public class BoardBean {
    private String id;
    private String name;
    private String size;
    private int price;
    private String description;

    public BoardBean(){};

    public BoardBean(String name, String description, String size, int price){
        this.name = name;
        this.size = size;
        this.price = price;
        this.description = description;
    }



    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setId(String id) {this.id = id;}

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
