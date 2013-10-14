package com.clemble.casino.server.spring.web.player;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.clemble.casino.server.spring.web.BasicWebApplicationInitializer;

public class PlayerWebApplicationInitializer extends BasicWebApplicationInitializer {

    @Override
    protected void doInit(ServletContext container) throws ServletException {
        // Step 2. Create the 'root' Spring application context
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(PlayerWebSpringConfiguration.class);
        // Step 3. Registering appropriate Dispatcher
        ServletRegistration.Dynamic dispatcher = container.addServlet("player", new DispatcherServlet(rootContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }

}
