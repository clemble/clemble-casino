package com.clemble.casino.integration.goal;

import com.clemble.casino.bet.Bet;
import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.goal.event.action.GoalReachedAction;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.configuration.GoalRoleConfiguration;
import com.clemble.casino.goal.lifecycle.configuration.rule.reminder.BasicReminderRule;
import com.clemble.casino.goal.lifecycle.configuration.rule.reminder.NoReminderRule;
import com.clemble.casino.goal.lifecycle.configuration.rule.share.ShareRule;
import com.clemble.casino.goal.lifecycle.construction.GoalConstruction;
import com.clemble.casino.goal.lifecycle.construction.GoalConstructionRequest;
import com.clemble.casino.goal.lifecycle.management.event.GoalEndedEvent;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.clemble.casino.lifecycle.configuration.rule.breach.LooseBreachPunishment;
import com.clemble.casino.lifecycle.configuration.rule.privacy.PrivacyRule;
import com.clemble.casino.lifecycle.configuration.rule.timeout.MoveTimeoutCalculator;
import com.clemble.casino.lifecycle.configuration.rule.timeout.TimeoutRule;
import com.clemble.casino.lifecycle.configuration.rule.timeout.TotalTimeoutCalculator;
import com.clemble.casino.lifecycle.initiation.InitiationState;
import com.clemble.casino.lifecycle.management.outcome.PlayerWonOutcome;
import com.clemble.casino.lifecycle.record.EventRecord;
import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.test.concurrent.AsyncCompletionUtils;
import com.clemble.test.concurrent.Check;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Collection;
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
        "Basic",
        new Bet(Money.create(Currency.FakeMoney, 500), Money.create(Currency.FakeMoney, 50)),
        new BasicReminderRule(TimeUnit.HOURS.toMillis(4)),
        new BasicReminderRule(TimeUnit.HOURS.toMillis(2)),
        new TimeoutRule(LooseBreachPunishment.getInstance(), new MoveTimeoutCalculator(TimeUnit.SECONDS.toMillis(1))),
        new TimeoutRule(LooseBreachPunishment.getInstance(), new TotalTimeoutCalculator(TimeUnit.SECONDS.toMillis(2))),
        PrivacyRule.me,
        new GoalRoleConfiguration(new Bet(Money.create(Currency.FakeMoney, 500), Money.create(Currency.FakeMoney, 50)), 3, NoReminderRule.INSTANCE, NoReminderRule.INSTANCE),
        ShareRule.none
    );

    @Autowired
    public PlayerScenarios playerScenarios;

    @Test
    public void testSimpleRecord() {
        // Step 1. Creating player
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        // Step 2. Creating GoalRequest
        GoalConstructionRequest goalRequest = new GoalConstructionRequest(CONFIGURATION, "Simple test", DateTimeZone.UTC);
        final GoalConstruction construction = A.goalOperations().constructionService().construct(goalRequest);
        final String goalKey = construction.getGoalKey();
        // Step 3. Checking construction
        AsyncCompletionUtils.check(new Check(){
            @Override
            public boolean check() {
                return A.goalOperations().initiationService().get(goalKey).getState() == InitiationState.initiated;
            }
        }, 30_000);
        // Step 4. Checking value
        AsyncCompletionUtils.check(new Check(){
            @Override
            public boolean check() {
                Collection<EventRecord> events = A.goalOperations().recordService().get(goalKey).getEventRecords();
                for(EventRecord event: events) {
                    if (event.getEvent() instanceof GoalEndedEvent)
                        return true;
                }
                return false;
            }
        }, 30_000);
    }

    @Test
    public void testWining() {
        // Step 1. Creating player
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        // Step 2. Creating GoalRequest
        GoalConstructionRequest goalRequest = new GoalConstructionRequest(CONFIGURATION, "Simple test", DateTimeZone.UTC);
        final GoalConstruction construction = A.goalOperations().constructionService().construct(goalRequest);
        final String goalKey = construction.getGoalKey();
        // Step 3. Checking construction
        AsyncCompletionUtils.check(new Check(){
            @Override
            public boolean check() {
                return A.goalOperations().initiationService().get(goalKey).getState() == InitiationState.initiated;
            }
        }, 30_000);
        // Step 4. Checking goal started
        AsyncCompletionUtils.check(new Check(){
            @Override
            public boolean check() {
                return A.goalOperations().actionService().getState(goalKey) != null;
            }
        }, 30_000);
        // Step 5. Performing simple action
        A.goalOperations().actionService().process(goalKey, new GoalReachedAction("Win bitch"));
        // Step 6. Waiting for goal to completes
        AsyncCompletionUtils.check(new Check(){
            @Override
            public boolean check() {
                Collection<EventRecord> events = A.goalOperations().recordService().get(goalKey).getEventRecords();
                for(EventRecord event: events) {
                    if (event.getEvent() instanceof GoalEndedEvent)
                        return true;
                }
                return false;
            }
        }, 30_000);
        Assert.assertEquals(A.goalOperations().recordService().get(goalKey).getOutcome(), new PlayerWonOutcome(A.getPlayer()));
    }

}
