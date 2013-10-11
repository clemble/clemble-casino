package com.clemble.casino.web.management;

import com.clemble.casino.web.mapping.WebMapping;

public interface ManagementWebMapping extends WebMapping {

    final public static String MANAGEMENT_PLAYER_LOGIN = "/registration/login";
    final public static String MANAGEMENT_PLAYER_REGISTRATION = "/registration/signin";
    final public static String MANAGEMENT_PLAYER_REGISTRATION_SOCIAL = "/registration/social";

    final public static String MANAGEMENT_PLAYER_SESSIONS = "/player/{playerId}/session";
    final public static String MANAGEMENT_PLAYER_SESSIONS_SESSION = "/player/{playerId}/session/{sessionId}";

    final public static String MANAGEMENT_CONFIGURATION_NOTIFICATION = "/configuration/notification";
    final public static String MANAGEMENT_CONFIGURATION_PAYMENT = "/configuration/payment";
    final public static String MANAGEMENT_CONFIGURATION_PRESENCE = "/configuration/presence";

}
