package com.clemble.casino.server.security;

import com.clemble.casino.registration.PlayerToken;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by mavarazy on 8/8/14.
 */
public class PlayerTokenUtils {

    private PlayerTokenUtils() {
        throw new IllegalAccessError();
    }

    public static PlayerToken updateResponse(PlayerToken token, HttpServletResponse response) {
        Cookie cookie = new Cookie("player", token.getPlayer());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return token;
    }
}
