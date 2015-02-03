package com.clemble.server.tag.repository;

import com.clemble.server.tag.ServerPlayerTags;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by mavarazy on 2/3/15.
 */
public interface ServerPlayerTagsRepository extends MongoRepository<ServerPlayerTags, String> {
}
