package com.clemble.casino.server.connection.service;

import com.clemble.casino.player.service.PlayerConnectionService;
import org.springframework.social.connect.ConnectionKey;

import java.util.Collection;

/**
 * Created by mavarazy on 8/12/14.
 */
public interface PlayerGraphService extends PlayerConnectionService {

    boolean addOwned(String player, ConnectionKey connectionKey);

    boolean create(String player);

    Iterable<String> getOwners(Collection<ConnectionKey> connectionKeys);

    boolean connect(String a, String b);

}
