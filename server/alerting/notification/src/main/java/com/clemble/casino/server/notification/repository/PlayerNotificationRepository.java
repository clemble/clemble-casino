package com.clemble.casino.server.notification.repository;

import com.clemble.casino.notification.PlayerNotification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by mavarazy on 11/29/14.
 */
public interface PlayerNotificationRepository extends MongoRepository<PlayerNotification, String>{

    public List<PlayerNotification> findByPlayerOrderByCreatedDesc(String player);

}
