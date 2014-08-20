package com.clemble.casino.server.game.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.clemble.casino.game.construct.ScheduledGame;

@Repository
public interface GameScheduleRepository extends MongoRepository<ScheduledGame, String> {

    public List<ScheduledGame> findByStartDateBetween(long startTime, long endTime);

}
