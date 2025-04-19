package dao;

public class CustomOrderDbmsDao {
    private static CustomOrderDbmsDao instance;


    public static synchronized CustomOrderDbmsDao getInstance(){
        if(instance == null){
            instance = new CustomOrderDbmsDao();
        }
        return instance;
    }
}
