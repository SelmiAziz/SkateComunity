package model.decorator;

public class ExtraPilesDecorator extends BoardDecorator {

    private Board board;
    private int extraPiles; // 1 o 2
    private int minExtra;
    private int maxExtra;
    private int costForOne;
    private int costForTwo;

    public ExtraPilesDecorator(Board board, int extraPiles) {
        super(board);
        this.board = board;
        this.extraPiles = extraPiles;
        setMinExtra(0);
        setMaxExtra(2);
        setCostForOne(7);  // se aggiungi una sola pile
        setCostForTwo(18); // se ne aggiungi due, è più costoso
    }

    public void setMinExtra(int minExtra) {
        this.minExtra = minExtra;
    }

    public void setMaxExtra(int maxExtra) {
        this.maxExtra = maxExtra;
    }

    public void setCostForOne(int costForOne) {
        this.costForOne = costForOne;
    }

    public void setCostForTwo(int costForTwo) {
        this.costForTwo = costForTwo;
    }

    @Override
    public int price() {
        int base = super.price();
        int validExtra = Math.max(minExtra, Math.min(maxExtra, extraPiles));
        int cost = 0;
        if (validExtra == 1) {
            cost = costForOne;
        } else if (validExtra == 2) {
            cost = costForTwo;
        }
        return base + cost;
    }

    @Override
    public String description() {
        return super.description() + " + " + extraPiles + " extra pile(s)";
    }
}

