package com.clemble.casino.server.game.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.configuration.ServerGameConfiguration;
import com.clemble.casino.game.specification.GameConfigurationKey;

@Repository
public interface ServerGameConfigurationRepository extends JpaRepository<ServerGameConfiguration, GameConfigurationKey>, JpaSpecificationExecutor<ServerGameConfiguration> {

    @Query(value = "select configuration from ServerGameConfiguration configuration where configuration.configurationKey.game = :game")
    public List<ServerGameConfiguration> findByGame(@Param("game") Game game);

}
