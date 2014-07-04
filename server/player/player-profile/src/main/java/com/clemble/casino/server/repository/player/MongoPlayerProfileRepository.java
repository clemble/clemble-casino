package com.clemble.casino.server.repository.player;

import com.clemble.casino.player.PlayerProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by mavarazy on 6/12/14.
 */
public interface MongoPlayerProfileRepository extends MongoRepository<PlayerProfile, String>, PlayerProfileRepository {

}
