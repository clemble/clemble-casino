package com.clemble.casino.server.profile;

import org.springframework.data.annotation.Id;

/**
 * Created by mavarazy on 8/16/14.
 */
public class PlayerImageRedirect {

    @Id
    final private String player;
    final private String redirect;

    public PlayerImageRedirect(String player, String redirect) {
        this.player = player;
        this.redirect = redirect;
    }

    public String getPlayer() {
        return player;
    }

    public String getRedirect() {
        return redirect;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerImageRedirect that = (PlayerImageRedirect) o;

        if (!player.equals(that.player)) return false;
        if (!redirect.equals(that.redirect)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = player.hashCode();
        result = 31 * result + redirect.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "player:image:" + player + ':' + redirect;
    }
}
