package controls;

import beans.AuthBean;
import dao.CustomerDao;
import dao.patternabstractfactory.DaoFactory;
import dao.OrganizerDao;
import dao.UserDao;
import beans.LogUserBean;
import beans.RegisterUserBean;
import exceptions.DataAccessException;
import exceptions.UserNameAlreadyUsedException;
import exceptions.UserNotFoundException;
import login.User;
import login.Role;
import model.Customer;
import model.Organizer;
import model.Wallet;
import utils.Session;
import utils.SessionManager;
import utils.SkaterLevel;

import java.io.IOException;


public class LoginController {
    private final CustomerDao costumerDao = DaoFactory.getInstance().createCostumerDao();
    private final UserDao userDao = DaoFactory.getInstance().createUserDao();
    private final OrganizerDao organizerDao = DaoFactory.getInstance().createOrganizerDao();



    public AuthBean logUser(LogUserBean loginBean) throws UserNotFoundException, DataAccessException {
        if (!userDao.checkUserByUsernameAndPassword(loginBean.getUsername(), loginBean.getPassword())) {
            throw new UserNotFoundException("Credenziali non valide");
        }

        User user = userDao.selectUserByUsername(loginBean.getUsername());
        Session session = SessionManager.getInstance().createSession(user);

        return new AuthBean(session.getToken(),user.getRole() == Role.COSTUMER ? "Costumer" : "Organizer");
    }


    private String generateSuggestedUsername(String baseUsername) throws DataAccessException {
        String suggestedUsername = baseUsername;
        int suffix = 1;

        while (userDao.checkUserByUsername(suggestedUsername)) {
            suggestedUsername = baseUsername + suffix;
            suffix++;
        }

        return suggestedUsername;
    }



    public void registerUser(RegisterUserBean registerUserBean) throws UserNameAlreadyUsedException, DataAccessException {
        String baseUsername = registerUserBean.getUsername();

        if (userDao.checkUserByUsername(baseUsername)) {
            String suggestion = generateSuggestedUsername(baseUsername);
            throw new UserNameAlreadyUsedException("Lo username è già utilizzato", suggestion);
        }

        String password = registerUserBean.getPassword();
        String dateOfBirth = registerUserBean.getDateOfBirth();
        Role role = registerUserBean.getRole().equals("Organizer") ? Role.ORGANIZER : Role.COSTUMER;
        User user;
        SkaterLevel skillLevel = null;

        if (role == Role.COSTUMER) {
            String skill = registerUserBean.getSkillLevel();
            switch (skill) {
                case "Novice":
                    skillLevel = SkaterLevel.NOVICE;
                    break;
                case "Proficient":
                    skillLevel = SkaterLevel.PROFICIENT;
                    break;
                default:
                    skillLevel = SkaterLevel.ADVANCED;
                    break;
            }

            Wallet wallet = new Wallet();
            wallet.depositCoins(400); // bonus di registrazione
            Customer costumer = new Customer(baseUsername, password, dateOfBirth, skillLevel, wallet);
            costumerDao.saveCustomer(costumer);
            user = costumer;
        } else {
            Organizer organizer = new Organizer(baseUsername, password, dateOfBirth);
            organizerDao.addOrganizer(organizer);
            user = organizer;
        }

        SessionManager.getInstance().createSession(user);
    }


}
