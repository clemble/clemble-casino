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
        // Step 1. Encoding player URL
        String encodedPlayer = null;
        try {
            encodedPlayer = URLEncoder.encode(player, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        // Step 2. Setting cookie for the player
        Cookie cookie = new Cookie("player", encodedPlayer);
        cookie.setPath("/");
        cookie.setDomain(domain);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public void signOut(HttpServletResponse response) {
        // Step 1. Removing player
        Cookie cookie = new Cookie("player", "done");
        cookie.setPath("/");
        cookie.setDomain(domain);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}
