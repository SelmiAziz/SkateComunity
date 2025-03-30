package controls;

import dao.CompetitionDao;
import dao.patternAbstractFactory.DaoFactory;
import dao.OrganizerDao;
import beans.CompetitionBean;
import exceptions.CompetitionAlreadyExistsException;
import model.Competition;
import model.Organizer;
import utils.SessionManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CreateCompetitionController {
    private final DaoFactory daoFactory = DaoFactory.getInstance();
    private final CompetitionDao competitionDao = daoFactory.createCompetitionDao();
    private final OrganizerDao organizerDao = daoFactory.createOrganizerDao();

    public void createCompetition(CompetitionBean competitionBean) throws CompetitionAlreadyExistsException, SQLException {
        String newCompetitionName = competitionBean.getName();
        competitionDao.checkCompetition(newCompetitionName);
        if (competitionDao.checkCompetition(newCompetitionName)) {
            throw new CompetitionAlreadyExistsException("La competizione in questione esiste già");
        }
        Competition newCompetition = new Competition(competitionBean.getName(), competitionBean.getDescription(), competitionBean.getDate(), competitionBean.getLocation(), competitionBean.getCoins(),  competitionBean.getMaxRegistrations());
        Organizer organizer = organizerDao.selectOrganizerByUsername(SessionManager.getInstance().getSession().getUsername());
        newCompetition.setOrganizer(organizer);
        organizer.addCompetition(newCompetition);
        competitionDao.addCompetition(newCompetition);
    }

    public List<CompetitionBean> organizerCompetitions() {
        Organizer organizer = organizerDao.selectOrganizerByUsername(SessionManager.getInstance().getSession().getUsername());
        List<Competition> competitionList = organizer.getCompetitionCreatedList();
        List<CompetitionBean> competitionBeanList = new ArrayList<>();
        for (Competition competition : competitionList) {
            competitionBeanList.add(new CompetitionBean(competition.getName(), competition.getDescription(), competition.getDate(), competition.getLocation(), competition.getParticipationFee(), competition.getCurrentRegistrations(), competition.getMaxRegistrations()));
        }
        return competitionBeanList;
    }
}
