package com.clemble.casino.server.game.configuration.repository;

import java.util.List;

import com.clemble.casino.server.game.configuration.ServerGameConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.clemble.casino.game.Game;

@Repository
public interface ServerGameConfigurationRepository extends MongoRepository<ServerGameConfiguration, String> {

    public List<ServerGameConfiguration> findByConfigurationGame(@Param("game") Game game);

}
