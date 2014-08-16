package com.clemble.casino.server.profile.repository;

import com.clemble.casino.server.profile.PlayerImageRedirect;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by mavarazy on 8/16/14.
 */
public interface PlayerImageRedirectRepository extends MongoRepository<PlayerImageRedirect, String> {
}
