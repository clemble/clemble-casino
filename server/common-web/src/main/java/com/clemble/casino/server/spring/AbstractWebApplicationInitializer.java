package com.clemble.casino.server.spring;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.WebApplicationInitializer;

import com.google.common.collect.ImmutableMap;
import com.thetransactioncompany.cors.CORSFilter;

abstract public class AbstractWebApplicationInitializer implements WebApplicationInitializer {

    @Override
    final public void onStartup(ServletContext container) throws ServletException {
        // Step 1. Creating CORS filter
        FilterRegistration.Dynamic filter = container.addFilter("CORS", CORSFilter.class);
        if(filter != null) {
            filter.setInitParameters(ImmutableMap.of("cors.allowOrigin", "*", "cors.supportedHeaders", "Accept, Origin, Content-Type, playerId, sessionId, tableId"));
            filter.addMappingForUrlPatterns(null, false, "/*");
        }
        // Step 2. Proceeding to actual initialization
        doInit(container);
    }

    abstract protected void doInit(ServletContext container) throws ServletException;
}
