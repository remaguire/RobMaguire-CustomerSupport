package org.example.robmaguirecustomersupport;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionIdListener;
import jakarta.servlet.http.HttpSessionListener;

import java.text.SimpleDateFormat;
import java.util.Date;

@WebListener
public class SessionListener implements HttpSessionListener, HttpSessionIdListener {
    private SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");

    private String date() {
        return format.format(new Date());
    }

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        SessionRegistry.addSession(event.getSession());
        System.out.println(date() + ": Session ID " + event.getSession().getId() + " created.");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        SessionRegistry.removeSession(event.getSession());
        System.out.println(date() + ": Session ID " + event.getSession().getId() + " destroyed.");
    }

    @Override
    public void sessionIdChanged(HttpSessionEvent event, String oldSessionId) {
        SessionRegistry.updateSessionId(event.getSession(), oldSessionId);
        System.out.println(date() + ": Session ID " + oldSessionId + " changed to " + event.getSession().getId());
    }
}
