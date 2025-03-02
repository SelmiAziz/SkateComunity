package login;

public class SessionManager {
    private static SessionManager instance;
    private Session session;

    private SessionManager(){}

    public synchronized static SessionManager getInstance(){
        if(instance == null){
            instance = new SessionManager();
        }
        return instance;
    }
    public void createSession(Session session){
        this.session = session;
    }

    public Session getSession(){
        return this.session;
    }

    public boolean validSession(){
        return this.session != null;
    }

    public void terminateSession(){
        this.session = null;
    }
}
