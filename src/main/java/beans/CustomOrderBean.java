package beans;

public class CustomOrderBean {
    private String id;
    private String date;
    private int totalCost;
    private int estimatedDays;
    private String nameBoard;
    private String descriptionBoard;
    private String sizeBoard;
    private String status;


    public CustomOrderBean(){};

    public void setId(String id) {
        this.id = id;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public String getDate() {
        return date;
    }

    public int getEstimatedDays() {
        return estimatedDays;
    }

    public String getId() {
        return id;
    }

    public String getNameBoard() {
        return nameBoard;
    }

    public String getStatus() {
        return status;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setEstimatedDays(int estimatedDays) {
        this.estimatedDays = estimatedDays;
    }

    public void setNameBoard(String nameBoard) {
        this.nameBoard = nameBoard;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }
}
