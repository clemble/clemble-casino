package com.clemble.casino.server.spring;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import com.sun.org.apache.xerces.internal.parsers.SecurityConfiguration;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.WebApplicationInitializer;

import com.google.common.collect.ImmutableMap;
import com.thetransactioncompany.cors.CORSFilter;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

abstract public class AbstractWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

    public AbstractWebApplicationInitializer(Class ... configurations){
        super(configurations);
    }

}
