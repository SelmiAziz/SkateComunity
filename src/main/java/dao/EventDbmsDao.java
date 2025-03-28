package dao;

import dao.patternAbstractFactory.DaoFactory;
import model.Event;
import model.EventRegistration;
import model.Organizer;
import utils.DbsConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventDbmsDao implements EventDao {
    private static EventDbmsDao instance = null;
    private final OrganizerDao organizerDao = DaoFactory.getInstance().createOrganizerDao();
    private final EventRegistrationDao eventRegistrationDao = DaoFactory.getInstance().createEventRegistrationDao();
    private final List<Event> eventList = new ArrayList<>();

    public static synchronized EventDbmsDao getInstance(){
        if(instance == null){
            instance = new EventDbmsDao();
        }
        return instance;
    }

    @Override

    public Event selectEventByName(String eventName) {
        for(Event event:eventList){
            if(event.getName().equals(eventName)){
                return event;
            }
        }


        String sql = "SELECT e.eventName, " +
                "e.description, " +
                "e.coinsRequired, " +
                "e.date, " +
                "e.location, " +
                "e.MaxRegistrationNumber, " +
                "GROUP_CONCAT(r.idRegistration) AS registrationIds " +
                "FROM events e " +
                "LEFT JOIN registrations r ON e.eventName = r.eventName " +
                "WHERE e.eventName = ? " +
                "GROUP BY e.eventName, e.description, e.coinsRequired, e.date, e.location, e.MaxRegistrationNumber";

        Connection conn = DbsConnector.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, eventName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Event event = new Event(
                        eventName,
                        rs.getString("description"),
                        rs.getString("date"),
                        rs.getString("location"),
                        rs.getInt("coinsRequired"),
                        rs.getInt("MaxRegistrationNumber")
                );


                String registrationIdsStr = rs.getString("registrationIds");
                if (registrationIdsStr != null && !registrationIdsStr.isEmpty()) {
                    String[] registrationIds = registrationIdsStr.split(",");
                    for (String id : registrationIds) {
                        int registrationId = Integer.parseInt(id);
                        EventRegistration eventRegistration = eventRegistrationDao.selectEventRegistrationById(registrationId);
                        eventRegistration.setEvent(event);
                        event.addEventRegistration(eventRegistration);
                    }
                }
                eventList.add(event);
                return event;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    public List<Event> selectAvailableEvents() {
        List<Event> newEventList = new ArrayList<>();
        for(Event event:this.eventList){
            if(event.getCurrentRegistrations() < event.getCurrentRegistrations()){
                newEventList.add(event);
            }
        }

        if(!newEventList.isEmpty()){
            return newEventList;
        }

        EventRegistrationDao eventRegistrationDao = DaoFactory.getInstance().createEventRegistrationDao();
        List<Event> events = new ArrayList<>();
        List<String> organizerUsernames = new ArrayList<>(); // Lista dei nomi degli organizzatori
        List<String> registrationIdsList = new ArrayList<>(); // Lista delle stringhe con ID registrazioni

        String sql = "SELECT e.eventName, " +
                "e.description, " +
                "e.coinsRequired, " +
                "e.date, " +
                "e.location, " +
                "e.organizerUsername, " +
                "e.MaxRegistrationNumber, " +
                "GROUP_CONCAT(r.idRegistration) AS registrationIds " +
                "FROM events e " +
                "LEFT JOIN registrations r ON e.eventName = r.eventName " +
                "GROUP BY e.eventName, e.description, e.coinsRequired, e.date, e.location, e.MaxRegistrationNumber, e.organizerUsername " +
                "HAVING COUNT(r.idRegistration) < e.MaxRegistrationNumber";

        try (
                Connection conn = DbsConnector.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Event event = new Event(
                        rs.getString("eventName"),
                        rs.getString("description"),
                        rs.getString("date"),
                        rs.getString("location"),
                        rs.getInt("coinsRequired"),
                        rs.getInt("MaxRegistrationNumber")
                );

                events.add(event);
                organizerUsernames.add(rs.getString("organizerUsername"));
                registrationIdsList.add(rs.getString("registrationIds"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            this.eventList.add(event);
            String organizerUsername = organizerUsernames.get(i);
            if (organizerUsername != null) {
                Organizer organizer = organizerDao.selectOrganizerByUsername(organizerUsername);
                if (organizer != null) {
                    event.setOrganizer(organizer);
                }
            }
            String registrationIdsStr = registrationIdsList.get(i);
            if (registrationIdsStr != null && !registrationIdsStr.isEmpty()) {
                for (String id : registrationIdsStr.split(",")) {
                    int registrationId = Integer.parseInt(id);
                    EventRegistration eventRegistration = eventRegistrationDao.selectEventRegistrationById(registrationId);
                    if (eventRegistration != null) {
                        eventRegistration.setEvent(event);
                        event.addEventRegistration(eventRegistration);
                    }
                }
            }
        }

        return events;
    }


    @Override
    public boolean checkEvent(String eventName) {
        for(Event event:eventList){
            if(event.getName().equals(eventName)){
                return true;
            }
        }
        String sql = "SELECT COUNT(*) FROM events WHERE eventName = ?";

        Connection conn = DbsConnector.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;


    }

    @Override
    public List<Event> selectEventsByDateAndLocation(String date, String location) {
        List<Event> newEventList = new ArrayList<>();

        //first I look in memory
        for (Event event : this.eventList) {
            if (event.getDate().equals(date) && event.getLocation().equals(location)) {
                newEventList.add(event);
            }
        }
        if (!newEventList.isEmpty()) {
            return newEventList;
        }

        // Query if there is nothing in memory
        String sql = "SELECT e.eventName, " +
                "e.description, " +
                "e.coinsRequired, " +
                "e.date, " +
                "e.location, " +
                "e.organizerUsername, " +
                "e.MaxRegistrationNumber, " +
                "GROUP_CONCAT(r.idRegistration) AS registrationIds " +
                "FROM events e " +
                "LEFT JOIN registrations r ON e.eventName = r.eventName " +
                "WHERE e.date = ? AND e.location = ? " +
                "GROUP BY e.eventName, e.description, e.coinsRequired, e.date, e.location, e.MaxRegistrationNumber, e.organizerUsername";

        List<Event> events = new ArrayList<>();
        List<String> organizerUsernames = new ArrayList<>();
        List<String> registrationIdsList = new ArrayList<>();

        Connection conn = DbsConnector.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, date);
            stmt.setString(2, location);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Event event = new Event(
                            rs.getString("eventName"),
                            rs.getString("description"),
                            rs.getString("date"),
                            rs.getString("location"),
                            rs.getInt("coinsRequired"),
                            rs.getInt("MaxRegistrationNumber")
                    );

                    events.add(event);
                    eventList.add(event);
                    organizerUsernames.add(rs.getString("organizerUsername"));
                    registrationIdsList.add(rs.getString("registrationIds"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            this.eventList.add(event);

            String organizerUsername = organizerUsernames.get(i);
            if (organizerUsername != null) {
                Organizer organizer = organizerDao.selectOrganizerByUsername(organizerUsername);
                if (organizer != null) {
                    event.setOrganizer(organizer);
                }
            }

            String registrationIdsStr = registrationIdsList.get(i);
            if (registrationIdsStr != null && !registrationIdsStr.isEmpty()) {
                for (String id : registrationIdsStr.split(",")) {
                    int registrationId = Integer.parseInt(id);
                    EventRegistration eventRegistration = eventRegistrationDao.selectEventRegistrationById(registrationId);
                    if (eventRegistration != null) {
                        eventRegistration.setEvent(event);
                        event.addEventRegistration(eventRegistration);
                    }
                }
            }
        }

        return events;
    }

    @Override
    public void addEvent(Event event) {

        String sql = "INSERT INTO events " +
                "(eventName, " +
                "coinsRequired, " +
                "description, " +
                "date, " +
                "MaxRegistrationNumber, " +
                "currentRegistrationNumber, " +
                "organizerUsername, " +
                "location) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Connection connection = DbsConnector.getInstance().getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, event.getName());
                stmt.setInt(2, event.getParticipationFee());
                stmt.setString(3, event.getDescription());
                stmt.setString(4, event.getDate());
                stmt.setInt(5, event.getMaxRegistrations());
                stmt.setInt(6, event.getCurrentRegistrations()); // Aggiunto currentRegistration
                stmt.setString(7, event.getOrganizer().getUsername());
                stmt.setString(8, event.getLocation());

                stmt.executeUpdate();

                for (EventRegistration eventRegistration : event.getEventRegistrations()) {
                    eventRegistrationDao.addEventRegistration(eventRegistration);
                }
            }catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
