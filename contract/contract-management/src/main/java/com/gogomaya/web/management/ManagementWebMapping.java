package com.gogomaya.web.management;

import com.gogomaya.web.mapping.WebMapping;

public interface ManagementWebMapping extends WebMapping {

    final public static String MANAGEMENT_PREFIX = "/management-web";

    final public static String MANAGEMENT_GAME_ACTION_SPECIFICATIONS = "/options/{name}";

    final public static String PLAYER_REGISTRATION_LOGIN = "/registration/login";
    final public static String PLAYER_REGISTRATION_SIGN_IN = "/registration/signin";
    final public static String PLAYER_REGISTRATION_SOCIAL = "/registration/social";

}
