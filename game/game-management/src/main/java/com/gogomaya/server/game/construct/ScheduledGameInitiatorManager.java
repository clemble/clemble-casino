package com.gogomaya.server.game.construct;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.ExecutorService;

import com.gogomaya.server.repository.game.GameScheduleRepository;

public class ScheduledGameInitiatorManager implements GameInitiatorManager {

//    final private MinMaxPriorityQueue<ScheduledGame> maxScheduledQueue = MinMaxPriorityQueue.<ScheduledGame>maximumSize(100).<ScheduledGame>create();
//    final private ExecutorService scheduledExecutorService;
    final private GameScheduleRepository scheduleRepository;

    public ScheduledGameInitiatorManager(GameScheduleRepository scheduleRepository, ExecutorService scheduledExecutorService) {
//        this.scheduledExecutorService = checkNotNull(scheduledExecutorService);
        this.scheduleRepository = checkNotNull(scheduleRepository);
    }

    @Override
    public void register(GameConstruction construction) {
        // Step 1. Sanity check
        if (construction == null) {
            throw new IllegalArgumentException("Construction can't be NULL");
        }
        if (!(construction.getRequest() instanceof ScheduledGameRequest)) {
            throw new IllegalArgumentException("Request of wrong class");
        }
        // Step 2. Saving scheduled game
        long startTime = ((ScheduledGameRequest) construction.getRequest()).getStartTime().getTime();
        ScheduledGame scheduledGame = new ScheduledGame(startTime, construction.getSession());
        scheduledGame = scheduleRepository.saveAndFlush(scheduledGame);
        // Step 3. Checking value
    }

}