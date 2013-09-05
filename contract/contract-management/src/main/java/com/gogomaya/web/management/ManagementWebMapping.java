package com.gogomaya.web.management;

import com.gogomaya.web.mapping.WebMapping;

public interface ManagementWebMapping extends WebMapping {

    final public static String MANAGEMENT_PREFIX = "/management-web";

    final public static String MANAGEMENT_GAME_ACTION_SPECIFICATIONS = "/options/{name}";

    final public static String MANAGEMENT_PLAYER_LOGIN = "/registration/login";
    final public static String MANAGEMENT_PLAYER_REGISTRATION = "/registration/signin";
    final public static String MANAGEMENT_PLAYER_REGISTRATION_SOCIAL = "/registration/social";

}
