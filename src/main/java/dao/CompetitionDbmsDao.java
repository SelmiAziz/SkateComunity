package dao;

import dao.patternAbstractFactory.DaoFactory;
import model.Competition;
import model.Registration;
import model.Organizer;
import utils.DbsConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompetitionDbmsDao implements CompetitionDao {
    private static CompetitionDbmsDao instance = null;
    private final OrganizerDao organizerDao = DaoFactory.getInstance().createOrganizerDao();
    private final RegistrationDao competitionRegistrationDao = DaoFactory.getInstance().createRegistrationDao();
    private final List<Competition> competitionList = new ArrayList<>();

    public static synchronized CompetitionDbmsDao getInstance(){
        if(instance == null){
            instance = new CompetitionDbmsDao();
        }
        return instance;
    }

    @Override
    public Competition selectCompetitionByName(String competitionName) {
        for(Competition competition: this.competitionList){
            if(competition.getName().equals(competitionName)){
                return competition;
            }
        }
        String sql = "SELECT e.competitionName, " +
                "e.description, " +
                "e.coinsRequired, " +
                "e.date, " +
                "e.location, " +
                "e.MaxRegistrationNumber, " +
                "GROUP_CONCAT(r.idRegistration) AS registrationIds " +
                "FROM competitions e " +
                "LEFT JOIN registrations r ON e.competitionName = r.competitionName " +
                "WHERE e.competitionName = ? " +
                "GROUP BY e.competitionName, e.description, e.coinsRequired, e.date, e.location, e.MaxRegistrationNumber";

        Connection conn = DbsConnector.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, competitionName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Competition competition = new Competition(
                        competitionName,
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
                        Registration competitionRegistration = competitionRegistrationDao.selectRegistrationById(registrationId);
                        competitionRegistration.setCompetition(competition);
                        competition.addCompetitionRegistration(competitionRegistration);
                    }
                }
                this.competitionList.add(competition);
                return competition;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Competition> selectAvailableCompetitions() {
        List<Competition> newCompetitionList = new ArrayList<>();
        for(Competition competition : this.competitionList){
            if(competition.getCurrentRegistrations() < competition.getMaxRegistrations()){
                newCompetitionList.add(competition);
            }
        }

        if(!newCompetitionList.isEmpty()){
            return newCompetitionList;
        }

        RegistrationDao competitionRegistrationDao = DaoFactory.getInstance().createRegistrationDao();
        List<Competition> competitions = new ArrayList<>();
        List<String> organizerUsernames = new ArrayList<>(); // Lista dei nomi degli organizzatori
        List<String> registrationIdsList = new ArrayList<>(); // Lista delle stringhe con ID registrazioni
        String sql = "SELECT e.competitionName, " +
                "e.description, " +
                "e.coinsRequired, " +
                "e.date, " +
                "e.location, " +
                "e.organizerUsername, " +
                "e.MaxRegistrationNumber, " +
                "GROUP_CONCAT(r.idRegistration) AS registrationIds " +
                "FROM competitions e " +
                "LEFT JOIN registrations r ON e.competitionName = r.competitionName " +
                "GROUP BY e.competitionName, e.description, e.coinsRequired, e.date, e.location, e.MaxRegistrationNumber, e.organizerUsername " +
                "HAVING COUNT(r.idRegistration) < e.MaxRegistrationNumber";
        try (
                Connection conn = DbsConnector.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Competition competition = new Competition(
                        rs.getString("competitionName"),
                        rs.getString("description"),
                        rs.getString("date"),
                        rs.getString("location"),
                        rs.getInt("coinsRequired"),
                        rs.getInt("MaxRegistrationNumber")
                );
                this.competitionList.add(competition);
                competitions.add(competition);
                organizerUsernames.add(rs.getString("organizerUsername"));
                registrationIdsList.add(rs.getString("registrationIds"));
            }

            for (int i = 0; i < competitions.size(); i++) {
                Competition competition = competitions.get(i);
                String organizerUsername = organizerUsernames.get(i);
                if (organizerUsername != null) {
                    Organizer organizer = organizerDao.selectOrganizerByUsername(organizerUsername);
                    if (organizer != null) {
                        competition.setOrganizer(organizer);
                    }
                }
                String registrationIdsStr = registrationIdsList.get(i);
                if (registrationIdsStr != null && !registrationIdsStr.isEmpty()) {
                    for (String id : registrationIdsStr.split(",")) {
                        int registrationId = Integer.parseInt(id);
                        Registration competitionRegistration = competitionRegistrationDao.selectRegistrationById(registrationId);
                        if (competitionRegistration != null) {
                            competitionRegistration.setCompetition(competition);
                            competition.addCompetitionRegistration(competitionRegistration);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return competitions;
    }

    @Override
    public boolean checkCompetition(String competitionName) {
        for(Competition competition:competitionList){
            if(competition.getName().equals(competitionName)){
                return true;
            }
        }
        String sql = "SELECT COUNT(*) FROM competitions WHERE competitionName = ?";

        Connection conn = DbsConnector.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, competitionName);

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
    public List<Competition> selectCompetitionsByDateAndLocation(String date, String location) {
        List<Competition> newCompetitionList = new ArrayList<>();

        //first I look in memory
        for (Competition competition : this.competitionList) {
            if (competition.getDate().equals(date) && competition.getLocation().equals(location)) {
                newCompetitionList.add(competition);
            }
        }
        if (!newCompetitionList.isEmpty()) {
            return newCompetitionList;
        }

        // Query if there is nothing in memory
        String sql = "SELECT e.competitionName, " +
                "e.description, " +
                "e.coinsRequired, " +
                "e.date, " +
                "e.location, " +
                "e.organizerUsername, " +
                "e.MaxRegistrationNumber, " +
                "GROUP_CONCAT(r.idRegistration) AS registrationIds " +
                "FROM competitions e " +
                "LEFT JOIN registrations r ON e.competitionName = r.competitionName " +
                "WHERE e.date = ? AND e.location = ? " +
                "GROUP BY e.competitionName, e.description, e.coinsRequired, e.date, e.location, e.MaxRegistrationNumber, e.organizerUsername";

        List<Competition> competitions = new ArrayList<>();
        List<String> organizerUsernames = new ArrayList<>();
        List<String> registrationIdsList = new ArrayList<>();

        Connection conn = DbsConnector.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, date);
            stmt.setString(2, location);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Competition competition = new Competition(
                            rs.getString("competitionName"),
                            rs.getString("description"),
                            rs.getString("date"),
                            rs.getString("location"),
                            rs.getInt("coinsRequired"),
                            rs.getInt("MaxRegistrationNumber")
                    );

                    competitions.add(competition);
                    this.competitionList.add(competition);
                    organizerUsernames.add(rs.getString("organizerUsername"));
                    registrationIdsList.add(rs.getString("registrationIds"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < competitions.size(); i++) {
            Competition competition = competitions.get(i);

            String organizerUsername = organizerUsernames.get(i);
            if (organizerUsername != null) {
                Organizer organizer = organizerDao.selectOrganizerByUsername(organizerUsername);
                if (organizer != null) {
                    competition.setOrganizer(organizer);
                }
            }

            String registrationIdsStr = registrationIdsList.get(i);
            if (registrationIdsStr != null && !registrationIdsStr.isEmpty()) {
                for (String id : registrationIdsStr.split(",")) {
                    int registrationId = Integer.parseInt(id);
                    Registration competitionRegistration = competitionRegistrationDao.selectRegistrationById(registrationId);
                    if (competitionRegistration != null) {
                        competitionRegistration.setCompetition(competition);
                        competition.addCompetitionRegistration(competitionRegistration);
                    }
                }
            }
        }

        return competitions;
    }

    @Override
    public void addCompetition(Competition competition) {
        this.competitionList.add(competition);

        String sql = "INSERT INTO competitions " +
                "(competitionName, " +
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
            stmt.setString(1, competition.getName());
            stmt.setInt(2, competition.getParticipationFee());
            stmt.setString(3, competition.getDescription());
            stmt.setString(4, competition.getDate());
            stmt.setInt(5, competition.getMaxRegistrations());
            stmt.setInt(6, competition.getCurrentRegistrations()); // Aggiunto currentRegistration
            stmt.setString(7, competition.getOrganizer().getUsername());
            stmt.setString(8, competition.getLocation());

            stmt.executeUpdate();

            for (Registration competitionRegistration : competition.getCompetitionRegistrations()) {
                competitionRegistrationDao.addRegistration(competitionRegistration);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

