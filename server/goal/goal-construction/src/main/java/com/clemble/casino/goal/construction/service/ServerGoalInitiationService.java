package com.clemble.casino.goal.construction.service;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.goal.construction.GoalInitiation;
import com.clemble.casino.goal.construction.GoalInitiationExpirationTask;
import com.clemble.casino.goal.construction.event.GoalInitiationCreatedEvent;
import com.clemble.casino.goal.construction.repository.GoalInitiationRepository;
import com.clemble.casino.server.event.goal.SystemGoalInitiationDueEvent;
import com.clemble.casino.server.executor.EventTaskExecutor;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

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
        notificationService.notify(initiation.getPlayer(), new GoalInitiationCreatedEvent(initiation.getGoalKey()));
        // Step 4. Scheduling Cancel task
        taskExecutor.schedule(new GoalInitiationExpirationTask(initiation.getGoalKey(), initiation.getStartDate()));
    }

    public void initiate(SystemGoalInitiationDueEvent dueEvent) {

    }

    @Override
    public Collection<GoalInitiation> getPending() {
        throw new UnsupportedOperationException();
    }

    public Collection<GoalInitiation> getPending(String player) {
        return initiationRepository.findByPlayer(player);
    }

}
