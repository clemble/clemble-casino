package com.clemble.casino.server.profile.spring;

import com.clemble.casino.server.spring.AbstractWebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Created by mavarazy on 7/4/14.
 */
public class PlayerProfileApplicationInitializer extends AbstractWebApplicationInitializer {

    public PlayerProfileApplicationInitializer() {
        super(PlayerProfileSpringConfiguration.class);
    }

}
