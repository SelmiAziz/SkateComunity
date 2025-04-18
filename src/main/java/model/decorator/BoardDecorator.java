package model.decorator;

public abstract class BoardDecorator implements Board {

    private Board board;

    public BoardDecorator(Board skateboard){
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
    public String boardId() {
        return board.boardId();
    }

}
