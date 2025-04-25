package beans;

public class OrderSummaryBean {
    private String id;
    private String creationDate;
    private String deliveryDate;
    private int totalCost;
    private int estimatedDays;
    private String nameBoard;
    private String descriptionBoard;
    private String sizeBoard;
    private String regionDestination;
    private String provinceDestination;
    private String cityDestination;
    private String streetAddersDestination;
    private String status;


    public OrderSummaryBean(){};

    public void setId(String id) {
        this.id = id;
    }

    public int getTotalCost() {
        return this.totalCost;
    }

    public String getCreationDate() {
        return this.creationDate;
    }

    public String getDeliveryDate(){return this.deliveryDate;}

    public int getEstimatedDays() {
        return this.estimatedDays;
    }

    public String getId() {
        return this.id;
    }

    public String getNameBoard() {
        return this.nameBoard;
    }

    public String getStatus() {
        return this.status;
    }

    public void setCreationDate(String date) {
        this.creationDate = date;
    }

    public void setEstimatedDays(int estimatedDays) {this.estimatedDays = estimatedDays;}

    public void setNameBoard(String nameBoard) {
        this.nameBoard = nameBoard;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public String getDescriptionBoard() {
        return this.descriptionBoard;
    }

    public String getCityDestination() {
        return this.cityDestination;
    }

    public String getProvinceDestination() {
        return this.provinceDestination;
    }

    public String getRegionDestination() {
        return this.regionDestination;
    }

    public String getSizeBoard() {
        return this.sizeBoard;
    }

    public String getStreetAddersDestination() {
        return this.streetAddersDestination;
    }

    public void setCityDestination(String cityDestination) {
        this.cityDestination = cityDestination;
    }


    public void setDeliveryDate(String date){
        this.deliveryDate = date;
    }

    public void setDescriptionBoard(String descriptionBoard) {
        this.descriptionBoard = descriptionBoard;
    }

    public void setProvinceDestination(String provinceDestination) {
        this.provinceDestination = provinceDestination;
    }

    public void setRegionDestination(String regionDestination) {
        this.regionDestination = regionDestination;
    }

    public void setSizeBoard(String sizeBoard) {
        this.sizeBoard = sizeBoard;
    }

    public void setStreetAddersDestination(String streetAddersDestination) {
        this.streetAddersDestination = streetAddersDestination;
    }
}
