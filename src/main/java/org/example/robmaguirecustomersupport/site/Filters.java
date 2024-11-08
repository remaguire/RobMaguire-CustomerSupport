package org.example.robmaguirecustomersupport.site;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class Filters {
    public static class FilterForAttribute implements Filter {
        public final String attributeName, redirect;

        public FilterForAttribute(String attributeName, String redirect) {
            this.attributeName = attributeName;
            this.redirect = redirect;
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            final var jRequest = (HttpServletRequest) request;
            final var jResponse = (HttpServletResponse) response;
            final var session = jRequest.getSession(false);
            if (session == null || session.getAttribute(attributeName) == null)
                jResponse.sendRedirect(jRequest.getContextPath() + redirect);
            else
                chain.doFilter(request, response);
        }
    }
}
