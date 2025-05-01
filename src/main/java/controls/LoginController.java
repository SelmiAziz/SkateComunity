package controls;

import beans.AuthBean;
import dao.CustomerDao;
import dao.patternAbstractFactory.DaoFactory;
import dao.OrganizerDao;
import dao.UserDao;
import beans.LogUserBean;
import beans.RegisterUserBean;
import exceptions.UserNameAlreadyUsedException;
import exceptions.UserAlreadyExistsException;
import exceptions.UserNotFoundException;
import login.User;
import login.Role;
import model.Customer;
import model.Organizer;
import model.Wallet;
import utils.Session;
import utils.SessionManager;
import utils.SkaterLevel;


public class LoginController {
    private final CustomerDao costumerDao = DaoFactory.getInstance().createCostumerDao();
    private final UserDao userDao = DaoFactory.getInstance().createUserDao();
    private final OrganizerDao organizerDao = DaoFactory.getInstance().createOrganizerDao();



    public AuthBean logUser(LogUserBean loginBean) throws UserNotFoundException {
        if (!userDao.checkUserByUsernameAndPassword(loginBean.getUsername(), loginBean.getPassword())) {
            throw new UserNotFoundException("Credenziali non valide");
        }

        User user = userDao.selectUserByUsername(loginBean.getUsername());
        Session session = new Session(user.getUsername(), user.getRole());
        SessionManager.getInstance().createSession(session);

        return new AuthBean(session.getToken(),user.getRole() == Role.COSTUMER ? "Costumer" : "Organizer");
    }



    public void registerUser(RegisterUserBean registerUserBean) throws UserAlreadyExistsException, UserNameAlreadyUsedException {
        if (userDao.checkUserByUsername(registerUserBean.getUsername())){
           throw new UserAlreadyExistsException("L'utente è già presente nel sistema");
        }
        String username = registerUserBean.getUsername();
        String password = registerUserBean.getPassword();
        String dateOfBirth = registerUserBean.getDateOfBirth();
        Role role =  registerUserBean.getRole().equals("Organizer") ? Role.ORGANIZER : Role.COSTUMER;
        User user;
        if(role == Role.COSTUMER){
            SkaterLevel skillLevel = registerUserBean.getSkillLevel().equals("Novice") ? SkaterLevel.NOVICE
                                    : registerUserBean.getSkillLevel().equals("Proficient") ? SkaterLevel.PROFICIENT
                                    : SkaterLevel.ADVANCED;
            Wallet wallet = new Wallet();
            Customer costumer =  new Customer(username,password,dateOfBirth,skillLevel, wallet);
            costumerDao.addCustomer(costumer);
            user = costumer;
        }else{
            Organizer organizer = new Organizer(username, password,dateOfBirth);
            organizerDao.addOrganizer(organizer);
            user = organizer;
        }
        SessionManager.getInstance().createSession(new Session(user.getUsername(), user.getRole()));
    }



}
