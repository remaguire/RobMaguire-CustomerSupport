package org.example.robmaguirecustomersupport.site;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionRegistry {
    private static final Map<String, HttpSession> sessions = new ConcurrentHashMap<>();

    public static void addSession(HttpSession session) {
        sessions.put(session.getId(), session);
    }

    public static void updateSessionId(HttpSession session, String oldSessionId) {
        synchronized (sessions) {
            sessions.remove(oldSessionId);
            addSession(session);
        }
    }

    public static void removeSession(HttpSession session) {
        sessions.remove(session.getId());
    }

    public static List<HttpSession> getSessions() {
        return new ArrayList<>(sessions.values());
    }

    public static int getNumberOfSessions() {
        return sessions.size();
    }

    private SessionRegistry() {
    }
}
