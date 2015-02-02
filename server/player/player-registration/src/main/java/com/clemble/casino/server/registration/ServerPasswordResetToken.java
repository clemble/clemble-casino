package com.clemble.casino.server.registration;

import com.clemble.casino.player.PlayerAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

/**
 * Created by mavarazy on 2/2/15.
 */
public class ServerPasswordResetToken implements PlayerAware {

    @Id
    final private String player;
    final private String token;
    // Number of attempts player can have mistakes in token is 1, after that token is removed

    @JsonCreator
    public ServerPasswordResetToken(@JsonProperty(PLAYER) String player, @JsonProperty("token") String token) {
        this.player = player;
        this.token = token;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "reset:" + player;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerPasswordResetToken)) return false;

        ServerPasswordResetToken that = (ServerPasswordResetToken) o;

        if (!token.equals(that.token)) return false;
        if (!player.equals(that.player)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = player.hashCode();
        result = 31 * result + token.hashCode();
        return result;
    }

}
