package model.decorator;

public class NoseConcaveDecorator extends BoardDecorator {

    private double concaveDepthMm;
    private double costMultiplier;
    private double maxDepth;
    private double minDepth;

    public NoseConcaveDecorator(Board board, double concaveDepthMm) {
        super(board);
        this.concaveDepthMm = concaveDepthMm;
        setCostMultiplier(1.2);
        setConcaveMinDepth(3.0);
        setConcaveMaxDepth(20.0);
    }

    public void setConcaveMinDepth(double minDepth) {
        this.minDepth = minDepth;
    }

    public void setConcaveMaxDepth(double maxDepth) {
        this.maxDepth = maxDepth;
    }

    public void setCostMultiplier(double costMultiplier) {
        this.costMultiplier = costMultiplier;
    }


    @Override
    public int price() {
        int base = super.price();
        double d = Math.clamp(concaveDepthMm, minDepth, maxDepth);
        int cost = (int) Math.round(d * costMultiplier);
        return base + cost;
    }

    @Override
    public String description() {
        return super.description() + " + nose concave (" + concaveDepthMm + "mm)";
    }
}
