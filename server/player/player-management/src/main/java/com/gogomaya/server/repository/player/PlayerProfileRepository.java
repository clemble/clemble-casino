package com.gogomaya.server.repository.player;

import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

import com.gogomaya.player.PlayerProfile;

@Repository
public interface PlayerProfileRepository extends CouchbaseRepository<PlayerProfile, String> {

}
