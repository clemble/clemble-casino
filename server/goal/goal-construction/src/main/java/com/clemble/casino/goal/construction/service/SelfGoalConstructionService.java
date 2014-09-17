package com.clemble.casino.goal.construction.service;

import com.clemble.casino.construction.Construction;
import com.clemble.casino.construction.ConstructionState;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.goal.configuration.GoalConfiguration;
import com.clemble.casino.goal.construction.GoalConstruction;
import com.clemble.casino.goal.construction.GoalConstructionRequest;
import com.clemble.casino.goal.construction.GoalKeyGenerator;
import com.clemble.casino.goal.construction.repository.GoalConstructionRepository;
import com.clemble.casino.goal.rule.judge.JudgeType;
import com.clemble.casino.money.Money;
import com.clemble.casino.payment.service.PlayerAccountServiceContract;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by mavarazy on 9/10/14.
 */
public class SelfGoalConstructionService implements GoalConstructionService {

    final private GoalKeyGenerator keyGenerator;
    final private ServerGoalInitiationService initiationService;
    final private GoalConstructionRepository constructionRepository;
    final private PlayerAccountServiceContract accountService;

    public SelfGoalConstructionService(
        GoalKeyGenerator keyGenerator,
        ServerGoalInitiationService initiationService,
        GoalConstructionRepository constructionRepository,
        PlayerAccountServiceContract accountService) {
        this.keyGenerator = keyGenerator;
        this.accountService = accountService;
        this.initiationService = initiationService;
        this.constructionRepository = constructionRepository;
    }

    @Override
    public GoalConstruction construct(GoalConstructionRequest request) {
        throw new UnsupportedOperationException();
    }


    public GoalConstruction construct(String player, GoalConstructionRequest request) {
        // Step 1. Checking this is appropriate request for this service
        if(request.getConfiguration().getJudgeRule().getType() != JudgeType.self)
            throw new IllegalArgumentException();
        // Step 1.1. Checking there is enough money to complete it
        Money price = request.getConfiguration().getBid().getAmount();
        if (!accountService.canAfford(Collections.singleton(player), price.getCurrency(), price.getAmount()).isEmpty()){
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameConstructionInsufficientMoney, player);
        }
        // Step 2. Creating new GoalConfiguration
        GoalConstruction construction = request.toConstruction(player, keyGenerator.generate(player));
        construction = construction.clone(ConstructionState.constructed);
        // Step 3. Saving game construction
        GoalConstruction savedConstruction =  constructionRepository.save(construction);
        // Step 4. Initiating saved session right away
        initiationService.start(savedConstruction.toInitiation());
        // Step 5. Returning saved construction
        return savedConstruction;
    }

    @Override
    public Collection<GoalConstruction> getPending(String player) {
        return null;
    }

}
