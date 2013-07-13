package com.gogomaya.server.web.mapping;

public class PlayerWebMapping implements WebMapping {

    final public static String PLAYER_PREFIX = "/spi";

    final public static String PLAYER_SESSION_LOGIN = "/session/login";
    final public static String PLAYER_REGISTRATION_LOGIN = "/registration/login";
    final public static String PLAYER_REGISTRATION_SIGN_IN = "/registration/signin";
    final public static String PLAYER_REGISTRATION_SOCIAL = "/registration/social";

    private PlayerWebMapping() {
        throw new IllegalAccessError();
    }

}
