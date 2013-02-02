package com.gogomaya.server.web;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public class SocialHandlerMapping extends RequestMappingHandlerMapping {

    public SocialHandlerMapping() {
        setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Override
    protected boolean isHandler(Class<?> beanType) {
        return (SocialConnectionDataController.class.isAssignableFrom(beanType));
    }
}
