package Dao;

import model.Organizer;

import java.util.ArrayList;
import java.util.List;

public class OrganizerDemoDao implements OrganizerDao {
    private static OrganizerDemoDao instance;
    private List<Organizer> organizerList;
    private AccountDao accountDao;

    public OrganizerDemoDao() {
        this.organizerList = new ArrayList<>();

    }

    public synchronized static OrganizerDemoDao getInstance() {
        if (instance == null) {
            instance = new OrganizerDemoDao();
        }
        return instance;
    }

    public List<Organizer> getAllOrganizers(){
        return this.organizerList;
    }

    public void addOrganizer(Organizer organizer){
        this.organizerList.add(organizer);
    }

    public Organizer getOrganizerByUsername(String username){
        for(Organizer organizer:organizerList){
            if(organizer.getAccount().getUsername().equals(username)){
                return organizer;
            }
        }
        return null;
    }

}