package dao;

import model.ProgressNote;

public class ProgressNoteDbmsDao  implements ProgressNoteDao{
    private static ProgressNoteDbmsDao instance;


    public static synchronized ProgressNoteDbmsDao getInstance(){
        if(instance == null){
            instance = new ProgressNoteDbmsDao();
        }
        return instance;
    }


    @Override
    public ProgressNote selectProgressNoteById(String id) {
        return null;
    }

    @Override
    public void saveProgressNote(ProgressNote progressNote, String customOrderId) {

    }
}
