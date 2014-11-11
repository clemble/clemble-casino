package com.clemble.casino.server.connection.repository;

import com.clemble.casino.server.connection.MongoPlayerGraph;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * Created by mavarazy on 8/12/14.
 */
@Repository
public interface MongoPlayerGraphRepository extends MongoRepository<MongoPlayerGraph, String>, PlayerGraphRepository<MongoPlayerGraph, String> {

    public List<MongoPlayerGraph> findByOwnedIn(Collection<ConnectionKey> connectionKey);

}
