package org.example.robmaguirecustomersupport.site;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
public class SessionListController {
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(Map<String, Object> model, HttpSession session, HttpServletRequest request) {
        final var admin = (Boolean) session.getAttribute("admin");
        if (admin) {
            model.put("timestamp", System.currentTimeMillis());
            model.put("numberOfSessions", SessionRegistry.getNumberOfSessions());
            model.put("sessionList", SessionRegistry.getSessions());
        }

        return "session/list";
    }
}
