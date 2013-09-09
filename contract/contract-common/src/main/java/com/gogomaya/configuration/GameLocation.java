package com.gogomaya.configuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gogomaya.game.Game;

public class GameLocation {

    final private Game game;

    final private String url;

    @JsonCreator
    public GameLocation(@JsonProperty("game") Game game, @JsonProperty("url") String url) {
        this.game = game;
        this.url = url;
    }

    public Game getGame() {
        return game;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((game == null) ? 0 : game.hashCode());
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GameLocation other = (GameLocation) obj;
        if (game != other.game)
            return false;
        if (url == null) {
            if (other.url != null)
                return false;
        } else if (!url.equals(other.url))
            return false;
        return true;
    }

}
