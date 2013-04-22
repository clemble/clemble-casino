package com.gogomaya.server.game.match;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gogomaya.server.game.SpecificationName;
import com.gogomaya.server.game.tictactoe.TicTacToeSpecification;

@Repository
public interface TicTacToeSpecificationRepository extends JpaRepository<TicTacToeSpecification, SpecificationName>{

}
