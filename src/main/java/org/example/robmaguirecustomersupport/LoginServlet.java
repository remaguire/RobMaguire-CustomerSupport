package org.example.robmaguirecustomersupport;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet(
        name = "loginServlet",
        urlPatterns = "/login"
)
public class LoginServlet extends HttpServlet {
    private static final Map<String, String> users = new ConcurrentHashMap<>();
    private static final List<String> admin = Collections.synchronizedList(new ArrayList<>());

    static {
        users.put("remaguire", "password");
        users.put("user", "pass");

        admin.add("remaguire");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final HttpSession session = request.getSession();
        if (request.getParameter("logout") != null) {
            session.invalidate();
            response.sendRedirect("login");
            return;
        } else if (session.getAttribute("username") != null) {
            response.sendRedirect("tickets");
            return;
        }

        request.setAttribute("loginFailed", false);
        final String action = Optional.ofNullable(request.getParameter("action")).orElse("login");
        switch (action) {
            case "signup":
                request.getRequestDispatcher("/WEB-INF/jsp/view/signup.jsp")
                        .forward(request, response);
                break;
            case "login":
            default:
                request.getRequestDispatcher("/WEB-INF/jsp/view/login.jsp")
                        .forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String username = request.getParameter("username"),
                password = request.getParameter("password");
        final HttpSession session = request.getSession();
        if ("signup".equals(request.getParameter("action"))) {
            if (users.containsKey(username)) {
                request.setAttribute("loginFailed", true);
                request.getRequestDispatcher("/WEB-INF/jsp/view/signup.jsp")
                        .forward(request, response);
                return;
            }

            users.put(username, password);
            session.setAttribute("username", username);
            response.sendRedirect("tickets");
            return;
        }

        if (session.getAttribute("username") != null) {
            response.sendRedirect("tickets");
            return;
        }

        if (username == null || password == null || !users.containsKey(username) || !password.equals(users.get(username))) {
            request.setAttribute("loginFailed", true);
            request.getRequestDispatcher("/WEB-INF/jsp/view/login.jsp")
                    .forward(request, response);
        } else {
            session.setAttribute("username", username);
            if (admin.contains(username)) session.setAttribute("admin", true);
            request.changeSessionId();
            response.sendRedirect("tickets");
        }
    }
}
