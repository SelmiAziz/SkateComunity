package controls;

import beans.AuthTokenBean;
import dao.CompetitionDao;
import dao.patternAbstractFactory.DaoFactory;
import dao.OrganizerDao;
import beans.CompetitionBean;
import exceptions.CompetitionAlreadyExistsException;
import exceptions.SessionExpiredException;
import model.Competition;
import model.Organizer;
import utils.Session;
import utils.SessionManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CreateCompetitionController {
    private final DaoFactory daoFactory = DaoFactory.getInstance();
    private final CompetitionDao competitionDao = daoFactory.createCompetitionDao();
    private final OrganizerDao organizerDao = daoFactory.createOrganizerDao();

    public void createCompetition(CompetitionBean competitionBean, AuthTokenBean authTokenBean) throws CompetitionAlreadyExistsException, SQLException, SessionExpiredException {
        Session session = SessionManager.getInstance().getSessionByToken(authTokenBean.getToken());
        if(session == null){
            throw new SessionExpiredException();
        }
        String newCompetitionName = competitionBean.getName();
        competitionDao.checkCompetition(newCompetitionName);
        if (competitionDao.checkCompetition(newCompetitionName)) {
            throw new CompetitionAlreadyExistsException("La competizione in questione esiste già");
        }
        Competition newCompetition = new Competition(competitionBean.getName(), competitionBean.getDescription(), competitionBean.getDate(), competitionBean.getLocation(), competitionBean.getCoins(),  competitionBean.getMaxRegistrations());
        Organizer organizer = organizerDao.selectOrganizerByUsername(SessionManager.getInstance().getSessionByToken("flòg").getUsername());
        newCompetition.setOrganizer(organizer);
        organizer.addCompetition(newCompetition);
        competitionDao.addCompetition(newCompetition);
    }

    public List<CompetitionBean> organizerCompetitions(AuthTokenBean authTokenBean) throws SessionExpiredException {
        Session session = SessionManager.getInstance().getSessionByToken(authTokenBean.getToken());
        if(session == null){
            throw new SessionExpiredException();
        }
        Organizer organizer = organizerDao.selectOrganizerByUsername(session.getUsername());
        List<Competition> competitionList = organizer.getCompetitionCreatedList();
        List<CompetitionBean> competitionBeanList = new ArrayList<>();
        for (Competition competition : competitionList) {
            competitionBeanList.add(new CompetitionBean(competition.getName(), competition.getDescription(), competition.getDate(), competition.getLocation(), competition.getParticipationFee(), competition.getCurrentRegistrations(), competition.getMaxRegistrations()));
        }
        return competitionBeanList;
    }
}
