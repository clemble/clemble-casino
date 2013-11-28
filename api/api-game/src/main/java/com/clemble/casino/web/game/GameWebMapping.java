package com.clemble.casino.web.game;

import com.clemble.casino.web.mapping.WebMapping;

public interface GameWebMapping extends WebMapping {

    final public static String GAME_SPECIFICATION_OPTIONS = "/options/{name}";

    final public static String GAME_SESSIONS = "/game";
    final public static String GAME_SESSIONS_SESSION = "/{game}/{sessionId}";

    final public static String GAME_SESSIONS_CONSTRUCTION = "/{game}/{sessionId}/constuction";
    final public static String GAME_SESSIONS_CONSTRUCTION_RESPONSES = "/{game}/{sessionId}/construction/response";
    final public static String GAME_SESSIONS_CONSTRUCTION_RESPONSES_PLAYER = "/{game}/{sessionId}/construction/response/{playerId}";

    final public static String GAME_SESSIONS_STATE = "/{game}/{session}/state";
    final public static String GAME_SESSIONS_ACTIONS = "/{game}/{session}/action";
    final public static String GAME_SESSIONS_ACTIONS_ACTION = "/{game}/{session}/action/{actionId}";

    final public static String GAME_SESSIONS_SERVER = "/{game}/{session}/server";

}
