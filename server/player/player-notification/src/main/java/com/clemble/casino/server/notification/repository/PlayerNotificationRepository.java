package com.clemble.casino.server.notification.repository;

import com.clemble.casino.server.notification.ServerPlayerNotification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by mavarazy on 11/29/14.
 */
public interface PlayerNotificationRepository extends MongoRepository<ServerPlayerNotification, String>{

    public List<ServerPlayerNotification> findByPlayer(String player);

}
