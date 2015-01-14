package com.clemble.casino.integration.goal;

import com.clemble.casino.bet.Bid;
import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.configuration.GoalRoleConfiguration;
import com.clemble.casino.goal.lifecycle.configuration.rule.reminder.BasicReminderRule;
import com.clemble.casino.goal.lifecycle.configuration.rule.reminder.NoReminderRule;
import com.clemble.casino.goal.lifecycle.configuration.rule.share.ShareRule;
import com.clemble.casino.goal.lifecycle.construction.GoalConstruction;
import com.clemble.casino.goal.lifecycle.construction.GoalConstructionRequest;
import com.clemble.casino.goal.lifecycle.management.GoalRole;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.clemble.casino.integration.utils.CheckUtils;
import com.clemble.casino.lifecycle.configuration.rule.breach.CountdownBreachPunishment;
import com.clemble.casino.lifecycle.configuration.rule.breach.LooseBreachPunishment;
import com.clemble.casino.lifecycle.configuration.rule.breach.PenaltyBreachPunishment;
import com.clemble.casino.lifecycle.configuration.rule.privacy.PrivacyRule;
import com.clemble.casino.lifecycle.configuration.rule.timeout.MoveTimeoutCalculator;
import com.clemble.casino.lifecycle.configuration.rule.timeout.TimeoutRule;
import com.clemble.casino.lifecycle.configuration.rule.timeout.TotalTimeoutCalculator;
import com.clemble.casino.lifecycle.management.outcome.Outcome;
import com.clemble.casino.lifecycle.management.outcome.PlayerLostOutcome;
import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.casino.payment.Bank;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by mavarazy on 1/5/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { IntegrationTestSpringConfiguration.class })
public class GoalMoveTimeoutPunishmentProcessingTest {

    final private GoalConfiguration LOOSE_PUNISHMENT = new GoalConfiguration(
        "move:loose:punishment",
        "move:loose:punishment",
        new Bid(Money.create(Currency.FakeMoney, 100), Money.create(Currency.FakeMoney, 50)),
        new BasicReminderRule(TimeUnit.SECONDS.toMillis(1)),
        NoReminderRule.INSTANCE,
        new TimeoutRule(LooseBreachPunishment.getInstance(), new MoveTimeoutCalculator(TimeUnit.SECONDS.toMillis(1))),
        new TimeoutRule(LooseBreachPunishment.getInstance(), new TotalTimeoutCalculator(TimeUnit.HOURS.toMillis(3))),
        PrivacyRule.world,
        new GoalRoleConfiguration(
            new Bid(Money.create(Currency.FakeMoney, 100), Money.create(Currency.FakeMoney, 50)),
            3,
            new BasicReminderRule(TimeUnit.SECONDS.toMillis(1)),
            NoReminderRule.INSTANCE
        ),
        ShareRule.none
    );

    final private GoalConfiguration PENALTY_PUNISHMENT = new GoalConfiguration(
        "move:penalty:punishment",
        "move:penalty:punishment",
        new Bid(Money.create(Currency.FakeMoney, 100), Money.create(Currency.FakeMoney, 30)),
        new BasicReminderRule(TimeUnit.SECONDS.toMillis(1)),
        NoReminderRule.INSTANCE,
        new TimeoutRule(new PenaltyBreachPunishment(Money.create(Currency.FakeMoney, 10)), new MoveTimeoutCalculator(TimeUnit.SECONDS.toMillis(1))),
        new TimeoutRule(new PenaltyBreachPunishment(Money.create(Currency.FakeMoney, 10)), new TotalTimeoutCalculator(TimeUnit.HOURS.toMillis(3))),
        PrivacyRule.world,
        new GoalRoleConfiguration(
            new Bid(Money.create(Currency.FakeMoney, 100), Money.create(Currency.FakeMoney, 50)),
            3,
            new BasicReminderRule(TimeUnit.SECONDS.toMillis(1)),
            NoReminderRule.INSTANCE
        ),
        ShareRule.none
    );

    final private GoalConfiguration COUNTDOWN_PUNISHMENT = new GoalConfiguration(
        "move:countdown:punishment",
        "move:countdown:punishment",
        new Bid(Money.create(Currency.FakeMoney, 100), Money.create(Currency.FakeMoney, 50)),
        new BasicReminderRule(TimeUnit.SECONDS.toMillis(1)),
        NoReminderRule.INSTANCE,
        new TimeoutRule(new CountdownBreachPunishment(Money.create(Currency.FakeMoney, 10), 100), new MoveTimeoutCalculator(TimeUnit.SECONDS.toMillis(1))),
        new TimeoutRule(new CountdownBreachPunishment(Money.create(Currency.FakeMoney, 10), 100), new TotalTimeoutCalculator(TimeUnit.HOURS.toMillis(3))),
        PrivacyRule.world,
        new GoalRoleConfiguration(
            new Bid(Money.create(Currency.FakeMoney, 100), Money.create(Currency.FakeMoney, 50)),
            3,
            new BasicReminderRule(TimeUnit.SECONDS.toMillis(1)),
            NoReminderRule.INSTANCE
        ),
        ShareRule.none
    );


    @Autowired
    public PlayerScenarios playerScenarios;

    @Test
    public void testLoose() {
        // Step 1. Creating player A
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        // Step 2. Creating goal request
        GoalConstruction AC = A.goalOperations().constructionService().construct(new GoalConstructionRequest(LOOSE_PUNISHMENT, "Test loose timeout", DateTime.now().plusYears(1)));
        CheckUtils.checkNotNull((i) -> A.goalOperations().initiationService().get(AC.getGoalKey()));
        A.goalOperations().initiationService().confirm(AC.getGoalKey());
        // Step 3. Checking AC
        Outcome expected = new PlayerLostOutcome(A.getPlayer());
        boolean check = CheckUtils.check((i) -> expected.equals(A.goalOperations().recordService().get(AC.getGoalKey()).getOutcome()));
        Assert.assertTrue(check);
    }

    @Test
    public void testPenalty() {
        // Step 1. Creating player A
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        // Step 2. Creating goal request
        GoalConstruction AC = A.goalOperations().constructionService().construct(new GoalConstructionRequest(PENALTY_PUNISHMENT, "Test penalty timeout", DateTime.now().plusYears(1)));
        CheckUtils.checkNotNull((i) -> A.goalOperations().initiationService().get(AC.getGoalKey()));
        Bank originalBank = A.goalOperations().initiationService().get(AC.getGoalKey()).getBank();
        A.goalOperations().initiationService().confirm(AC.getGoalKey());
        // Step 3. Checking AC
        boolean check = CheckUtils.check((i) -> !originalBank.equals(A.goalOperations().actionService().getState(AC.getGoalKey()).getBank()));
        Assert.assertTrue(check);
        // Step 4. Checking loosing after money expires
        Outcome expected = new PlayerLostOutcome(A.getPlayer());
        boolean checkLoose = CheckUtils.check((i) -> expected.equals(A.goalOperations().recordService().get(AC.getGoalKey()).getOutcome()));
        Assert.assertTrue(checkLoose);
    }

}
