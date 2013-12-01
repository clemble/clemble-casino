package com.clemble.casino.web.game;

import com.clemble.casino.web.mapping.WebMapping;

public interface GameWebMapping extends WebMapping {

    final public static String GAME_SPECIFICATION_OPTIONS = "/options/{name}";

    final public static String GAME_SESSIONS = "/game";
    final public static String GAME_SESSIONS_SESSION = "/{game}/{session}";

    final public static String GAME_CONSTRUCTION = "/{game}/{session}/constuction";
    final public static String GAME_CONSTRUCTION_RESPONSES = "/{game}/{session}/construction/response";
    final public static String GAME_CONSTRUCTION_RESPONSES_PLAYER = "/{game}/{session}/construction/response/{playerId}";

    final public static String GAME_SESSIONS_STATE = "/{game}/{session}/state";
    final public static String GAME_SESSIONS_ACTIONS = "/{game}/{session}/action";
    final public static String GAME_SESSIONS_ACTIONS_ACTION = "/{game}/{session}/action/{actionId}";

}
