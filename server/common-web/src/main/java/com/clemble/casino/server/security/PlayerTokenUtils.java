package com.clemble.casino.server.security;

import com.clemble.casino.registration.PlayerToken;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
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
        Cookie cookie = new Cookie("player", player);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        // TODO figure out a better way
        cookie.setDomain(domain);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }
}
