package com.clemble.casino.server.social;

import com.clemble.casino.player.PlayerAware;
import org.springframework.social.connect.Connection;

/**
 * Created by mavarazy on 7/4/14.
 */
public class SocialConnection implements PlayerAware {

    final private String player;
    final private Connection<?> connection;

    public SocialConnection(String player, Connection<?> connection) {
        this.player = player;
        this.connection = connection;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    public Connection<?> getConnection() {
        return connection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SocialConnection that = (SocialConnection) o;

        if (connection != null ? !connection.equals(that.connection) : that.connection != null) return false;
        if (player != null ? !player.equals(that.player) : that.player != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = player != null ? player.hashCode() : 0;
        result = 31 * result + (connection != null ? connection.hashCode() : 0);
        return result;
    }
}
