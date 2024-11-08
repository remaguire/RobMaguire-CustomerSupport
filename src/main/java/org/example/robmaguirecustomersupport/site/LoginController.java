package org.example.robmaguirecustomersupport.site;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class LoginController {
    private static final Map<String, String> users = new ConcurrentHashMap<>();
    private static final List<String> admin = Collections.synchronizedList(new ArrayList<>());

    static {
        users.put("remaguire", "password");
        users.put("user", "pass");

        admin.add("remaguire");
    }

    public static class LoginForm {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    @RequestMapping("logout")
    public View logout(HttpSession session) {
        session.invalidate();
        return new RedirectView("/login", true, false);
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public ModelAndView login(Map<String, Object> model, HttpSession session) {
        if (session.getAttribute("username") == null)
            return getTicketRedirect();

        model.put("loginFailed", false);
        model.put("loginForm", new LoginForm());
        return new ModelAndView("login");
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ModelAndView login(Map<String, Object> model, HttpSession session, HttpServletRequest request, LoginForm form) {
        if (session.getAttribute("username") == null)
            return getTicketRedirect();

        final var user = form.getUsername();
        final var password = form.getPassword();
        if (user == null || password == null || !users.containsKey(user) || password.equals(users.get(user))) {
            form.setPassword(null);
            model.put("loginFailed", true);
            model.put("loginForm", form);
            return new ModelAndView("login");
        }

        session.setAttribute("username", user);
        if (admin.contains(user)) session.setAttribute("admin", true);
        request.changeSessionId();
        return getTicketRedirect();
    }

    @RequestMapping(value = "signup", method = RequestMethod.GET)
    public ModelAndView signup(Map<String, Object> model, HttpSession session) {
        if (session.getAttribute("username") != null)
            return getTicketRedirect();

        model.put("loginFailed", false);
        model.put("loginForm", new LoginForm());
        return new ModelAndView("signup");
    }

    @RequestMapping(value = "signup", method = RequestMethod.POST)
    public ModelAndView signup(Map<String, Object> model, HttpSession session, HttpServletRequest request, LoginForm form) {
        if (session.getAttribute("username") != null)
            return getTicketRedirect();

        final var user = form.getUsername();
        final var password = form.getPassword();
        if (user == null || password == null || users.containsKey(user)) {
            model.put("loginFailed", true);
            model.put("loginForm", form);
            return new ModelAndView("signup");
        }

        session.setAttribute("username", user);
        if (admin.contains(user)) session.setAttribute("admin", true);
        request.changeSessionId();
        return getTicketRedirect();
    }

    private ModelAndView getTicketRedirect() {
        return new ModelAndView(new RedirectView("/ticket/list", true, false));
    }
}
