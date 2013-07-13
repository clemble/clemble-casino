package com.gogomaya.server.web.mapping;

public class GameWebMapping implements WebMapping {

    final public static String GAME_PREFIX = "/spi";

    final public static String GAME_OPTIONS = "/active/options";
    final public static String GAME_MOVE = "/active/action";

    final public static String GAME_CONSTRUCTION = "/active/constuct/{constructionId}";
    final public static String GAME_CONSTRUCTION_AUTOMATIC = "/active/session";
    final public static String GAME_CONSTRUCTION_GENERIC = "/active/constuct";
    final public static String GAME_CONSTRUCTION_RESPONCE = "/active/constuct/responce";

    private GameWebMapping() {
        throw new IllegalAccessError();
    }

}
