package com.clemble.casino.server.spring;

import com.clemble.casino.server.security.AuthenticationHandleInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import com.clemble.casino.server.spring.common.JsonSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;

@Configuration
@Import({ JsonSpringConfiguration.class })
public class WebCommonSpringConfiguration extends WebBasicSpringConfiguration implements SpringConfiguration {

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationHandleInterceptor());
    }

}
