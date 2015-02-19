package com.clemble.casino.integration.goal;

import com.clemble.casino.bet.Bet;
import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.event.EventSelector;
import com.clemble.casino.client.event.EventSelectors;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.client.event.PlayerEventSelector;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.configuration.GoalRoleConfiguration;
import com.clemble.casino.goal.lifecycle.configuration.rule.reminder.BasicReminderRule;
import com.clemble.casino.goal.lifecycle.configuration.rule.reminder.NoReminderRule;
import com.clemble.casino.goal.lifecycle.configuration.rule.share.ShareRule;
import com.clemble.casino.goal.lifecycle.construction.GoalConstruction;
import com.clemble.casino.goal.lifecycle.construction.GoalConstructionRequest;
import com.clemble.casino.integration.ClembleIntegrationTest;
import com.clemble.casino.integration.event.EventAccumulator;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.utils.CheckUtils;
import com.clemble.casino.lifecycle.configuration.rule.breach.LooseBreachPunishment;
import com.clemble.casino.lifecycle.configuration.rule.privacy.PrivacyRule;
import com.clemble.casino.lifecycle.configuration.rule.timeout.MoveTimeoutCalculator;
import com.clemble.casino.lifecycle.configuration.rule.timeout.TimeoutRule;
import com.clemble.casino.lifecycle.configuration.rule.timeout.TotalTimeoutCalculator;
import com.clemble.casino.lifecycle.management.event.action.bet.BidAction;
import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.casino.server.event.SystemEvent;
import com.clemble.casino.server.event.email.SystemEmailSendRequestEvent;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

/**
 * Created by mavarazy on 12/31/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ClembleIntegrationTest
public class GoalEmailNotificationTest {

    @Autowired
    public PlayerScenarios playerScenarios;

    @Autowired
    public EventAccumulator<SystemEvent> systemEventAccumulator;

    final private GoalConfiguration CONFIGURATION = new GoalConfiguration(
        "email:notification:test",
        "Email Notification Test",
        new Bet(Money.create(Currency.point, 100), Money.create(Currency.point, 50)),
        new BasicReminderRule(TimeUnit.SECONDS.toMillis(1)),
        NoReminderRule.INSTANCE,
        new TimeoutRule(LooseBreachPunishment.getInstance(), new MoveTimeoutCalculator(TimeUnit.SECONDS.toMillis(2))),
        new TimeoutRule(LooseBreachPunishment.getInstance(), new TotalTimeoutCalculator(TimeUnit.HOURS.toMillis(3))),
        PrivacyRule.world,
        new GoalRoleConfiguration(
            new Bet(Money.create(Currency.point, 100), Money.create(Currency.point, 50)),
            3,
            new BasicReminderRule(TimeUnit.SECONDS.toMillis(1)),
            NoReminderRule.INSTANCE
        ),
        ShareRule.EMPTY
    );

    @Test
    public void testInitialized() {
        Assert.assertNotNull(playerScenarios);
        Assert.assertNotNull(systemEventAccumulator);
    }

    @Test
    public void testNotification() {
        // Step 1. Create player
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        EventSelector emailSelector = EventSelectors.
            where(new PlayerEventSelector(A.getPlayer())).
            and(new EventTypeSelector(SystemEmailSendRequestEvent.class));
        // Step 2. Create construction
        GoalConstructionRequest requestA = new GoalConstructionRequest(CONFIGURATION, "Test email notification", "UTC");
        A.goalOperations().constructionService().construct(requestA);
        // Step 3. Checking timeout email notification received
        SystemEmailSendRequestEvent reminderNotification = (SystemEmailSendRequestEvent) systemEventAccumulator.waitFor(emailSelector);
        Assert.assertNotNull(reminderNotification);
        Assert.assertEquals(reminderNotification.getTemplate(), "goal_due");
    }

    @Test
    public void testSupporterNotification() {
        // Step 1. Create player
        final ClembleCasinoOperations A = playerScenarios.createPlayer();
        final EventSelector AEmailSelector = EventSelectors.
            where(new PlayerEventSelector(A.getPlayer())).
            and(new EventTypeSelector(SystemEmailSendRequestEvent.class));
        // Step 2. Create supporter
        final ClembleCasinoOperations B = playerScenarios.createPlayer();
        final EventSelector BEmailSelector = EventSelectors.
            where(new PlayerEventSelector(B.getPlayer())).
            and(new EventTypeSelector(SystemEmailSendRequestEvent.class));
        // Step 3. Creating requests
        final GoalConstructionRequest requestA = new GoalConstructionRequest(CONFIGURATION, "Test email notification", "UTC");
        final GoalConstruction constructionA = A.goalOperations().constructionService().construct(requestA);
        // Step 4. Checking Requests
        CheckUtils.check((i) ->
            A.goalOperations().initiationService().get(constructionA.getGoalKey()) != null &&
            A.goalOperations().actionService().getState(constructionA.getGoalKey()) != null
        );
        B.goalOperations().actionService().process(constructionA.getGoalKey(), new BidAction());
        // Step 5. Checking value
        SystemEmailSendRequestEvent reminderNotification = (SystemEmailSendRequestEvent) systemEventAccumulator.waitFor(BEmailSelector);
        Assert.assertNotNull(reminderNotification);
        Assert.assertEquals(reminderNotification.getTemplate(), "goal_due");
    }

//    @Test
//    public void testObserverNotification() {
//        // Step 1. Create player
//        final ClembleCasinoOperations A = playerScenarios.createPlayer();
//        final EventSelector AEmailSelector = EventSelectors.
//            where(new PlayerEventSelector(A.getPlayer())).
//            and(new EventTypeSelector(SystemEmailSendRequestEvent.class));
//        // Step 2. Create supporter
//        final ClembleCasinoOperations B = playerScenarios.createPlayer();
//        final EventSelector BEmailSelector = EventSelectors.
//            where(new PlayerEventSelector(B.getPlayer())).
//            and(new EventTypeSelector(SystemEmailSendRequestEvent.class));
//        // Step 3. Creating requests
//        final GoalConstructionRequest requestA = new GoalConstructionRequest(CONFIGURATION, "Test email notification", new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1)));
//        final GoalConstruction constructionA = A.goalOperations().constructionService().construct(requestA);
//        // Step 4. Checking Requests
//        CheckUtils.check((i) ->
//            A.goalOperations().initiationService().get(constructionA.getGoalKey()) != null
//        );
//        B.goalOperations().initiationService().bid(constructionA.getGoalKey(), GoalRole.observer);
//        // Step 5. Starting goal A
//        A.goalOperations().initiationService().confirm(constructionA.getGoalKey());
//        // Step 6. Checking value
//        SystemEmailSendRequestEvent reminderNotification = (SystemEmailSendRequestEvent) systemEventAccumulator.waitFor(BEmailSelector);
//        Assert.assertNotNull(reminderNotification);
//        Assert.assertEquals(reminderNotification.getTemplate(), "goal_due");
//    }

}
