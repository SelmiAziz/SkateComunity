package dao;

import model.decorator.Skateboard;

import java.util.List;

public interface SkateboardDao {
    Skateboard selectSkateboardById(int id);
    Skateboard selectSkateboardByName(String name);
    void addSkateboard(Skateboard skateboard, int idOrder);
    void addSkateboard(Skateboard skateboard);
    List<Skateboard> selectAvailableSkateboards();
}
