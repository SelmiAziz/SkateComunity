package model.decorator;

public class WarrantyDecorator extends BoardDecorator {

    private Board board;
    private int warrantyMonths;
    private int minWarrantyMonths;
    private int maxWarrantyMonths;
    private double costForFirstPeriod;
    private double costForSecondPeriod;
    private int firstPeriodMonths;
    private int secondPeriodMonths;

    public WarrantyDecorator(Board board, int warrantyMonths) {
        super(board);
        this.board = board;
        this.warrantyMonths = warrantyMonths;
        setMinWarrantyMonths(0);
        setMaxWarrantyMonths(12);
        setFirstPeriodMonths(6); // primi 6 mesi
        setCostForFirstPeriod(0.5); // costo per il primo periodo
        setCostForSecondPeriod(1.2); // costo per il secondo periodo
    }

    public void setMinWarrantyMonths(int minWarrantyMonths) {
        this.minWarrantyMonths = minWarrantyMonths;
    }

    public void setMaxWarrantyMonths(int maxWarrantyMonths) {
        this.maxWarrantyMonths = maxWarrantyMonths;
    }

    public void setFirstPeriodMonths(int firstPeriodMonths) {
        this.firstPeriodMonths = firstPeriodMonths;
    }

    public void setCostForFirstPeriod(double costForFirstPeriod) {
        this.costForFirstPeriod = costForFirstPeriod;
    }

    public void setCostForSecondPeriod(double costForSecondPeriod) {
        this.costForSecondPeriod = costForSecondPeriod;
    }

    @Override
    public int price() {
        int base = super.price();
        int validMonths = Math.max(minWarrantyMonths, Math.min(warrantyMonths, maxWarrantyMonths)); // limita tra 0 e 12
        int firstPeriod = Math.min(validMonths, firstPeriodMonths); // primi 6 mesi
        int secondPeriod = Math.max(0, validMonths - firstPeriodMonths); // mesi successivi

        double totalCost = (firstPeriod * costForFirstPeriod) + (secondPeriod * costForSecondPeriod);
        return base + (int) Math.round(totalCost);
    }

    @Override
    public String description() {
        return super.description() + " + " + warrantyMonths + " months warranty";
    }
}

