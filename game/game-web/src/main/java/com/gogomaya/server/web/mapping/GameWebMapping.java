package com.gogomaya.server.web.mapping;

public class GameWebMapping implements WebMapping {

    final public static String GAME_PREFIX = "/spi";

    final public static String GAME_SPECIFICATION_OPTIONS = "/active/options/{name}";

    final public static String GAME_CONSTRUCTION_AUTOMATIC = "/active/session";
    final public static String GAME_CONSTRUCTION = "/active/constuction/{constructionId}";
    final public static String GAME_CONSTRUCTION_GENERIC = "/active/constuction";
    final public static String GAME_CONSTRUCTION_RESPONSE = "/active/constuction/{constructionId}/response";

    final public static String GAME_SESSION_MOVE = "/active/action";

    private GameWebMapping() {
        throw new IllegalAccessError();
    }

}
