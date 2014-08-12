package com.clemble.casino.server.connection.repository;

import com.clemble.casino.player.PlayerConnections;
import com.clemble.casino.server.connection.GraphConnectionKey;
import com.clemble.casino.server.connection.GraphPlayerConnections;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * Created by mavarazy on 8/12/14.
 */
@Repository
public interface MongoPlayerConnectionsRepository extends MongoRepository<PlayerConnections, String> {

    public List<PlayerConnections> findByOwnedIn(Collection<ConnectionKey> connectionKey);

}
