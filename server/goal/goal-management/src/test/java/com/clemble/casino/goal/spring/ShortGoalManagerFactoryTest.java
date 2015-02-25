package com.clemble.casino.goal.spring;

import com.clemble.casino.bet.Bet;
import com.clemble.casino.goal.action.GoalManagerFactoryFacade;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.configuration.GoalRoleConfiguration;
import com.clemble.casino.goal.lifecycle.configuration.rule.reminder.BasicReminderRule;
import com.clemble.casino.goal.lifecycle.configuration.rule.reminder.NoReminderRule;
import com.clemble.casino.goal.lifecycle.configuration.rule.share.ShareRule;
import com.clemble.casino.goal.lifecycle.initiation.GoalInitiation;
import com.clemble.casino.goal.lifecycle.management.event.GoalEndedEvent;
import com.clemble.casino.goal.repository.GoalRecordRepository;
import com.clemble.casino.lifecycle.configuration.rule.bet.LimitedBetRule;
import com.clemble.casino.lifecycle.configuration.rule.breach.LooseBreachPunishment;
import com.clemble.casino.lifecycle.configuration.rule.timeout.MoveTimeoutCalculator;
import com.clemble.casino.lifecycle.configuration.rule.timeout.TimeoutRule;
import com.clemble.casino.lifecycle.configuration.rule.timeout.TotalTimeoutCalculator;
import com.clemble.casino.lifecycle.initiation.InitiationState;
import com.clemble.casino.lifecycle.record.EventRecord;
import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.casino.payment.Bank;
import com.clemble.test.concurrent.AsyncCompletionUtils;
import com.clemble.test.concurrent.Check;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by mavarazy on 16/10/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { GoalManagementSpringConfiguration.class })
public class ShortGoalManagerFactoryTest {

    @Autowired
    public GoalManagerFactoryFacade managerFactory;

    @Autowired
    public GoalRecordRepository recordRepository;

    final private GoalConfiguration configuration = new GoalConfiguration(
        "basic",
        "Basic",
        new Bet(Money.create(Currency.point, 500), Money.create(Currency.point, 50)),
        new BasicReminderRule(TimeUnit.HOURS.toMillis(4)),
        new BasicReminderRule(TimeUnit.HOURS.toMillis(2)),
        new TimeoutRule(LooseBreachPunishment.getInstance(), new MoveTimeoutCalculator(TimeUnit.SECONDS.toMillis(1))),
        new TimeoutRule(LooseBreachPunishment.getInstance(), new TotalTimeoutCalculator(TimeUnit.SECONDS.toMillis(3))),
        new GoalRoleConfiguration(3, LimitedBetRule.create(50, 100), 50, NoReminderRule.INSTANCE, NoReminderRule.INSTANCE),
        ShareRule.EMPTY
    );

    @Test
    public void testSimpleCreation() {
        // Step 1. Generating goal
        String goalKey = RandomStringUtils.randomAlphabetic(10);
        String player = RandomStringUtils.randomAlphabetic(10);
        GoalInitiation initiation = new GoalInitiation(
            goalKey,
            InitiationState.initiated,
            Bank.create(player, configuration.getBet()),
            player,
            "Create goal state",
            "UTC",
            "",
            configuration,
            new HashSet<>(),
            DateTime.now(DateTimeZone.UTC));
        // Step 2. Starting initiation
        managerFactory.start(null, initiation);
        // Step 3. Checking there is a state for the game
        Assert.assertNotEquals(managerFactory.get(goalKey), null);
    }

    @Test
    @Ignore // TODO move this test to integration - Schedule is external service currently
    public void testSimpleTimeout() throws InterruptedException {
        // Step 1. Generating goal
        final String goalKey = RandomStringUtils.randomAlphabetic(10);
        String player = RandomStringUtils.randomAlphabetic(10);
        GoalInitiation initiation = new GoalInitiation(
            goalKey,
            InitiationState.initiated,
            Bank.create(player, configuration.getBet()),
            player,
            "Create goal state",
            "UTC",
            "",
            configuration,
            new HashSet<>(),
            DateTime.now(DateTimeZone.UTC));
        // Step 2. Starting initiation
        managerFactory.start(null, initiation);
        // Step 3. Checking there is a state for the game
        Thread.sleep(2000);
        AsyncCompletionUtils.check(new Check() {
            @Override
            public boolean check() {
                Set<EventRecord> events = recordRepository.findOne(goalKey).getEventRecords();
                for (EventRecord record : events) {
                    if (record.getEvent() instanceof GoalEndedEvent) {
                        return true;
                    }
                }
                return false;
            }
        }, 30_000);
        AsyncCompletionUtils.check(new Check() {
            @Override
            public boolean check() {
                return managerFactory.get(goalKey) == null;
            }
        }, 1_000);
    }

}
