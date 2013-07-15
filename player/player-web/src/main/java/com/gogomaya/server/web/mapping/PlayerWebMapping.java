package com.gogomaya.server.web.mapping;

public class PlayerWebMapping implements WebMapping {

    final public static String PLAYER_PREFIX = "/spi";

    final public static String PLAYER_REGISTRATION_LOGIN = "/registration/login";
    final public static String PLAYER_REGISTRATION_SIGN_IN = "/registration/signin";
    final public static String PLAYER_REGISTRATION_SOCIAL = "/registration/social";

    final public static String PLAYER_PROFILE = "/player/{playerId}/profile";
    final public static String PLAYER_SESSIONS = "/player/{playerId}/session";
    final public static String PLAYER_SESSIONS_SESSION = "/player/{playerId}/session/{sessionId}";

    private PlayerWebMapping() {
        throw new IllegalAccessError();
    }

}
