package dao;

import dao.patternAbstractFactory.DaoFactory;
import model.Customer;
import model.CompetitionRegistration;
import utils.DbsConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompetitionRegistrationDbmsDao implements CompetitionRegistrationDao {
    private final CustomerDao customerDao = DaoFactory.getInstance().createCostumerDao();
    private final List<CompetitionRegistration> competitionRegistrationList = new ArrayList<>();
    private static CompetitionRegistrationDbmsDao instance = null;

    public static synchronized CompetitionRegistrationDbmsDao getInstance(){
        if(instance == null){
            instance = new CompetitionRegistrationDbmsDao();
        }
        return instance;
    }

    @Override
    public void addCompetitionRegistration(CompetitionRegistration competitionRegistration) {
        competitionRegistrationList.add(competitionRegistration);

        String sql = "INSERT INTO registrations (numberRegistration, customerUsername, competitionName, registrationCode, assignedSeat) " +
                "VALUES (?, ?, ?, ?, ?)";

        Connection conn = DbsConnector.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, competitionRegistration.getParticipationNumber());
            stmt.setString(2, competitionRegistration.getParticipant().getUsername());
            stmt.setString(3, competitionRegistration.getCompetition().getName());
            stmt.setString(4, competitionRegistration.getRegistrationCode());
            stmt.setString(5, competitionRegistration.getAssignedTurn());

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
            e.printStackTrace();
        }
    }

    @Override
    public CompetitionRegistration selectCompetitionRegistrationById(int registrationId) {
        for(CompetitionRegistration competitionRegistration: competitionRegistrationList){
            if(competitionRegistration.getRegistrationId() == registrationId){
                return competitionRegistration;
            }
        }

        String sql = "SELECT er.numberRegistration, p.username, er.competitionName, er.registrationCode, er.assignedSeat " +
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

                CompetitionRegistration competitionRegistration = new CompetitionRegistration(registrationId, numberRegistration, registrationCode, assignedSeat);
                Customer customer = customerDao.selectCustomerByUsername(customerName);
                competitionRegistration.setParticipant(customer);
                return competitionRegistration;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
