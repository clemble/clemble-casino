package com.gogomaya.server.game.table;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gogomaya.server.game.action.GameTable;

public interface GameTableRepository<T extends GameTable<?>> extends JpaRepository<T, Long> {

}
