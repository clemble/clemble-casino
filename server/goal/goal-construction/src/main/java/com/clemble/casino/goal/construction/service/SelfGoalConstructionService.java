package com.clemble.casino.goal.construction.service;

import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.construction.service.GoalConstructionService;
import com.clemble.casino.lifecycle.construction.ConstructionState;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.goal.lifecycle.construction.GoalConstruction;
import com.clemble.casino.goal.lifecycle.construction.GoalConstructionRequest;
import com.clemble.casino.goal.construction.GoalKeyGenerator;
import com.clemble.casino.goal.construction.repository.GoalConstructionRepository;
import com.clemble.casino.money.Money;
import com.clemble.casino.payment.service.PlayerAccountService;
import com.clemble.casino.server.event.goal.SystemGoalInitiationStartedEvent;
import com.clemble.casino.server.player.notification.SystemNotificationService;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by mavarazy on 9/10/14.
 */
public class SelfGoalConstructionService implements GoalConstructionService {

    final private GoalKeyGenerator keyGenerator;
    final private SystemNotificationService notificationService;
    final private GoalConstructionRepository constructionRepository;
    final private PlayerAccountService accountService;

    public SelfGoalConstructionService(
        GoalKeyGenerator keyGenerator,
        SystemNotificationService notificationService,
        GoalConstructionRepository constructionRepository,
        PlayerAccountService accountService) {
        this.keyGenerator = keyGenerator;
        this.accountService = accountService;
        this.notificationService = notificationService;
        this.constructionRepository = constructionRepository;
    }

    @Override
    public GoalConstruction construct(GoalConstructionRequest request) {
        throw new UnsupportedOperationException();
    }

    public GoalConstruction construct(String player, GoalConstructionRequest request) {
        // TODO remove when done
        if (!(request.getConfiguration() instanceof GoalConfiguration))
            throw new IllegalArgumentException();
        // Step 1. Checking this is appropriate request for this service
        if (request.getGoal() == null || request.getGoal().isEmpty())
            throw ClembleCasinoException.fromError(ClembleCasinoError.GoalIsEmpty, player);
        // Step 1.1. Checking there is enough money to complete it
        Money price = request.getConfiguration().getBet().getAmount();
        if (!accountService.canAfford(Collections.singleton(player), price.getCurrency(), price.getAmount()).isEmpty()){
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameConstructionInsufficientMoney, player);
        }
        // Step 2. Creating new GoalConfiguration
        GoalConstruction construction = request.toConstruction(player, keyGenerator.generate(player));
        construction = construction.clone(ConstructionState.constructed);
        // Step 3. Saving game construction
        GoalConstruction savedConstruction =  constructionRepository.save(construction);
        // Step 4. Initiating saved session right away
        notificationService.send(new SystemGoalInitiationStartedEvent(savedConstruction.getGoalKey(), savedConstruction.toInitiation()));
        // Step 5. Returning saved construction
        return savedConstruction;
    }

    @Override
    public Collection<GoalConstruction> getPending(String player) {
        return null;
    }

}
