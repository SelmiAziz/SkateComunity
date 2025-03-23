package dao;

import model.Event;

import java.util.ArrayList;
import java.util.List;


public class EventDemoDao implements EventDao {
    private static EventDemoDao instance = null;
    private final List<Event> eventList = new ArrayList<>();

    public static synchronized EventDemoDao getInstance(){
        if(instance == null){
            instance = new EventDemoDao();
        }
        return instance;
    }

    public List<Event> selectEventsByDateAndLocation(String date, String location) {
        List<Event> newEventList = new ArrayList<>();
        for(Event event:this.eventList){
            if( event.getDate().equals(date) && event.getLocation().equals(location)){
                newEventList.add(event);
            }
        }
        return newEventList;
    }

    @Override
    public boolean checkEvent(String eventName) {
        for(Event event:eventList){
            if(event.getName().equals(eventName)){
                return true;
            }
        }
        return false;
    }


    @Override
    public List<Event> selectAvailableEvents() {
        List<Event> eventList= new ArrayList<>();
        for(Event event:this.eventList){
            if(event.getCurrentRegistrations()< event.getMaxRegistrations()){
                eventList.add(event);
            }
        }
        return eventList;
    }

    public Event selectEventByName(String name){
        for(Event event:eventList){
            if(event.getName().equals(name)){
                return event;
            }
        }
        return null;
    }
    public void addEvent(Event event){
        eventList.add(event);
    }

    public List<Event> selectAllEvents(){
        return eventList;
    }


}
