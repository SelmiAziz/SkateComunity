package dao;

import model.Trick;

import java.util.List;

public interface TrickDao {
    Trick selectTrickByName(String trickName);
    void addTrick(Trick trick);
    List<Trick> selectAvailableTricks();
}
