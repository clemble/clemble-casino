package com.clemble.casino.server.connection;

import com.clemble.casino.player.PlayerAware;
import org.springframework.social.connect.ConnectionKey;

import java.util.List;
import java.util.Set;

/**
 * Created by mavarazy on 11/11/14.
 */
public interface PlayerGraph extends PlayerAware {

    public void addOwned(ConnectionKey connectionKey);

    public void addConnection(String player);

    public Set<String> fetchConnections();
}
