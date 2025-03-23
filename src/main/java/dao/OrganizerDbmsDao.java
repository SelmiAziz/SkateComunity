package dao;

import dao.patternAbstractFactory.DaoFactory;
import login.Role;
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

    public static synchronized  OrganizerDbmsDao getInstance(){
        if(instance == null){
            instance = new OrganizerDbmsDao();
        }
        return instance;
    }

    @Override
    public Organizer selectOrganizerByUsername(String profileName) {
        EventDao eventDao = DaoFactory.getInstance().createEventDao();
        for (Organizer organizer : organizerList) {
            if (organizer.getUsername().equals(profileName)) {
                return organizer;
            }
        }

        String query = "SELECT u.username, u.password, u.dateOfBirth, e.eventName " +
                "FROM users u " +
                "LEFT JOIN events e ON u.username = e.organizerUsername " +
                "WHERE u.username = ?";

        Connection connection = DbsConnector.getInstance().getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, profileName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Organizer organizer = null;
                Set<String> eventNames = new HashSet<>();

                while (resultSet.next()) {
                    if (organizer == null) {
                        String username = resultSet.getString("username");
                        String password = resultSet.getString("password");
                        String dateOfBirth = resultSet.getString("dateOfBirth");
                        organizer = new Organizer(username, password, dateOfBirth);
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


    public void update(Organizer organizer) {
        String query = "UPDATE profiles SET numCoins = ? WHERE profileName = ?";

        Connection connection = DbsConnector.getInstance().getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, 15);
            preparedStatement.setString(2, organizer.getUsername());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addOrganizer(Organizer organizer) {
        organizerList.add(organizer);
        UserDao userDao = DaoFactory.getInstance().createUserDao();
        userDao.addUser(organizer);

    }

}


