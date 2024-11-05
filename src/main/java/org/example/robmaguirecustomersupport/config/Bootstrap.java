package org.example.robmaguirecustomersupport.config;

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.example.robmaguirecustomersupport.site.Filters;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class Bootstrap implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext appContext) throws ServletException {
        final var rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(RootContextConfiguration.class);
        appContext.addListener(new ContextLoaderListener(rootContext));

        final var servletContext = new AnnotationConfigWebApplicationContext();
        servletContext.register(ServletContextConfiguration.class);

        final var dispatcher = appContext.addServlet("springDispatcher", new DispatcherServlet(servletContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.setMultipartConfig(new MultipartConfigElement(null, 20_971_520L, 41_943040L, 512_000));
        dispatcher.addMapping("/");

        final var registrationFilter = appContext.addFilter("registrationFilter", new Filters.FilterForAttribute("username", "/login"));
        registrationFilter.addMappingForUrlPatterns(null, false, "/ticket", "/ticket/*");

        final var adminFilter = appContext.addFilter("adminFilter", new Filters.FilterForAttribute("admin", "/login"));
        adminFilter.addMappingForUrlPatterns(null, false, "/session", "/session/*");
    }
}
