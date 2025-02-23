package Dao;

import model.Event;

import java.util.ArrayList;
import java.util.List;


public class EventDemoDao implements EventDao {
    private static EventDemoDao instance;
    private List<Event> events;

    public EventDemoDao(){
        events = new ArrayList<>();
    }

    public synchronized static EventDemoDao getInstance(){
        if(instance == null){
            instance = new EventDemoDao();
        }
        return instance;
    }

    public List<Event> getEventsByDateAndCountry() {
        return events;
    }

    public void removeEventByName(String name){

    }

    public Event getEventByName(String name){
        return new Event("a","d","s",30,"o",10);
    }
    public void addEvent(Event event){
        events.add(event);
    }

    public List<Event> getAllEvents(){
        return events;
    }

    public void printAllEvents() {
        if (events.isEmpty()) {
            System.out.println("Non ci sono eventi disponibili.");
        } else {
            for (Event event : events) {
                System.out.println("Titolo: " + event.getName());
                System.out.println("Descrizione: " + event.getDescription());
                System.out.println("Data: " + event.getDate());
                System.out.println("---");
            }
        }
    }
}
