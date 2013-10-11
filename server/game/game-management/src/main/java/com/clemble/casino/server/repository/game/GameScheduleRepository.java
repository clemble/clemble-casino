package com.clemble.casino.server.repository.game;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.construct.ScheduledGame;

@Repository
public interface GameScheduleRepository extends JpaRepository<ScheduledGame, GameSessionKey> {

    public List<ScheduledGame> findByStartDateBetween(long startTime, long endTime);

}
