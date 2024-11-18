package org.example.robmaguirecustomersupport.site;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.robmaguirecustomersupport.entities.UserPrincipal;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@Controller
public class AuthenticationController {
    @Inject
    AuthenticationService authenticationService;

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

    public static boolean isLoggedIn(HttpSession session) {
        return (UserPrincipal.getPrincipal(session) != null);
    }

    public static boolean isAdmin(HttpSession session) {
        final var principal = (UserPrincipal) UserPrincipal.getPrincipal(session);
        return (principal != null && principal.isAdmin());
    }

    @RequestMapping("logout")
    public View logout(HttpSession session) {
        session.invalidate();
        return new RedirectView("/login", true, false);
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public ModelAndView login(Map<String, Object> model, HttpSession session) {
        if (isLoggedIn(session))
            return getTicketRedirect();

        model.put("loginFailed", false);
        model.put("loginForm", new LoginForm());
        return new ModelAndView("login");
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ModelAndView login(Map<String, Object> model, HttpSession session, HttpServletRequest request, LoginForm form) {
        if (isLoggedIn(session))
            return getTicketRedirect();

        final UserPrincipal principal;
        try {
            principal = authenticationService.authenticate(form.getUsername(), form.getPassword());
        } catch (ConstraintViolationException ex) {
            return new ModelAndView("login");
        }

        if (principal == null) {
            form.setPassword(null);
            model.put("loginFailed", true);
            model.put("loginForm", form);
            return new ModelAndView("login");
        }

        UserPrincipal.setPrincipal(session, principal);
        request.changeSessionId();
        return getTicketRedirect();
    }

    @RequestMapping(value = "signup", method = RequestMethod.GET)
    public ModelAndView signup(Map<String, Object> model, HttpSession session) {
        if (isLoggedIn(session))
            return getTicketRedirect();

        model.put("loginFailed", false);
        model.put("loginForm", new LoginForm());
        return new ModelAndView("signup");
    }

    @RequestMapping(value = "signup", method = RequestMethod.POST)
    public ModelAndView signup(Map<String, Object> model, HttpSession session, HttpServletRequest request, LoginForm form) {
        if (isLoggedIn(session))
            return getTicketRedirect();

        final UserPrincipal principal = authenticationService.register(form.getUsername(), form.getPassword());
        if (principal == null) {
            model.put("loginFailed", true);
            model.put("loginForm", form);
            return new ModelAndView("signup");
        }

        /*UserPrincipal.setPrincipal(session, principal);
        request.changeSessionId();
        return getTicketRedirect();*/
        model.put("loginFailed", false);
        model.put("loginForm", form);
        return new ModelAndView("login");
    }

    private ModelAndView getTicketRedirect() {
        return new ModelAndView(new RedirectView("/ticket/list", true, false));
    }
}
