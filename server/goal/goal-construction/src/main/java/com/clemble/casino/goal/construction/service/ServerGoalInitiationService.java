package com.clemble.casino.goal.construction.service;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.construction.event.GameInitiatedEvent;
import com.clemble.casino.game.construction.service.GameInitiationService;
import com.clemble.casino.goal.construction.GoalInitiation;
import com.clemble.casino.goal.construction.GoalStartTask;
import com.clemble.casino.goal.construction.event.GoalInitiatedEvent;
import com.clemble.casino.goal.construction.repository.GoalInitiationRepository;
import com.clemble.casino.server.executor.EventTaskExecutor;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by mavarazy on 9/13/14.
 */
public class ServerGoalInitiationService implements GoalInitiationService {

    final private Logger LOG = LoggerFactory.getLogger(ServerGoalInitiationService.class);

    final private GoalInitiationRepository initiationRepository;
    final private PlayerNotificationService notificationService;
    final private EventTaskExecutor taskExecutor;

    public ServerGoalInitiationService(GoalInitiationRepository initiationRepository, PlayerNotificationService notificationService, EventTaskExecutor taskExecutor) {
        this.initiationRepository = initiationRepository;
        this.notificationService = notificationService;
        this.taskExecutor = taskExecutor;
    }

    public void start(GoalInitiation initiation) {
        LOG.debug("Creating {}", initiation);
        // Step 1. Sanity check
        if (initiation == null) {
            LOG.error("Invalid initiation {}", initiation);
            throw ClembleCasinoException.withKey(ClembleCasinoError.ServerError, initiation.getGoalKey());
        }
        if (initiationRepository.exists(initiation.getGoalKey())) {
            LOG.error("Already have {} pending", initiation.getGoalKey());
            throw ClembleCasinoException.withKey(ClembleCasinoError.ServerError, initiation.getGoalKey());
        }
        // Step 2. Adding to internal cache
        LOG.debug("Adding new pending goal {}", initiation.getGoalKey());
        initiationRepository.save(initiation);
        // Step 3. Sending notification to the players, that they need to confirm
        LOG.debug("Notifying player {}", initiation);
        notificationService.notify(initiation.getPlayer(), new GoalInitiatedEvent(initiation.getGoalKey()));
        // Step 4. Scheduling Cancel task
        taskExecutor.schedule(new GoalStartTask(initiation.getGoalKey(), initiation.getStartDate()));
    }

    @Override
    public Collection<GoalInitiation> getPending() {
        throw new UnsupportedOperationException();
    }

    public Collection<GoalInitiation> getPending(String player) {
        return initiationRepository.findByPlayer(player);
    }

}
