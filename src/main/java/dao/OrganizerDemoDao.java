package dao;

import model.Organizer;

import java.util.ArrayList;
import java.util.List;

public class OrganizerDemoDao implements OrganizerDao {
    private static OrganizerDemoDao instance;
    private List<Organizer> organizerList;

    public OrganizerDemoDao() {
        this.organizerList = new ArrayList<>();

    }

    public static synchronized  OrganizerDemoDao getInstance() {
        if (instance == null) {
            instance = new OrganizerDemoDao();
        }
        return instance;
    }


    public void addOrganizer(Organizer organizer){
        this.organizerList.add(organizer);
    }

    public Organizer selectOrganizerByUsername(String username){
        for(Organizer organizer:organizerList){
            if(organizer.getUsername().equals(username)){
                return organizer;
            }
        }
        return null;
    }



    public void update(Organizer organizer) {
        //not needed in demo
    }

}