package model.decorator;

public abstract class BoardDecorator implements Board {

    private Board board;

    protected BoardDecorator(Board skateboard){
        this.board = skateboard;
    }

    @Override
    public String description() {
       return board.description();
    }

    @Override
    public int price() {
        return board.price();
    }

    @Override
    public String name() {
        return board.name();
    }

    @Override
    public String size() {
        return board.size();
    }

    @Override
    public String boardCode() {
        return board.boardCode();
    }

}
