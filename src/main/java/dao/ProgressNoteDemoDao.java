package dao;

import model.ProgressNote;


public class ProgressNoteDemoDao implements ProgressNoteDao {
    private static ProgressNoteDemoDao instance ;


    public static synchronized ProgressNoteDemoDao getInstance(){
        if(instance == null){
            instance = new ProgressNoteDemoDao();
        }
        return instance;
    }

    @Override
    public void saveProgressNote(ProgressNote progressNote, String customOrderId) {
        //it is not necessary
    }

    @Override
    public ProgressNote selectProgressNoteById(String id) {
        return null;
    }
}
