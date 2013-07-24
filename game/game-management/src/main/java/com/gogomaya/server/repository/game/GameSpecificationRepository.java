package com.gogomaya.server.repository.game;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.specification.SpecificationName;

@Repository
public interface GameSpecificationRepository extends JpaRepository<GameSpecification, SpecificationName> {

    @Query(value = "select specification from GameSpecification specification where specification.name.game = :game")
    public List<GameSpecification> findByGame(@Param("game") String game);

}
