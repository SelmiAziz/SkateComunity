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

    private static final String DESCRIPTION = "description";
    private static final String LOCATION = "location";
    private static final String COINSREQUIRE = "coinsRequired";
    private static final String MAXREGISTRATIONNUMBER = "MaxRegistrationNumber";
    private static final String DATE = "date";
    private static final String REGISTRATIONIDS = "registrationIds";

    public static synchronized CompetitionDbmsDao getInstance() {
        if (instance == null) {
            instance = new CompetitionDbmsDao();
        }
        return instance;
    }

    @Override
    public Competition selectCompetitionByName(String competitionName) {
        for (Competition competition : this.competitionList) {
            if (competition.getName().equals(competitionName)) {
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
                        rs.getString(DESCRIPTION),
                        rs.getString(DATE),
                        rs.getString(LOCATION),
                        rs.getInt(COINSREQUIRE),
                        rs.getInt(MAXREGISTRATIONNUMBER)
                );

                String registrationIdsStr = rs.getString(REGISTRATIONIDS);
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
        List<Competition> availableCompetitions = new ArrayList<>();
        for (Competition competition : this.competitionList) {
            if (competition.getRegistrationsNumber() < competition.getMaxRegistrations()) {
                availableCompetitions.add(competition);
            }
        }

        if (!availableCompetitions.isEmpty()) {
            return availableCompetitions;
        }

        return loadAvailableCompetitionsFromDb();
    }

    private List<Competition> loadAvailableCompetitionsFromDb() {
        List<Competition> competitions = new ArrayList<>();

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
                "GROUP BY e.competitionName, e.description, e.coinsRequired, e.date, e.location, " +
                "e.MaxRegistrationNumber, e.organizerUsername " +
                "HAVING COUNT(r.idRegistration) < e.MaxRegistrationNumber";

        try (
                Connection conn = DbsConnector.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                Competition competition = buildCompetitionFromResultSet(rs);
                setOrganizerAndRegistrations(competition, rs);
                this.competitionList.add(competition);
                competitions.add(competition);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return competitions;
    }

    private Competition buildCompetitionFromResultSet(ResultSet rs) throws SQLException {
        return new Competition(
                rs.getString("competitionName"),
                rs.getString(DESCRIPTION),
                rs.getString(DATE),
                rs.getString(LOCATION),
                rs.getInt(COINSREQUIRE),
                rs.getInt(MAXREGISTRATIONNUMBER)
        );
    }

    private void setOrganizerAndRegistrations(Competition competition, ResultSet rs) throws SQLException {
        String organizerUsername = rs.getString("organizerUsername");
        if (organizerUsername != null) {
            Organizer organizer = organizerDao.selectOrganizerByUsername(organizerUsername);
            if (organizer != null) {
                competition.setOrganizer(organizer);
            }
        }

        String registrationIdsStr = rs.getString(REGISTRATIONIDS);
        if (registrationIdsStr != null && !registrationIdsStr.isEmpty()) {
            for (String id : registrationIdsStr.split(",")) {
                int registrationId = Integer.parseInt(id);
                Registration registration = competitionRegistrationDao.selectRegistrationById(registrationId);
                if (registration != null) {
                    registration.setCompetition(competition);
                    competition.addCompetitionRegistration(registration);
                }
            }
        }
    }

    @Override
    public boolean checkCompetition(String competitionName) {
        for (Competition competition : competitionList) {
            if (competition.getName().equals(competitionName)) {
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
        List<Competition> competitionsFound = searchCompetitionsInList(date, location);
        if (!competitionsFound.isEmpty()) {
            return competitionsFound;
        }

        List<Competition> competitions = new ArrayList<>();
        List<String> organizers = new ArrayList<>();
        List<String> registrations = new ArrayList<>();

        loadCompetitionsFromDatabase(date, location, competitions, organizers, registrations);
        associateOrganizersAndRegistrations(competitions, organizers, registrations);

        return competitions;
    }

    private List<Competition> searchCompetitionsInList(String date, String location) {
        List<Competition> found = new ArrayList<>();
        for (Competition c : this.competitionList) {
            if (c.getDate().equals(date) && c.getLocation().equals(location)) {
                found.add(c);
            }
        }
        return found;
    }

    private void loadCompetitionsFromDatabase(String date, String location,
                                              List<Competition> competitions,
                                              List<String> organizers,
                                              List<String> registrations) {
        String sql = "SELECT e.competitionName, e.description, e.coinsRequired, e.date, e.location, " +
                "e.organizerUsername, e.MaxRegistrationNumber, GROUP_CONCAT(r.idRegistration) AS registrationIds " +
                "FROM competitions e " +
                "LEFT JOIN registrations r ON e.competitionName = r.competitionName " +
                "WHERE e.date = ? AND e.location = ? " +
                "GROUP BY e.competitionName, e.description, e.coinsRequired, e.date, e.location, " +
                "e.MaxRegistrationNumber, e.organizerUsername";

        try (Connection conn = DbsConnector.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, date);
            stmt.setString(2, location);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Competition competition = new Competition(
                            rs.getString("competitionName"),
                            rs.getString(DESCRIPTION),
                            rs.getString(DATE),
                            rs.getString(LOCATION),
                            rs.getInt(COINSREQUIRE),
                            rs.getInt(MAXREGISTRATIONNUMBER)
                    );

                    competitions.add(competition);
                    this.competitionList.add(competition);
                    organizers.add(rs.getString("organizerUsername"));
                    registrations.add(rs.getString(REGISTRATIONIDS));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void associateOrganizersAndRegistrations(List<Competition> competitions,
                                                     List<String> organizers,
                                                     List<String> registrations) {
        for (int i = 0; i < competitions.size(); i++) {
            Competition competition = competitions.get(i);
            assignOrganizerToCompetition(competition, organizers.get(i));
            assignRegistrationsToCompetition(competition, registrations.get(i));
        }
    }

    private void assignOrganizerToCompetition(Competition competition, String organizerUsername) {
        if (organizerUsername == null) {
            return;
        }

        Organizer organizer = organizerDao.selectOrganizerByUsername(organizerUsername);
        if (organizer != null) {
            competition.setOrganizer(organizer);
        }
    }

    private void assignRegistrationsToCompetition(Competition competition, String registrationIds) {
        if (registrationIds == null || registrationIds.isEmpty()) {
            return;
        }

        for (String id : registrationIds.split(",")) {
            int regId = Integer.parseInt(id);
            Registration r = competitionRegistrationDao.selectRegistrationById(regId);
            if (r != null) {
                r.setCompetition(competition);
                competition.addCompetitionRegistration(r);
            }

        }
    }

    @Override
    public void addCompetition(Competition competition) {
        if (checkCompetition(competition.getName())) {
            return ;
        }

        String sql = "INSERT INTO competitions (competitionName, description, date, location, coinsRequired, MaxRegistrationNumber, currentRegistrationNumber, organizerUsername) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DbsConnector.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, competition.getName());
            stmt.setString(2, competition.getDescription());
            stmt.setString(3, competition.getDate());
            stmt.setString(4, competition.getLocation());
            stmt.setInt(5, competition.getParticipationFee());
            stmt.setInt(6, competition.getMaxRegistrations());
            stmt.setInt(7, competition.getRegistrationsNumber());
            stmt.setString(8, competition.getOrganizer() != null ? competition.getOrganizer().getUsername() : null);

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                this.competitionList.add(competition);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
