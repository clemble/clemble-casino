package com.clemble.casino.server.email;

import com.clemble.casino.player.PlayerAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

/**
 * Created by mavarazy on 12/6/14.
 */
public class PlayerEmail implements PlayerAware {

    @Id
    final private String player;
    final private String email;

    @JsonCreator
    public PlayerEmail(
        @JsonProperty(PLAYER) String player,
        @JsonProperty("email") String email) {
        this.player = player;
        this.email = email;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerEmail that = (PlayerEmail) o;

        if (!email.equals(that.email)) return false;
        if (!player.equals(that.player)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = player.hashCode();
        result = 31 * result + email.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return player + ":" + email;
    }

}
