package com.gogomaya.server.player.notification;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.server.ServerRegistry;

@JsonTypeName("simple")
public class SimplePlayerNotificationRegistry implements PlayerNotificationRegistry {

    /**
     * Generated 
     */
    private static final long serialVersionUID = 3216512055379596980L;

    final private ServerRegistry serverRegistry;

    @JsonCreator
    public SimplePlayerNotificationRegistry(@JsonProperty("serverRegistry") ServerRegistry serverRegistry) {
        this.serverRegistry = checkNotNull(serverRegistry);
    }

    @Override
    public String findNotificationServer(String player) {
        return serverRegistry.find(player);
    }

    public ServerRegistry getServerRegistry() {
        return serverRegistry;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((serverRegistry == null) ? 0 : serverRegistry.hashCode());
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
        SimplePlayerNotificationRegistry other = (SimplePlayerNotificationRegistry) obj;
        if (serverRegistry == null) {
            if (other.serverRegistry != null)
                return false;
        } else if (!serverRegistry.equals(other.serverRegistry))
            return false;
        return true;
    }

}
