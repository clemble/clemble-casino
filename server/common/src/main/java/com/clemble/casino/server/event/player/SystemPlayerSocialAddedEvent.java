package com.clemble.casino.server.event.player;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.player.ConnectionKeyAware;
import com.clemble.casino.server.event.SystemEvent;
import org.springframework.social.connect.ConnectionKey;

import com.clemble.casino.player.PlayerAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SystemPlayerSocialAddedEvent implements
    SystemEvent,
    ConnectionKeyAware,
    PlayerAware {

    /**
     * Generated 06/01/14
     */
    private static final long serialVersionUID = 5081512807506108684L;
    
    final public static String CHANNEL = "social:added";

    final private String player;
    final private ConnectionKey connection;

    @JsonCreator
    public SystemPlayerSocialAddedEvent(@JsonProperty("player") String player, @JsonProperty("connection") ConnectionKey connection) {
        this.player = checkNotNull(player);
        this.connection = checkNotNull(connection);
    }

    @Override
    public String getPlayer() {
        return player;
    }

    @Override
    public ConnectionKey getConnection() {
        return connection;
    }

    @Override
    public String getChannel(){
        return CHANNEL;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((connection == null) ? 0 : connection.hashCode());
        result = prime * result + ((player == null) ? 0 : player.hashCode());
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
        SystemPlayerSocialAddedEvent other = (SystemPlayerSocialAddedEvent) obj;
        if (connection == null) {
            if (other.connection != null)
                return false;
        } else if (!connection.equals(other.connection))
            return false;
        if (player == null) {
            if (other.player != null)
                return false;
        } else if (!player.equals(other.player))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "sys:" + player + ":" + CHANNEL;
    }

}
