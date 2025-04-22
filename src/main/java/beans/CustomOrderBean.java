package beans;

public class CustomOrderBean {
    private String id;
    private String status;
    private String descriptionBoard;
    private String creationDate;

    public CustomOrderBean(){};

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String date) {
        this.creationDate = date;
    }

    public void setDescriptionBoard(String descriptionBoard) {
        this.descriptionBoard = descriptionBoard;
    }

    public String getDescriptionBoard() {
        return descriptionBoard;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
