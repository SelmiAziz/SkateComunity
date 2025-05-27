package dao;

import dao.patternabstractfactory.DaoFactory;
import exceptions.DataAccessException;
import model.Customer;
import model.Registration;
import utils.DbsConnector;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistrationDbmsDao implements RegistrationDao {
    private final CustomerDao customerDao = DaoFactory.getInstance().createCostumerDao();
    private final List<Registration> competitionRegistrationList = new ArrayList<>();
    private static RegistrationDbmsDao instance = null;

    public static synchronized RegistrationDbmsDao getInstance(){
        if(instance == null){
            instance = new RegistrationDbmsDao();
        }
        return instance;
    }

    @Override
    public void addRegistration(Registration competitionRegistration) throws DataAccessException {
        competitionRegistrationList.add(competitionRegistration);

        String sql = "INSERT INTO registrations (numberRegistration, customerUsername, competitionName, registrationCode, assignedSeat, registrationName, email) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conn = DbsConnector.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, competitionRegistration.getParticipationNumber());
            stmt.setString(2, competitionRegistration.getParticipant().getUsername());
            stmt.setString(3, competitionRegistration.getCompetition().getName());
            stmt.setString(4, competitionRegistration.getRegistrationCode());
            stmt.setString(5, competitionRegistration.getAssignedTurn());
            stmt.setString(6, competitionRegistration.getRegistrationName());
            stmt.setString(7, competitionRegistration.getEmail());

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        competitionRegistration.setRegistrationId(generatedId);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public Registration selectRegistrationById(int registrationId) throws DataAccessException{
        for(Registration competitionRegistration: competitionRegistrationList){
            if(competitionRegistration.getRegistrationId() == registrationId){
                return competitionRegistration;
            }
        }

        String sql = "SELECT er.numberRegistration, p.username, er.competitionName, er.registrationCode, er.assignedSeat, er.registrationName, er.email " +
                "FROM registrations er " +
                "JOIN users p ON er.customerUsername = p.username " +
                "WHERE er.idRegistration = ?";

        Connection conn = DbsConnector.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, registrationId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int numberRegistration = rs.getInt("numberRegistration");
                String customerName = rs.getString("username");
                String registrationCode = rs.getString("registrationCode");
                String assignedSeat = rs.getString("assignedSeat");
                String registrationName = rs.getString("registrationName");
                String email = rs.getString("email");

                Registration competitionRegistration = new Registration(registrationId, numberRegistration, registrationCode, assignedSeat, registrationName, email);
                Customer customer = customerDao.selectCustomerByUsername(customerName);
                competitionRegistration.setParticipant(customer);
                return competitionRegistration;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

        return null;
    }
}
