package com.clemble.casino.server.registration.repository;

import com.clemble.casino.server.registration.ServerPasswordResetToken;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by mavarazy on 2/2/15.
 */
public interface ServerPasswordResetTokenRepository extends MongoRepository<ServerPasswordResetToken, String> {
}
