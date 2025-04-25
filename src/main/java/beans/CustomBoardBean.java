package beans;

public class CustomBoardBean {
    private String name;
    private int extraPiles;
    private double gripTexture;
    private double concaveNose;
    private double concaveTail;
    private int warrantyMonths;

    public CustomBoardBean(){};


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getGripTexture() {
        return gripTexture;
    }

    public int getExtraPiles() {
        return extraPiles;
    }

    public void setExtraPiles(int extraPiles) {
        this.extraPiles = extraPiles;
    }

    public void setGripTexture(double gripTexture) {
        this.gripTexture = gripTexture;
    }

    public double getConcaveNose() {
        return concaveNose;
    }


    public double getConcaveTail() {
        return concaveTail;
    }

    public int getWarrantyMonths() {
        return warrantyMonths;
    }

    public void setConcaveNose(double concaveNose) {
        this.concaveNose = concaveNose;
    }


    public void setConcaveTail(double concaveTail) {
        this.concaveTail = concaveTail;
    }


    public void setWarrantyMonths(int warrantyMonths) {
        this.warrantyMonths = warrantyMonths;
    }


}