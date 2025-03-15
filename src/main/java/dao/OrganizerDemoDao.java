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

    public synchronized static OrganizerDemoDao getInstance() {
        if (instance == null) {
            instance = new OrganizerDemoDao();
        }
        return instance;
    }


    public void addOrganizer(Organizer organizer){
        this.organizerList.add(organizer);
    }

    public Organizer selectOrganizerByOrganizerName(String organizerName){
        for(Organizer organizer:organizerList){
            if(organizer.getName().equals(organizerName)){
                return organizer;
            }
        }
        return null;
    }

    @Override
    public void update(Organizer organizer) {

    }

}