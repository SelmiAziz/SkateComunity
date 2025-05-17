package model.decorator;

public class GripTextureDecorator extends BoardDecorator {

    private double costMultiplier;
    private double gripValue;
    private double maxGrip;
    private double minGrip;

    public GripTextureDecorator(Board board, double gripValue){
        super(board);
        this.gripValue = gripValue;
        setCostMultiplier(10);
        setMaxGrip(0.7);
        setMinGrip(0.4);
    }


    public void setCostMultiplier(double costMultiplier) {
        this.costMultiplier = costMultiplier;
    }

    public void setMaxGrip(double maxGrip) {
        this.maxGrip = maxGrip;
    }


    public void setMinGrip(double minGrip) {
        this.minGrip = minGrip;
    }

    @Override
    public int price() {
        int v = super.price();
        double g = Math.max(this.minGrip, Math.min(this.maxGrip, this.gripValue));
        double norm = (g - this.minGrip) / (this.maxGrip - this.minGrip);
        int curvedCost = (int)Math.round(Math.pow(norm, 2.2) * this.costMultiplier);
        return v+curvedCost;
    }

    @Override
    public String description() {
        return super.description() + " + grip texture (" + gripValue + ")";
    }
}
