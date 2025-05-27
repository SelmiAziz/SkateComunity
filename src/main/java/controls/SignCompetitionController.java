package controls;

import beans.*;
import dao.*;
import dao.patternabstractfactory.DaoFactory;
import exceptions.*;
import model.*;
import utils.Session;
import utils.SessionManager;
import java.util.ArrayList;
import java.util.List;

public class SignCompetitionController {
    private final DaoFactory daoFactory = DaoFactory.getInstance();
    private final CompetitionDao competitionDao = daoFactory.createCompetitionDao();
    private final RegistrationDao competitionRegistrationDao = daoFactory.createRegistrationDao();
    private final CustomerDao customerDao = daoFactory.createCostumerDao();
    private final PaymentController paymentController = new PaymentController();


    public CompetitionBean competitionDetails(String token, CompetitionBean competitionBean) throws SessionExpiredException, DataAccessException{
        Session session = SessionManager.getInstance().getSessionByToken(token);
        if(session == null){
            throw new SessionExpiredException();
        }
        Competition competition = null;
        competition = competitionDao.selectCompetitionByName(competitionBean.getName());
        return new CompetitionBean(competition.getName(), competition.getDescription(), competition.getParticipationFee(),(competition.getMaxRegistrations() - competition.getRegistrationsNumber()));
    }

    public List<CompetitionBean> allAvailableCompetitions(String token) throws SessionExpiredException, DataAccessException{
        Session session = SessionManager.getInstance().getSessionByToken(token);
        if(session == null){
            throw new SessionExpiredException();
        }
        List<Competition> competitionList = competitionDao.selectAvailableCompetitions();
        List<CompetitionBean> competitionBeanList = new ArrayList<>();
        for (Competition competition : competitionList) {
            competitionBeanList.add(new CompetitionBean(competition.getName(), competition.getLocation(), competition.getDate()));
        }
        return competitionBeanList;
    }

    //dummy function that generate a code
    protected String generateCode(String competitionName,int currentRegistrationNumber){
        return "XX0" + competitionName+currentRegistrationNumber;
    }

    protected String findAvailableSpotForCompetition(int registrationNumber) {
        int groupSize = 19;
        int groupIndex = (registrationNumber - 1) / groupSize;
        int number = ((registrationNumber - 1) % groupSize) + 1;

        StringBuilder letterBuilder = new StringBuilder();
        do {
            letterBuilder.insert(0, (char) ('A' + (groupIndex % 26)));
            groupIndex = groupIndex / 26 - 1;
        } while (groupIndex >= 0);

        return letterBuilder.append(number).toString();
    }

    public RegistrationBean signToCompetition(String token, CompetitionBean competitionBean, RegistrationRequestBean registrationRequestBean) throws UserAlreadySignedCompetition, InsufficientCoinsException, NoAvailableSeats, SessionExpiredException, DataAccessException {
        Session session = SessionManager.getInstance().getSessionByToken(token);
        if(session == null){
            throw new SessionExpiredException();
        }
        Customer currentCustomer = customerDao.selectCustomerByUsername(session.getUsername());
        Competition competition = competitionDao.selectCompetitionByName(competitionBean.getName());
        List<Registration> participantsList = competition.getCompetitionRegistrations();
        for(Registration competitionParticipation : participantsList ){
            if(competitionParticipation.getParticipant().getUsername().equals(currentCustomer.getUsername())){
                throw new UserAlreadySignedCompetition("L'utente è già segnato a questa competizione");
            }
        }

        if(competition.getMaxRegistrations() - competition.getRegistrationsNumber() <= 0){
            throw new NoAvailableSeats("Non sono presenti più posti disponibili per tale competizione");
        }

        paymentController.payWithCoins(currentCustomer.getWallet(), competition.getParticipationFee());

        int registrationNumber = competition.getRegistrationsNumber() +1;
        String registrationCode = generateCode(competition.getName(),competition.getRegistrationsNumber());
        String availableSeatCode = findAvailableSpotForCompetition(registrationNumber);

        Registration registration = new Registration(registrationNumber, registrationCode, availableSeatCode, registrationRequestBean.getRegistrationName(), registrationRequestBean.getEmail());
        registration.setParticipant(currentCustomer);
        registration.setCompetition(competition);
        competition.addCompetitionRegistration(registration);
        competitionRegistrationDao.addRegistration(registration);

        RegistrationBean registrationBean = new RegistrationBean();
        registrationBean.setCurrentRegistrationNumber(registrationNumber);
        registrationBean.setRegistrationCode(registration.getRegistrationCode());
        registrationBean.setAssignedSeat(availableSeatCode);
        return registrationBean;

    }

    public List<CompetitionBean> searchCompetitionByDateAndLocation(String token, CompetitionBean competitionBean) throws SessionExpiredException, DataAccessException{
        Session session = SessionManager.getInstance().getSessionByToken(token);
        if(session == null){
            throw new SessionExpiredException();
        }
        List<CompetitionBean> competitionBeanList = new ArrayList<>();
        List<Competition> competitionList = competitionDao.selectCompetitionsByDateAndLocation(competitionBean.getDate(), competitionBean.getLocation());
        for (Competition competition : competitionList) {
            competitionBeanList.add(new CompetitionBean(competition.getName(), competition.getLocation(), competition.getDate()));
        }
        return competitionBeanList;

    }

    public WalletBean customerInfo(String token) throws SessionExpiredException, DataAccessException {
        Session session = SessionManager.getInstance().getSessionByToken(token);
        if(session == null){
            throw new SessionExpiredException();
        }
        Customer customer = customerDao.selectCustomerByUsername(session.getUsername());
        Wallet wallet = customer.getWallet();
        return new WalletBean(wallet.getBalance());
    }

}

