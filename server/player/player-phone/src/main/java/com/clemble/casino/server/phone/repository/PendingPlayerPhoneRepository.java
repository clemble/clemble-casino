package com.clemble.casino.server.phone.repository;

import com.clemble.casino.server.phone.PendingPlayerPhone;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by mavarazy on 12/8/14.
 */
public interface PendingPlayerPhoneRepository extends MongoRepository<PendingPlayerPhone, String> {
}
