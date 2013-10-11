package com.clemble.casino.web.game;

import com.clemble.casino.web.mapping.WebMapping;

public interface GameWebMapping extends WebMapping {

    final public static String GAME_PREFIX = "/picpacpoe-game"; // Temporary

    final public static String GAME_SPECIFICATION_OPTIONS = "/options/{name}";

    final public static String GAME_SESSIONS = "/session";
    final public static String GAME_SESSIONS_SESSION = "/session/{sessionId}";

    final public static String GAME_SESSIONS_CONSTRUCTION = "/session/{sessionId}/constuction";
    final public static String GAME_SESSIONS_CONSTRUCTION_RESPONSES = "/session/{sessionId}/construction/response";
    final public static String GAME_SESSIONS_CONSTRUCTION_RESPONSES_PLAYER = "/session/{sessionId}/construction/response/{playerId}";

    final public static String GAME_SESSIONS_ACTIONS = "/session/{sessionId}/action";
    final public static String GAME_SESSIONS_ACTIONS_ACTION = "/session/{sessionId}/action/{actionId}";

    final public static String GAME_SESSIONS_SERVER = "/session/{sessionId}/server";

}
