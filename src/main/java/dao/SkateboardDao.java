package dao;

import model.decorator.Skateboard;

import java.util.List;

public interface SkateboardDao {
    Skateboard selectSkateboardById(String id);
    Skateboard selectSkateboardByName(String name);
    void addSkateboard(Skateboard skateboard, String idOrder);
    void addSkateboard(Skateboard skateboard);
    List<Skateboard> selectAvailableSkateboards();
}
