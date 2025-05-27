package dao;

import exceptions.DataAccessException;
import model.ProgressNote;

public interface ProgressNoteDao {
    void saveProgressNote(ProgressNote progressNote, String customOrderId) throws DataAccessException;
    ProgressNote selectProgressNoteById(String id) throws DataAccessException;
}
