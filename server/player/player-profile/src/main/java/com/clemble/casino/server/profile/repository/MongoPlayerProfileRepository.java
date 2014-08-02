package com.clemble.casino.server.profile.repository;

import com.clemble.casino.player.PlayerProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by mavarazy on 6/12/14.
 */
public interface MongoPlayerProfileRepository extends MongoRepository<PlayerProfile, String>, PlayerProfileRepository {

}
