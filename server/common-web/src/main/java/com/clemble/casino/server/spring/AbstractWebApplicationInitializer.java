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

abstract public class AbstractWebApplicationInitializer implements WebApplicationInitializer {

    final private Class[] configurations;

    public AbstractWebApplicationInitializer(Class ... configurations){
        this.configurations = configurations;
    }

    @Override
    final public void onStartup(ServletContext container) throws ServletException {
        // Step 1. Creating CORS filter
        FilterRegistration.Dynamic filter = container.addFilter("CORS", CORSFilter.class);
        if(filter != null) {
            filter.setInitParameters(ImmutableMap.of("cors.allowOrigin", "*", "cors.supportedHeaders", "Accept, Origin, Content-Type, playerId, sessionId, tableId"));
            filter.addMappingForUrlPatterns(null, false, "/*");
        }
        // Step 2. Proceeding to actual initialization
                // Step 2. Create the 'root' Spring application context
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        for(Class<?> configuration: configurations)
            rootContext.register(configuration);
        rootContext.register(WebCommonSpringConfiguration.class);
        // Step 3. Registering appropriate Dispatcher
        ServletRegistration.Dynamic dispatcher = container.addServlet(getClass().getSimpleName(), new DispatcherServlet(rootContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");;
    }

}

//abstract public class AbstractWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {
// TODO resolve, when enabling security
//    public AbstractWebApplicationInitializer(Class ... configurations){
//        super(configurations);
//    }
//
//}
