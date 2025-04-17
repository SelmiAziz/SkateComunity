package beans;

public class CustomSkateboardBean {
    private String name;
    private int extraPiles;
    private double gripTexture;
    private int noiseReduction;
    private double concaveNose;
    private double concaveTail;
    private int warrantyMonths;

    public CustomSkateboardBean(){};

    public CustomSkateboardBean(String name, int extraPiles, float gripTexture, int noiseReduction, float concaveNose, float concaveTail, int warrantyMonths){
        this.name = name;
        this.extraPiles = extraPiles;
        this.gripTexture = gripTexture;
        this.noiseReduction = noiseReduction;
        this.concaveNose = concaveNose;
        this.concaveTail = concaveTail;
        this.warrantyMonths = warrantyMonths;
    }

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

    public int getNoiseReduction() {
        return noiseReduction;
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

    public void setNoiseReduction(int noiseReduction) {
        this.noiseReduction = noiseReduction;
    }

    public void setWarrantyMonths(int warrantyMonths) {
        this.warrantyMonths = warrantyMonths;
    }


}
