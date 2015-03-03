package com.clemble.casino.goal.construction.service;

import com.clemble.casino.goal.lifecycle.initiation.service.GoalInitiationService;
import com.clemble.casino.lifecycle.initiation.InitiationState;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.goal.lifecycle.initiation.GoalInitiation;
import com.clemble.casino.goal.lifecycle.initiation.event.GoalInitiationCreatedEvent;
import com.clemble.casino.goal.construction.repository.GoalInitiationRepository;
import com.clemble.casino.money.Money;
import com.clemble.casino.money.Operation;
import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PendingTransaction;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.event.SystemEvent;
import com.clemble.casino.server.event.goal.SystemGoalInitiationDueEvent;
import com.clemble.casino.server.event.payment.SystemPaymentFreezeRequestEvent;
import com.clemble.casino.server.event.schedule.SystemAddJobScheduleEvent;
import com.clemble.casino.server.event.schedule.SystemRemoveJobScheduleEvent;
import com.clemble.casino.server.player.notification.ServerNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.google.common.collect.ImmutableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Set;

/**
 * Created by mavarazy on 9/13/14.
 */
public class ServerGoalInitiationService implements GoalInitiationService {

    final private Logger LOG = LoggerFactory.getLogger(ServerGoalInitiationService.class);

    final private GoalInitiationRepository initiationRepository;
    final private ServerNotificationService notificationService;
    final private SystemNotificationService systemNotificationService;

    public ServerGoalInitiationService(
        GoalInitiationRepository initiationRepository,
        ServerNotificationService notificationService,
        SystemNotificationService systemNotificationService) {
        this.initiationRepository = initiationRepository;
        this.notificationService = notificationService;
        this.systemNotificationService = systemNotificationService;
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
        notificationService.send(GoalInitiationCreatedEvent.create(initiation));
        // Step 4. Freezing amount for a player
        LOG.debug("Freezing amount for a player {}", initiation.getPlayer());
        Money amount = initiation.getConfiguration().getBet().getAmount();
        SystemEvent freezeRequest = SystemPaymentFreezeRequestEvent.create(initiation.getGoalKey(), initiation.getPlayer(), amount);
        systemNotificationService.send(freezeRequest);
        // Step 5. Scheduling Cancel task
        SystemEvent expirationTask = new SystemGoalInitiationDueEvent(initiation.getGoalKey());
        systemNotificationService.send(new SystemAddJobScheduleEvent(initiation.getGoalKey(), "initiation", expirationTask, initiation.getStartDate()));
    }

    @Override
    public GoalInitiation confirm(String key) {
        throw new UnsupportedOperationException();
    }

    public GoalInitiation confirm(String player, String key) {
        // Step 1. Fetching goal and checking everything is fine
        GoalInitiation initiation = initiationRepository.findOne(key);
        if(initiation == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.GoalNotExists);
        if(!initiation.getPlayer().equals(player))
            throw ClembleCasinoException.fromError(ClembleCasinoError.GoalNotOwnedByPlayer);
        // Step 2. Sending notification to start service in the same way as usual flow
        // Step 2.1. Removing scheduled notification
        systemNotificationService.send(new SystemRemoveJobScheduleEvent(initiation.getGoalKey(), "initiation"));
        // Step 2.2. Checking initiation is due
        systemNotificationService.send(new SystemGoalInitiationDueEvent(initiation.getGoalKey()));
        // Step 3. Returning original flow
        return initiation;
    }

    @Override
    public Collection<GoalInitiation> getPending() {
        throw new UnsupportedOperationException();
    }

    public Collection<GoalInitiation> getPending(String player) {
        return initiationRepository.findByPlayerAndState(player, InitiationState.pending);
    }

    @Override
    public GoalInitiation get(String key) {
        return initiationRepository.findOne(key);
    }

}
