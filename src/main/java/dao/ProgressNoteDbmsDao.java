package dao;

public class ProgressNoteDbmsDao {
    private static ProgressNoteDbmsDao instance;


    public static synchronized ProgressNoteDbmsDao getInstance(){
        if(instance == null){
            instance = new ProgressNoteDbmsDao();
        }
        return instance;
    }
}
