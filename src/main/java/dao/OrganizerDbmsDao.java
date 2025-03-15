package dao;

import dao.patternAbstractFactory.DaoFactory;
import model.Event;
import model.Organizer;
import utils.DbsConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrganizerDbmsDao implements OrganizerDao {
    private static OrganizerDbmsDao instance;
    private final List<Organizer> organizerList = new ArrayList<>();

    public synchronized static OrganizerDbmsDao getInstance(){
        if(instance == null){
            instance = new OrganizerDbmsDao();
        }
        return instance;
    }

    @Override
    public Organizer selectOrganizerByOrganizerName(String profileName) {
        EventDao eventDao = DaoFactory.getInstance().createEventDao();
        for (Organizer organizer : organizerList) {
            if (organizer.getName().equals(profileName)) {
                return organizer;
            }
        }

        String query = "SELECT o.profileName, o.numCoins, e.eventName  " +
                "FROM profiles o " +
                "LEFT JOIN events e ON o.profileName = e.organizerName " +
                "WHERE o.profileName = ?";

        Connection connection = DbsConnector.getInstance().getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, profileName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Organizer organizer = null;
                Set<String> eventNames = new HashSet<>();

                while (resultSet.next()) {
                    if (organizer == null) {
                        String pName = resultSet.getString("profileName");
                        int numCoins = resultSet.getInt("numCoins");
                        organizer = new Organizer(pName, numCoins);
                    }

                    String eventName = resultSet.getString("eventName");
                    if (eventName != null) {
                        eventNames.add(eventName);
                    }
                }

                if (organizer != null) {
                    // Recupero gli eventi completi chiamando EventDao
                    for (String eventName : eventNames) {
                        Event event = eventDao.selectEventByName(eventName);

                           organizer.addEvent(event);
                           event.setOrganizer(organizer);
                    }

                    organizerList.add(organizer);
                    return organizer;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void update(Organizer organizer) {
        String query = "UPDATE profiles SET numCoins = ? WHERE profileName = ?";

        Connection connection = DbsConnector.getInstance().getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, organizer.getCoins());
            preparedStatement.setString(2, organizer.getName());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addOrganizer(Organizer organizer) {
        organizerList.add(organizer);
        String query = "INSERT INTO profiles (profileName, numCoins, profileType) VALUES (?, ?, ?)";
        Connection connection = DbsConnector.getInstance().getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, organizer.getName());
            preparedStatement.setInt(2, organizer.getCoins());
            preparedStatement.setString(3, "organizer");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}


