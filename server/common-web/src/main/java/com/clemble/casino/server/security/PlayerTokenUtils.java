package com.clemble.casino.server.security;

import com.clemble.casino.registration.PlayerToken;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

/**
 * Created by mavarazy on 8/8/14.
 */
public class PlayerTokenUtils {

    final private int maxAge;
    final private String domain;

    public PlayerTokenUtils(String domain, int maxAge) {
        this.domain = domain;
        this.maxAge = maxAge;
    }

    public void updateResponse(String player, HttpServletResponse response) {
        try {
            Cookie cookie = new Cookie("player", URLEncoder.encode(player, "UTF-8"));
            cookie.setPath("/");
//            cookie.setHttpOnly(true);
            cookie.setDomain(domain);
            cookie.setMaxAge(maxAge);
            response.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
