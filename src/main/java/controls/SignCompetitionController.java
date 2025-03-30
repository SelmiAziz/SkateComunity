package controls;

import beans.WalletBean;
import beans.CompetitionRegistrationBean;
import dao.*;
import dao.patternAbstractFactory.DaoFactory;
import beans.CompetitionBean;
import exceptions.InsufficientCoinsException;
import exceptions.NoAvailableSeats;
import exceptions.UserAlreadySignedCompetition;
import model.*;
import utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class SignCompetitionController {
    private final DaoFactory daoFactory = DaoFactory.getInstance();
    private final CompetitionDao competitionDao = daoFactory.createCompetitionDao();
    private final CompetitionRegistrationDao competitionRegistrationDao = daoFactory.createCompetitionRegistrationDao();
    private final CustomerDao customerDao = daoFactory.createCostumerDao();
    private final SessionManager sessionManager = SessionManager.getInstance();
    private final PaymentController paymentController = new PaymentController();


    public CompetitionBean competitionDetails(CompetitionBean competitionBean){
        Competition competition = competitionDao.selectCompetitionByName(competitionBean.getName());
        return new CompetitionBean(competition.getName(), competition.getDescription(), competition.getParticipationFee(),(competition.getMaxRegistrations() - competition.getCurrentRegistrations()));
    }

    public List<CompetitionBean> allAvailableCompetitions(){
        List<Competition> competitionList = competitionDao.selectAvailableCompetitions();
        List<CompetitionBean> competitionBeanList = new ArrayList<>();
        for (Competition competition : competitionList){
            competitionBeanList.add(new CompetitionBean(competition.getName(),competition.getLocation(), competition.getDate()));
        }
        return competitionBeanList;
    }

    //dummy function that generate a code
    private String generateCode(String competitionName,int currentRegistrationNumber){
        return "XX0" + competitionName+currentRegistrationNumber;
    }

    private String findAvailableSpotForCompetition(int registrationNumber) {
        int groupSize = 19;
        int groupIndex = (registrationNumber - 1) / groupSize;
        int number = ((registrationNumber - 1) % groupSize) + 1;

        String letter = "";
        do {
            letter = (char) ('A' + (groupIndex % 26)) + letter;
            groupIndex = groupIndex / 26 - 1;
        } while (groupIndex >= 0);

        return letter + number;
    }


    public CompetitionRegistrationBean signToCompetition(CompetitionBean competitionBean) throws UserAlreadySignedCompetition, InsufficientCoinsException, NoAvailableSeats {
        String customerName = sessionManager.getSession().getUsername();
        Customer currentCustomer = customerDao.selectCustomerByUsername(customerName);
        Competition competition = competitionDao.selectCompetitionByName(competitionBean.getName());
        List<CompetitionRegistration> participantsList = competition.getCompetitionRegistrations();
        for(CompetitionRegistration competitionParticipation : participantsList ){
            if(competitionParticipation.getParticipant().getUsername().equals(currentCustomer.getUsername())){
                throw new UserAlreadySignedCompetition("L'utente è già segnato a questa competizione");
            }
        }

        if(competition.getMaxRegistrations() - competition.getCurrentRegistrations() <= 0){
            throw new NoAvailableSeats("Non sono presenti più posti disponibili per tale competizione");
        }

        try{
            paymentController.payWithCoins(currentCustomer.getWallet(), competition.getParticipationFee());
        }catch(InsufficientCoinsException e){
            throw e;
        }

        int registrationNumber = competition.getCurrentRegistrations() +1;
        String registrationCode = generateCode(competition.getName(),competition.getCurrentRegistrations());
        String availableSeatCode = findAvailableSpotForCompetition(registrationNumber);

        CompetitionRegistration newCompetitionRegistration = new CompetitionRegistration(registrationNumber, registrationCode, availableSeatCode);
        newCompetitionRegistration.setParticipant(currentCustomer);
        newCompetitionRegistration.setCompetition(competition);
        competition.addCompetitionRegistration(newCompetitionRegistration);
        competitionRegistrationDao.addCompetitionRegistration(newCompetitionRegistration);

        CompetitionRegistrationBean competitionRegistrationBean = new CompetitionRegistrationBean(registrationNumber, registrationCode, availableSeatCode);
        return competitionRegistrationBean;

    }

    public List<CompetitionBean> searchCompetitionByDateAndLocation(CompetitionBean competitionBean){
        List<CompetitionBean> competitionBeanList = new ArrayList<>();
        List<Competition> competitionList = competitionDao.selectCompetitionsByDateAndLocation(competitionBean.getDate(), competitionBean.getLocation());
        for(Competition competition: competitionList){
            competitionBeanList.add(new CompetitionBean(competition.getName(), competition.getLocation(), competition.getDate()));
        }
        return competitionBeanList;
    }

    public WalletBean customerInfo(){
        String username = sessionManager.getSession().getUsername();
        Customer customer = customerDao.selectCustomerByUsername(username);
        Wallet wallet = customer.getWallet();
        WalletBean walletBean = new WalletBean(wallet.getBalance());
        return walletBean;
    }

}

