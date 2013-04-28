package com.gogomaya.server.game.match;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gogomaya.server.game.GameSpecification;
import com.gogomaya.server.game.SpecificationName;

@Repository
public interface TicTacToeSpecificationRepository extends JpaRepository<GameSpecification, SpecificationName> {

}
