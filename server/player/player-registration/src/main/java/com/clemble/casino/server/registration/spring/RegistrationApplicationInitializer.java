package com.clemble.casino.server.registration.spring;

import com.clemble.casino.server.spring.AbstractWebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Created by mavarazy on 7/4/14.
 */
public class RegistrationApplicationInitializer extends AbstractWebApplicationInitializer {

    public RegistrationApplicationInitializer() {
        super(RegistrationSpringConfiguration.class);
    }

}
