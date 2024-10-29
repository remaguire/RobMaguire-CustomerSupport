package org.example.robmaguirecustomersupport;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(
        name = "sessionListServlet",
        urlPatterns = "/sessions"
)
public class SessionListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("username") == null) {
            response.sendRedirect("login");
            return;
        }

        request.setAttribute("numberOfSessions", SessionRegistry.getNumberOfSessions());
        request.setAttribute("sessionList", SessionRegistry.getSessions());
        request.getRequestDispatcher("/WEB-INF/jsp/view/sessions.jsp")
                .forward(request, response);
    }
}
