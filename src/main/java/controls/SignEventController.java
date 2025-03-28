package controls;

import beans.WalletBean;
import beans.EventRegistrationBean;
import dao.*;
import dao.patternAbstractFactory.DaoFactory;
import beans.EventBean;
import exceptions.InsufficientCoinsException;
import exceptions.NoAvailableSeats;
import exceptions.UserAlreadySignedEvent;
import model.*;
import utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class SignEventController {
    private final DaoFactory daoFactory = DaoFactory.getInstance();
    private final EventDao eventDao = daoFactory.createEventDao();
    private final EventRegistrationDao eventRegistrationDao = daoFactory.createEventRegistrationDao();
    private final CustomerDao costumerDao = daoFactory.createCostumerDao();
    private final SessionManager sessionManager = SessionManager.getInstance();
    private final PaymentController paymentController = new PaymentController();


    public EventBean eventDetails(EventBean eventBean){
        Event event = eventDao.selectEventByName(eventBean.getName());
        return new EventBean(event.getName(), event.getDescription(), event.getParticipationFee(),(event.getMaxRegistrations() - event.getCurrentRegistrations()));
    }

    public List<EventBean> allAvailableEvents(){
        List<Event> eventList = eventDao.selectAvailableEvents();
        List<EventBean> eventBeanList = new ArrayList<>();
        for (Event event : eventList){
            eventBeanList.add(new EventBean(event.getName(),event.getLocation(), event.getDate()));
        }
        return eventBeanList;
    }

    //dummy function that generate a code
    private String generateCode(String eventName,int currentRegistrationNumber){
        return "XX0" + eventName+currentRegistrationNumber;
    }

    private String findAvailableSpotForEvent(int registrationNumber) {
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


    public EventRegistrationBean signToEvent(EventBean eventBean) throws UserAlreadySignedEvent, InsufficientCoinsException, NoAvailableSeats {
        String customerName = sessionManager.getSession().getUsername();
        Customer currentCustomer = costumerDao.selectCustomerByUsername(customerName);
        Event event = eventDao.selectEventByName(eventBean.getName());
        Organizer organizer = event.getOrganizer();
        List<EventRegistration> participantsList = event.getEventRegistrations();
        for(EventRegistration eventParticipation : participantsList ){
            if(eventParticipation.getParticipant().getUsername().equals(currentCustomer.getUsername())){
                throw new UserAlreadySignedEvent("L'utente è già segnato a questo evento");
            }
        }

        if(event.getMaxRegistrations() - event.getCurrentRegistrations() <= 0){
            throw new NoAvailableSeats("Non sono presenti più posti disponibili per tale evento");
        }

        try{
            paymentController.payWithCoins(currentCustomer.getWallet(), event.getParticipationFee());
        }catch(InsufficientCoinsException e){
            throw e;
        }

        int registrationNumber = event.getCurrentRegistrations() +1;
        String registrationCode = generateCode(event.getName(),event.getCurrentRegistrations());
        String availableSeatCode = findAvailableSpotForEvent(registrationNumber);

        EventRegistration newEventRegistration = new EventRegistration(registrationNumber, registrationCode, availableSeatCode);
        newEventRegistration.setParticipant(currentCustomer);
        newEventRegistration.setEvent(event);
        event.addEventRegistration(newEventRegistration);
        eventRegistrationDao.addEventRegistration(newEventRegistration);

        EventRegistrationBean eventRegistrationBean = new EventRegistrationBean(registrationNumber, registrationCode, availableSeatCode);
        return eventRegistrationBean;

    }

    public List<EventBean> searchEventByDateAndLocation(EventBean eventBean){
        List<EventBean> eventBeanList = new ArrayList<>();
        List<Event> eventList = eventDao.selectEventsByDateAndLocation(eventBean.getDate(), eventBean.getLocation());
        for(Event event: eventList){
            eventBeanList.add(new EventBean(event.getName(), event.getLocation(), event.getDate()));
        }
        return eventBeanList;
    }

    public WalletBean costumerInfo(){
        String username = sessionManager.getSession().getUsername();
        Customer customer = costumerDao.selectCustomerByUsername(username);
        Wallet wallet = customer.getWallet();
        WalletBean walletBeanBean = new WalletBean(wallet.getBalance());
        return walletBeanBean;
    }

}
