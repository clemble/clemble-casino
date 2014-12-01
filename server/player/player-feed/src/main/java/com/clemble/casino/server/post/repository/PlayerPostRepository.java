package com.clemble.casino.server.post.repository;

import com.clemble.casino.post.PlayerPost;
import com.clemble.casino.server.post.ServerPlayerPost;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.List;

/**
 * Created by mavarazy on 11/30/14.
 */
public interface PlayerPostRepository extends MongoRepository<ServerPlayerPost, String> {

    List<ServerPlayerPost> findByPlayerInOrderByCreated(Collection<String> players);

}
