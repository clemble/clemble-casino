package com.gogomaya.server.game.table;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gogomaya.server.game.table.GameTable;

public interface GameTableRepository extends JpaRepository<GameTable, Long> {

    GameTable findByPlayersIn(String player);

}
