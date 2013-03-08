package com.gogomaya.social;

import javax.servlet.http.HttpServletResponse;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;

public final class SimpleSignInAdapter implements SignInAdapter {

    private final UserCookieGenerator userCookieGenerator = new UserCookieGenerator();

    public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
        userCookieGenerator.addCookie(userId, request.getNativeResponse(HttpServletResponse.class));
        return null;
    }

}