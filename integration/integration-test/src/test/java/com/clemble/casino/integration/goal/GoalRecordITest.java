package com.clemble.casino.integration.goal;

import com.clemble.casino.bet.Bid;
import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.configuration.rule.judge.JudgeRule;
import com.clemble.casino.goal.lifecycle.configuration.rule.judge.JudgeType;
import com.clemble.casino.goal.lifecycle.configuration.rule.parts.GoalPartsRule;
import com.clemble.casino.goal.lifecycle.configuration.rule.start.GoalStartRule;
import com.clemble.casino.goal.lifecycle.construction.GoalConstruction;
import com.clemble.casino.goal.lifecycle.construction.GoalConstructionRequest;
import com.clemble.casino.goal.lifecycle.initiation.GoalInitiation;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.clemble.casino.lifecycle.configuration.rule.bet.LimitedBetRule;
import com.clemble.casino.lifecycle.configuration.rule.breach.LooseBreachPunishment;
import com.clemble.casino.lifecycle.configuration.rule.privacy.PrivacyRule;
import com.clemble.casino.lifecycle.configuration.rule.time.MoveTimeRule;
import com.clemble.casino.lifecycle.configuration.rule.time.TotalTimeRule;
import com.clemble.casino.lifecycle.initiation.InitiationState;
import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.test.concurrent.AsyncCompletionUtils;
import com.clemble.test.concurrent.Check;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.concurrent.TimeUnit;

/**
 * Created by mavarazy on 17/10/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { IntegrationTestSpringConfiguration.class })
public class GoalRecordITest {

    GoalConfiguration CONFIGURATION = new GoalConfiguration(
        "basic",
        new Bid(Money.create(Currency.FakeMoney, 50), Money.create(Currency.FakeMoney, 5)),
        LimitedBetRule.create(5, 50),
        new JudgeRule("me", JudgeType.self),
        new GoalPartsRule(1),
        new MoveTimeRule(TimeUnit.SECONDS.toMillis(1), LooseBreachPunishment.getInstance()),
        new TotalTimeRule(TimeUnit.SECONDS.toMillis(2), LooseBreachPunishment.getInstance()),
        PrivacyRule.players,
        new GoalStartRule(1)
    );

    @Autowired
    public PlayerScenarios playerScenarios;

    @Test
    public void testSimpleRecord() {
        // Step 1. Creating player
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        // Step 2. Creating GoalRequest
        GoalConstructionRequest goalRequest = new GoalConstructionRequest(CONFIGURATION, "Simple test");
        final GoalConstruction construction = A.goalOperations().constructionService().construct(goalRequest);
        // Step 3. Checking construction
        AsyncCompletionUtils.check(new Check(){
            @Override
            public boolean check() {
                return A.goalOperations().initiationService().get(construction.getGoalKey()).getState() == InitiationState.initiated;
            }
        }, 30_000);
        // Step 4. Checking value
    }

}
