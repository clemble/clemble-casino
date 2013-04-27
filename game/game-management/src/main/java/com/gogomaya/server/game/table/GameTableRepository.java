package com.gogomaya.server.game.table;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.GameTable;

public interface GameTableRepository<Table extends GameTable<State>, State extends GameState> extends JpaRepository<Table, Long> {

}
