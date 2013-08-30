package com.gogomaya.web.player;

import com.gogomaya.web.mapping.WebMapping;

public interface PlayerWebMapping extends WebMapping {

    final public static String PLAYER_PREFIX = "/player-web";

    // TODO Change to single entry point
    final public static String PLAYER_REGISTRATION_LOGIN = "/registration/login";
    final public static String PLAYER_REGISTRATION_SIGN_IN = "/registration/signin";
    final public static String PLAYER_REGISTRATION_SOCIAL = "/registration/social";

    final public static String PLAYER_PROFILE = "/player/{playerId}/profile";
    final public static String PLAYER_SESSIONS = "/player/{playerId}/session";
    final public static String PLAYER_SESSIONS_SESSION = "/player/{playerId}/session/{sessionId}";

}
