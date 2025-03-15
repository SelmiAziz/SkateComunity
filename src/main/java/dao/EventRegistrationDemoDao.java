package dao;

import model.EventRegistration;

import java.util.ArrayList;
import java.util.List;

public class EventRegistrationDemoDao implements EventRegistrationDao {
    private static EventRegistrationDemoDao instance = null;
    private final List<EventRegistration> eventRegistrationList = new ArrayList<>();

    public static  synchronized EventRegistrationDemoDao getInstance(){
        if(instance == null){
            instance = new EventRegistrationDemoDao();
        }
        return instance;
    }

    @Override
    public void addEventRegistration(EventRegistration eventRegistration) {
        this.eventRegistrationList.add(eventRegistration);
    }

    @Override
    public EventRegistration selectEventRegistrationById(int id) {
        for(EventRegistration eventRegistration : eventRegistrationList){
            if(eventRegistration.getRegistrationId() == id){
                return eventRegistration;
            }
        }
        return null;
    }
}
