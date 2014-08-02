package com.clemble.casino.server.spring;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.security.oauth.provider.ConsumerDetailsService;
import org.springframework.security.oauth.provider.token.OAuthProviderTokenServices;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.clemble.casino.server.security.ClembleOAuthProviderTokenServices;
import com.clemble.casino.server.security.ClembleSecuritySignatureFilter;
import com.google.common.collect.ImmutableMap;
import com.thetransactioncompany.cors.CORSFilter;

abstract public class AbstractSecureWebApplicationInitializer implements WebApplicationInitializer {

    final private Class<?> configurationClass;

    public AbstractSecureWebApplicationInitializer(Class<?> configurationClass) {
        this.configurationClass = configurationClass;
    }

    @Override
    final public void onStartup(ServletContext container) throws ServletException {
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(configurationClass, OAuthSpringConfiguration.class);
        rootContext.refresh();

        ConsumerDetailsService consumerDetailsService = rootContext.getBean(ConsumerDetailsService.class);
        OAuthProviderTokenServices oAuthProviderTokenServices = rootContext.getBean(ClembleOAuthProviderTokenServices.class);
        container.addFilter("clemble", new ClembleSecuritySignatureFilter(oAuthProviderTokenServices, consumerDetailsService));

        // Step 1. Creating CORS filter
        FilterRegistration.Dynamic filter = container.addFilter("CORS", CORSFilter.class);
        if(filter != null) {
            filter.setInitParameters(ImmutableMap.of("cors.allowOrigin", "*", "cors.supportedHeaders", "Accept, Origin, Content-Type, playerId, sessionId, tableId"));
            filter.addMappingForUrlPatterns(null, false, "/*");
        }
        // Step 2. Proceeding to actual initialization
        // Step 3. Registering appropriate Dispatcher
        ServletRegistration.Dynamic dispatcher = container.addServlet("integration-game", new DispatcherServlet(rootContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }
}
