package model.decorator;

public class TailConcaveDecorator extends BoardDecorator {

    private double concaveDepthMm;
    private double costMultiplier;
    private double maxDepth;
    private double minDepth;

    public TailConcaveDecorator(Board board, double concaveDepthMm) {
        super(board);
        this.concaveDepthMm = concaveDepthMm;
        setCostMultiplier(1.0);
        setTailMinDepth(3.0);
        setTailMaxDepth(20.0);
    }

    public void setCostMultiplier(double costMultiplier) {
        this.costMultiplier = costMultiplier;
    }

    public void setTailMinDepth(double minDepth) {
        this.minDepth = minDepth;
    }

    public void setTailMaxDepth(double maxDepth) {
        this.maxDepth = maxDepth;
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
        return super.description() + " + tail concave (" + concaveDepthMm + "mm)";
    }
}

