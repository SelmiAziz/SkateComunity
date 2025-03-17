package dao;

import model.Trick;

import java.util.ArrayList;
import java.util.List;

public class TrickDemoDao implements TrickDao {
    private static  TrickDemoDao instance;
    List<Trick> trickList = new ArrayList<>();

    public static synchronized TrickDemoDao getInstance(){
        if(instance == null){
            instance = new TrickDemoDao();
        }
        return instance;
    }

    @Override
    public Trick getTrickByName(String trickName) {
        return null;
    }

    @Override
    public void addTrick(Trick trick) {
        trickList.add(trick);
            }
}



