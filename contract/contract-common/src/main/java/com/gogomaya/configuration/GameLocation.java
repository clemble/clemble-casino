package com.gogomaya.configuration;

import com.gogomaya.game.Game;

public class GameLocation {

    final private Game game;

    final private String url;

    public GameLocation(Game game, String url) {
        this.game = game;
        this.url = url;
    }

    public Game getGame() {
        return game;
    }

    public String getUrl() {
        return url;
    }

}
