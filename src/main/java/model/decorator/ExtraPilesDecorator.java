package model.decorator;

public class ExtraPilesDecorator extends BoardDecorator {

    private int extraPiles;
    private int minExtra;
    private int maxExtra;
    private int costBase;
    private int discountPerExtra; // es. 10 = -10% per ogni extra in più

    public ExtraPilesDecorator(Board board, int extraPiles) {
        super(board);
        this.extraPiles = extraPiles;
        setMinExtra(0);
        setMaxExtra(5);
        setCostBase(7);
        setDiscountPerExtra(10);
    }

    public void setMinExtra(int minExtra) {
        this.minExtra = minExtra;
    }

    public void setMaxExtra(int maxExtra) {
        this.maxExtra = maxExtra;
    }

    public void setCostBase(int costBase) {
        this.costBase = costBase;
    }

    public void setDiscountPerExtra(int discountPerExtra) {
        this.discountPerExtra = discountPerExtra;
    }

    @Override
    public int price() {
        int base = super.price();
        int validExtra = Math.min(Math.max(extraPiles, minExtra), maxExtra);
        int totalCost = 0;

        for (int i = 0; i < validExtra; i++) {
            int percent = Math.max(100 - (discountPerExtra * i), 0);
            totalCost += (costBase * percent) / 100;
        }

        return base + totalCost;
    }

    @Override
    public String description() {
        return super.description() + " + " + extraPiles + " extra pile(s)";
    }
}
