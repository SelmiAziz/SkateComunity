package dao;

import model.Trick;

import java.util.ArrayList;
import java.util.List;

public class TrickDbmsDao implements TrickDao{
    private static TrickDbmsDao instance;
    private List<Trick> trickList = new ArrayList<>();

    public static synchronized TrickDbmsDao getInstance(){
        if(instance == null){
            instance = new TrickDbmsDao();
        }
        return instance;
    }


    @Override
    public void addTrick(Trick trick) {
        this.trickList.add(trick);
    }

    @Override
    public Trick getTrickByName(String trickName) {
        for(Trick trick:this.trickList){
            if(trick.equals(trickName)){
                return trick;
            }
        }
        return null;
    }
}
