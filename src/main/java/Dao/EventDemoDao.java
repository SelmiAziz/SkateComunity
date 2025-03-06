package Dao;

import model.Event;

import java.util.ArrayList;
import java.util.List;


public class EventDemoDao implements EventDao {
    private static EventDemoDao instance;
    private List<Event> eventList;

    public EventDemoDao(){
        this.eventList = new ArrayList<>();
    }

    public synchronized static EventDemoDao getInstance(){
        if(instance == null){
            instance = new EventDemoDao();
        }
        return instance;
    }

    public List<Event> getEventsByDateAndCountry(String date, String country) {
        List<Event> newEventList = new ArrayList<>();
        for(Event event:this.eventList){
            if( event.getDate().equals(date) && event.getCountry().equals(country)){
                newEventList.add(event);
            }
        }
        return newEventList;
    }

    public void removeEventByName(String name){

    }

    public Event getEventByName(String name){
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

    public List<Event> getAllEvents(){
        return eventList;
    }

    public void printAllEvents() {
        if (eventList.isEmpty()) {
            System.out.println("Non ci sono eventi disponibili.");
        } else {
            for (Event event : eventList) {
                System.out.println("Titolo: " + event.getName());
                System.out.println("Descrizione: " + event.getDescription());
                System.out.println("Data: " + event.getDate());
                System.out.println("---");
            }
        }
    }
}
