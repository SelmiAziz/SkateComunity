package dao;

import model.decorator.Skateboard;

import java.util.ArrayList;
import java.util.List;

public class SkateboardDemoDao implements SkateboardDao{
    List<Skateboard> skateboardListBase = new ArrayList<>();
    List<Skateboard> skateboardList = new ArrayList<>();

    private static SkateboardDemoDao instance;

    public static synchronized  SkateboardDemoDao getInstance(){
        if(instance == null){
            instance = new SkateboardDemoDao();
        }
        return instance;
    }


    public void addSkateboard(Skateboard skateboard){
        skateboardListBase.add(skateboard);
    }


    @Override
    public List<Skateboard> selectAvailableSkateboards() {
        return skateboardListBase;
    }

    @Override
    public Skateboard selectSkateboardByName(String name) {
        for(Skateboard skateboard : skateboardList){
            if(skateboard.name().equals(name)){
                return skateboard;
            }
        }
        return null;
    }

    @Override
    public Skateboard selectSkateboardById(String id) {
        for(Skateboard skateboard : skateboardList){
            if(skateboard.skateboardId().equals(id)){
                return skateboard;
            }
        }
        return null;
    }

    @Override
    public void addSkateboard(Skateboard skateboard, String idOrder) {
        skateboardList.add(skateboard);
    }
}
