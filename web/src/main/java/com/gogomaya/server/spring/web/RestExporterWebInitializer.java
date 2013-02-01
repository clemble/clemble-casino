package com.gogomaya.server.spring.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
public class RestExporterWebInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // Step 1. Create Spring application context
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(RepositoryRestMvcConfiguration.class);
        // Step 2. Registering Context loader listener to properly manage started context
        servletContext.addListener(new ContextLoaderListener(rootContext));
        // Step 3. Generating REST Servlet to export existing Repositories
        DispatcherServlet exporter = new DispatcherServlet(rootContext);
        // Step 4. Providing appropriate mappings
        ServletRegistration.Dynamic registration = servletContext.addServlet("rest-exporter", exporter);
        registration.setLoadOnStartup(1);
        registration.addMapping("/api/*");
    }

}
