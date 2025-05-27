package dao;

import exceptions.DataAccessException;
import model.Trick;

import java.util.List;

public interface TrickDao {
    Trick selectTrickByName(String trickName) throws DataAccessException;
    void addTrick(Trick trick) throws DataAccessException;
    List<Trick> selectAvailableTricks() throws DataAccessException;
}
