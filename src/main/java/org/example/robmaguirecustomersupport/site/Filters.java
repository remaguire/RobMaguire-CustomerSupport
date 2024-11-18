package org.example.robmaguirecustomersupport.site;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class Filters {
    public static class LoggedInFilter implements Filter {
        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            final var jRequest = (HttpServletRequest) request;
            final var jResponse = (HttpServletResponse) response;
            final var session = jRequest.getSession(false);
            if (!AuthenticationController.isLoggedIn(session))
                jResponse.sendRedirect(jRequest.getContextPath() + "/login");
            else
                chain.doFilter(request, response);
        }
    }

    public static class AdminFilter implements Filter {
        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            final var jRequest = (HttpServletRequest) request;
            final var jResponse = (HttpServletResponse) response;
            final var session = jRequest.getSession(false);
            if (!AuthenticationController.isAdmin(session))
                jResponse.sendRedirect(jRequest.getContextPath() + "/login");
            else
                chain.doFilter(request, response);
        }
    }
}
