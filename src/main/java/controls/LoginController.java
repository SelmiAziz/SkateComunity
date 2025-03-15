package controls;

import dao.CostumerDao;
import dao.patternAbstractFactory.DaoFactory;
import dao.OrganizerDao;
import dao.UserDao;
import beans.LogUserBean;
import beans.RegisterUserBean;
import beans.UserInfo;
import exceptions.ProfileNameAlreadyUsedException;
import exceptions.UserAlreadyExistsException;
import exceptions.UserNotFoundException;
import login.User;
import login.ProfileType;
import model.Costumer;
import model.Organizer;
import utils.Session;
import utils.SessionManager;


public class LoginController {
    private final CostumerDao costumerDao = DaoFactory.getInstance().createCostumerDao();
    private final UserDao userDao = DaoFactory.getInstance().createUserDao();
    private final OrganizerDao organizerDao = DaoFactory.getInstance().createOrganizerDao();


    public  void logUser(LogUserBean loginBean) throws  UserNotFoundException{
        String username = loginBean.getUsername();
        String password = loginBean.getPassword();
        if(userDao.checkUser(username, password)){
            User user = userDao.selectUserByUsername(username);
            SessionManager.getInstance().createSession(new Session(user.getUsername(), user.getProfile().getName() , user.getProfile().getProfileType()));
        }else{
            throw new UserNotFoundException("Utente non trovato");
        }
    }


    public void registerUser(RegisterUserBean registerUserBean) throws UserAlreadyExistsException, ProfileNameAlreadyUsedException{
        if (userDao.selectUserByUsername(registerUserBean.getUsername()) != null){
           throw new UserAlreadyExistsException("L'utente è già presente nel sistema");
        }
        User user = new User(registerUserBean.getUsername(), registerUserBean.getPassword());
        if(registerUserBean.getProfileType() == ProfileType.COSTUMER){
            if (costumerDao.selectCostumerByCostumerName(registerUserBean.getProfileName()) != null){
                throw new ProfileNameAlreadyUsedException("Il nome profilo è già usato da un altro utente");
            }
            Costumer costumer =  new Costumer(registerUserBean.getProfileName());
            user.setProfile(costumer);
        }else{
            if (organizerDao.selectOrganizerByOrganizerName(registerUserBean.getProfileName()) != null){
                throw new ProfileNameAlreadyUsedException("Il nome profilo è già usato da un altro utente");
            }
           Organizer organizer = new Organizer(registerUserBean.getProfileName());
           user.setProfile(organizer);
        }
        SessionManager.getInstance().createSession(new Session(user.getUsername(), user.getProfile().getName(), user.getProfile().getProfileType()));
        userDao.addUser(user);
    }

    public LogUserBean determineProfile(){
        User user = userDao.selectUserByUsername(SessionManager.getInstance().getSession().getUsername());
        return new LogUserBean(user.getProfile().getProfileType());
    }


    public UserInfo getCurrentUserInfo(){
        User user = userDao.selectUserByUsername(SessionManager.getInstance().getSession().getUsername());
        return new  UserInfo(user.getUsername(), user.getProfile().getCoins());
    }


}
