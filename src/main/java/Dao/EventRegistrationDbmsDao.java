package Dao;

import Dao.patternAbstractFactory.DaoFactory;
import model.Costumer;
import model.EventRegistration;
import utils.DbsConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventRegistrationDbmsDao implements EventRegistrationDao{
    private final CostumerDao costumerDao = DaoFactory.getInstance().createCostumerDao();
    private final List<EventRegistration> eventRegistrationList = new ArrayList<>();
    private static EventRegistrationDbmsDao instance = null;

    public  static synchronized EventRegistrationDbmsDao getInstance(){
        if(instance == null){
            instance = new EventRegistrationDbmsDao();
        }
        return instance;
    }


    @Override
    public void addEventRegistration(EventRegistration eventRegistration) {
        String sql = "INSERT INTO registrations (numberRegistration, costumerName, eventName) " +
                "VALUES (?, ?, ?)";


        Connection conn = DbsConnector.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, eventRegistration.getParticipationNumber());
            stmt.setString(2, eventRegistration.getParticipant().getName());
            stmt.setString(3, eventRegistration.getEvent().getName());

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Override
    public EventRegistration selectEventRegistrationById(int registrationId) {
        for(EventRegistration eventRegistration: eventRegistrationList){
            if(eventRegistration.getRegistrationId()  ==  registrationId){
                return eventRegistration;
            }
        }
        String sql = "SELECT er.numberRegistration, p.profileName, er.eventName " +
                    "FROM registrations er " +
                    "JOIN profiles p ON er.costumerName = p.profileName " +
                    "WHERE er.idRegistration = ?";

        Connection conn = DbsConnector.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, registrationId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int numberRegistration = rs.getInt("numberRegistration");
                String customerName = rs.getString("profileName");

                EventRegistration eventRegistration = new EventRegistration(registrationId, numberRegistration);
                Costumer customer = costumerDao.selectCostumerByCostumerName(customerName);
                eventRegistration.setParticipant(customer);
                return eventRegistration;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

            return null;
    }


}


