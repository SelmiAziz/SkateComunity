package utils;

import login.Role;
import java.util.UUID;

public class Session {
    private final String username;
    private final String token;
    private final Role role;
    private long expiryTime;

    private  long SESSION_DURATION = 30L * 60 * 1000; // 30 minuti in millisecondi



    public Session(String username, Role role) {
        this.username = username;
        this.role = role;
        this.token = UUID.randomUUID().toString();
        this.expiryTime = System.currentTimeMillis() + SESSION_DURATION;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public Role getRole() {
        return role;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }

    public void updateExpiryTime() {
        this.expiryTime = System.currentTimeMillis() + SESSION_DURATION;
    }

    public long getExpiryTime() {
        return expiryTime;
    }
}

