package com.clemble.casino.server.security;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by mavarazy on 8/8/14.
 */
public class PlayerTokenUtils {

    final private int maxAge;
    final private String host;

    public PlayerTokenUtils(String host, int maxAge) {
        this.host = host;
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
        cookie.setDomain(host);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
        // Step 3. Specifying player in response
        try {
            response.getOutputStream().write(player.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void signOut(HttpServletResponse response) {
        // Step 1. Removing player
        Cookie cookie = new Cookie("player", "done");
        cookie.setPath("/");
        cookie.setDomain(host);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}
