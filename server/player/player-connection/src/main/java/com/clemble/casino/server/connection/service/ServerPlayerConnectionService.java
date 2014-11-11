package com.clemble.casino.server.connection.service;

import com.clemble.casino.player.FriendInvitation;
import com.clemble.casino.player.PlayerConnections;
import com.clemble.casino.player.event.PlayerInvitationAction;
import com.clemble.casino.player.service.PlayerConnectionService;
import org.springframework.social.connect.ConnectionKey;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by mavarazy on 8/12/14.
 */
public interface ServerPlayerConnectionService extends PlayerConnectionService {

    PlayerConnections save(PlayerConnections connections);

    PlayerConnections getServerConnection(String player);

    Iterable<PlayerConnections> getOwners(Collection<ConnectionKey> connectionKeys);

}
