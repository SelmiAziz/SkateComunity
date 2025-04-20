package dao;

import model.ProgressNote;

public interface ProgressNoteDao {
    void saveProgressNote(ProgressNote progressNote, String customOrderId);
}
