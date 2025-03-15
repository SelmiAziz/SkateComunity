package dao;

import model.Trick;

public interface TrickDao {
    Trick getTrickById();
    void addTrick(Trick trick);
}
