package com.clemble.casino.server.phone.repository;

import com.clemble.casino.server.phone.ServerPlayerPhone;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by mavarazy on 12/8/14.
 */
public interface PlayerPhoneRepository extends MongoRepository<ServerPlayerPhone, String> {
}
