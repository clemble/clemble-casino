package com.clemble.casino.server.connection.repository;

import com.clemble.casino.server.connection.ServerFriendInvitation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by mavarazy on 11/11/14.
 */
public interface PlayerFriendInvitationRepository extends MongoRepository<ServerFriendInvitation, String> {

    List<ServerFriendInvitation> findByReceiver(String player);

}
