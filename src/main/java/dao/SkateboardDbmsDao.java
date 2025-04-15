package dao;

import model.decorator.Skateboard;

import java.util.ArrayList;
import java.util.List;

public class SkateboardDbmsDao implements SkateboardDao{
    List<Skateboard> skateboardList = new ArrayList<>();
    List<Skateboard> skateboardListBase = new ArrayList<>();
    private static SkateboardDbmsDao instance;

    public static synchronized  SkateboardDbmsDao getInstance(){
        if(instance == null){
            instance = new SkateboardDbmsDao();
        }
        return instance;
    }

    @Override
    public List<Skateboard> selectAvailableSkateboards() {
        return skateboardListBase;
    }


    @Override
    public void addSkateboard(Skateboard skateboard){
        skateboardListBase.add(skateboard);
    }

    @Override
    public void addSkateboard(Skateboard skateboard, int idOrder) {
        skateboardList.add(skateboard);
    }

    @Override
    public Skateboard selectSkateboardById(int id) {
        for(Skateboard skateboard: skateboardList){
            if(skateboard.skateboardId() == id){
                return skateboard;
            }
        }

        return null;
    }

    @Override
    public Skateboard selectSkateboardByName(String name) {
        for(Skateboard skateboard: skateboardList){
            if(skateboard.name().equals(name)){
                return skateboard;
            }
        }
        return null;
    }
}
