package com.clemble.casino.server.security;

import com.clemble.casino.registration.RegistrationWebMapping;
import com.clemble.casino.social.SocialWebMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by mavarazy on 8/9/14.
 */
public class AuthenticationHandleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Step 1. Checking player cookie present
        if(request.getCookies() != null)
            for(Cookie cookie: request.getCookies())
                if(cookie.getName().equals("player"))
                    return true;
        // Step 2. Checking this is not one of registration URL's
        String uri = request.getRequestURI();
        if (uri.startsWith(RegistrationWebMapping.REGISTRATION_BASE) ||
            uri.startsWith(SocialWebMapping.SOCIAL_REGISTRATION_BASE) ||
            uri.startsWith(SocialWebMapping.SOCIAL_SIGN_IN_BASE))
            return true;
        // Step 3. Sending 401 Response
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.flushBuffer();
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

}
