package com.gogomaya.server.repository.game;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gogomaya.game.Game;
import com.gogomaya.game.specification.GameSpecification;
import com.gogomaya.game.specification.GameSpecificationKey;

@Repository
public interface GameSpecificationRepository extends JpaRepository<GameSpecification, GameSpecificationKey> {

    @Query(value = "select specification from GameSpecification specification where specification.name.game = :game")
    public List<GameSpecification> findByGame(@Param("game") Game game);

}
