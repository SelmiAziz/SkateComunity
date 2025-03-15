package Dao;

import model.EventRegistration;

public class EventRegistrationDemoDao implements EventRegistrationDao {
    private static EventRegistrationDemoDao instance = null;

    public static  synchronized EventRegistrationDemoDao getInstance(){
        if(instance == null){
            instance = new EventRegistrationDemoDao();
        }
        return instance;
    }

    @Override
    public void addEventRegistration(EventRegistration eventRegistration) {

    }

    @Override
    public EventRegistration selectEventRegistrationById(int id) {
        return null;
    }
}
