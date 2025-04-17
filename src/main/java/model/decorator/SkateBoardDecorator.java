package model.decorator;

public abstract class  SkateBoardDecorator implements Skateboard{

    private Skateboard skateboard;

    public SkateBoardDecorator(Skateboard skateboard){
        this.skateboard = skateboard;
    }

    @Override
    public String description() {
       return skateboard.description();
    }

    @Override
    public int price() {
        return skateboard.price();
    }

    @Override
    public String name() {
        return skateboard.name();
    }

    @Override
    public String size() {
        return skateboard.size();
    }

    @Override
    public String skateboardId() {
        return skateboard.skateboardId();
    }

}
