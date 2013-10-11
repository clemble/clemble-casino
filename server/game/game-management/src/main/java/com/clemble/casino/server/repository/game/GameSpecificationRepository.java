package com.clemble.casino.server.repository.game;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.specification.GameSpecification;
import com.clemble.casino.game.specification.GameSpecificationKey;

@Repository
public interface GameSpecificationRepository extends JpaRepository<GameSpecification, GameSpecificationKey> {

    @Query(value = "select specification from GameSpecification specification where specification.name.game = :game")
    public List<GameSpecification> findByGame(@Param("game") Game game);

}
