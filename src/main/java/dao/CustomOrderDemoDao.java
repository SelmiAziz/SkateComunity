package dao;

public class CustomOrderDemoDao {
    private static CustomOrderDemoDao instance;


    public static synchronized CustomOrderDemoDao getInstance(){
        if(instance == null){
            instance = new CustomOrderDemoDao();
        }
        return instance;
    }
}
