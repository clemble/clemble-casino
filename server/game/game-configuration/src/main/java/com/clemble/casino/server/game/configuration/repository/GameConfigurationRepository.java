package com.clemble.casino.server.game.configuration.repository;

import java.util.List;

import com.clemble.casino.game.lifecycle.configuration.GameConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.clemble.casino.game.Game;

@Repository
public interface GameConfigurationRepository extends MongoRepository<GameConfiguration, String> {

    public List<GameConfiguration> findByGame(@Param("game") Game game);

}
