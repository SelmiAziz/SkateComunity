package model.decorator;

public class NoseConcaveDecorator extends SkateBoardDecorator {

    private Skateboard skateboard;
    private double concaveDepthMm;
    private double costMultiplier;
    private double maxDepth;
    private double minDepth;

    public NoseConcaveDecorator(Skateboard skateboard, double concaveDepthMm) {
        super(skateboard);
        this.skateboard = skateboard;
        this.concaveDepthMm = concaveDepthMm;
        setCostMultiplier(1.2);
        setMinDepth(3.0);
        setMaxDepth(20.0);
    }

    public void setCostMultiplier(double costMultiplier) {
        this.costMultiplier = costMultiplier;
    }

    public void setMinDepth(double minDepth) {
        this.minDepth = minDepth;
    }

    public void setMaxDepth(double maxDepth) {
        this.maxDepth = maxDepth;
    }

    @Override
    public int price() {
        int base = super.price();
        double d = Math.max(minDepth, Math.min(maxDepth, concaveDepthMm));
        int cost = (int) Math.round(d * costMultiplier);
        return base + cost;
    }

    @Override
    public String description() {
        return super.description() + " + nose concave (" + concaveDepthMm + "mm)";
    }
}
