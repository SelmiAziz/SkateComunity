package dao;

import model.Trick;

public interface TrickDao {
    Trick getTrickByName(String trickName);
    void addTrick(Trick trick);
}
