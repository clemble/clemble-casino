package com.gogomaya.server.repository.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gogomaya.game.GameState;
import com.gogomaya.game.GameTable;

@Repository
public interface GameTableRepository<State extends GameState> extends JpaRepository<GameTable<State>, Long> {

    @Query("select table from GameTable table where table.currentSession.session = ?")
    public GameTable<State> findBySessionId(long session);

}
