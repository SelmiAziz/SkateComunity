package utils;


import login.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class SessionManager {
    private static SessionManager instance;
    private final List<Session> sessions;

    private SessionManager() {
        this.sessions = new ArrayList<>();
    }

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public synchronized Session createSession(User user) {
        Session session = new Session(user.getUsername(), user.getRole());
        this.sessions.add(session);
        return session;
    }

    public synchronized Session getSessionByToken(String token) {
        Iterator<Session> it = sessions.iterator();
        while (it.hasNext()) {
            Session s = it.next();
            if (s.getToken().equals(token)) {
                if (s.isExpired()) {
                    it.remove();
                    return null;
                }
                return s;
            }
        }
        return null;
    }

    public synchronized boolean validToken(String token) {
        return getSessionByToken(token) != null;
    }

    public synchronized void terminateSession(String token) {
        sessions.removeIf(s -> s.getToken().equals(token));
    }

    public synchronized void cleanupExpiredSessions() {
        sessions.removeIf(Session::isExpired);
    }
}

