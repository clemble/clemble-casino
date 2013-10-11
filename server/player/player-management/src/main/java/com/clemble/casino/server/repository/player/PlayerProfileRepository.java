package com.clemble.casino.server.repository.player;

import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

import com.clemble.casino.player.PlayerProfile;

@Repository
public interface PlayerProfileRepository extends CouchbaseRepository<PlayerProfile, String> {

}
