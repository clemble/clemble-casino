package com.clemble.casino.web.player;

import com.clemble.casino.web.mapping.WebMapping;

public interface PlayerWebMapping extends WebMapping {

    final public static String PLAYER_PROFILE_REGISTRATION = "/player/";
    final public static String PLAYER_PROFILE_REGISTRATION_SOCIAL = "/social/";

    final public static String PLAYER_PROFILE = "/player/{playerId}";
    final public static String PLAYER_SOCIAL = "/social/{playerId}";

    final public static String PLAYER_PRESENCE = "/presence/{playerId}";
    final public static String PLAYER_PRESENCES = "/presence";

    final public static String PLAYER_PRESENCES_PARAM = "players";
    
    final public static String PLAYER_NOTIFICATION_DOMAIN_PATTERN = "%s_notif.%s.%s";
}
