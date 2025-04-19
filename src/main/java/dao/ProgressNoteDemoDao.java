package dao;

public class ProgressNoteDemoDao {
    private static ProgressNoteDemoDao instance;


    public static synchronized ProgressNoteDemoDao getInstance(){
        if(instance == null){
            instance = new ProgressNoteDemoDao();
        }
        return instance;
    }
}
