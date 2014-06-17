package com.clemble.casino.server.spring.web.management;

import com.clemble.casino.server.spring.web.AbstractWebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

// TODO Consider deprecated

public class ManagementWebApplicationInitializer extends AbstractWebApplicationInitializer {

    @Override
    protected void doInit(ServletContext container) throws ServletException {
        // Step 1. Create the 'root' Spring application context
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(ManagementWebSpringConfiguration.class);
        // Step 2. Registering appropriate Dispatcher
        ServletRegistration.Dynamic dispatcher = container.addServlet("management", new DispatcherServlet(rootContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }

}
