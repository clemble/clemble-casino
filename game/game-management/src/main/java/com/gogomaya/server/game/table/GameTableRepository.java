package com.gogomaya.server.game.table;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.GameTable;

@Repository
public interface GameTableRepository<State extends GameState> extends JpaRepository<GameTable<State>, Long> {

    @Query("select table from GameTable table where table.currentSession.session = ?")
    public GameTable<State> findBySessionId(long session);

}
