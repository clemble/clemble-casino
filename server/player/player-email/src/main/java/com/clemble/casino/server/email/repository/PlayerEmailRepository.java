package com.clemble.casino.server.email.repository;

import com.clemble.casino.server.email.PlayerEmail;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by mavarazy on 12/6/14.
 */
public interface PlayerEmailRepository extends MongoRepository<PlayerEmail, String> {
}
