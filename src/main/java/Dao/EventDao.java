package Dao;

import model.Event;

import java.sql.SQLException;
import java.util.List;

public interface EventDao {
    Event selectEventByName(String nameEvent);
    boolean checkEvent(String eventName);
    void addEvent(Event event) throws SQLException;
    List<Event> selectAvailableEvents();
    List<Event> selectEventsByDateAndCountry(String date, String country);
}
