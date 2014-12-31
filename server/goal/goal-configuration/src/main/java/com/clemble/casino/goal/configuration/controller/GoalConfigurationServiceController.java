package com.clemble.casino.goal.configuration.controller;

import com.clemble.casino.bet.Bid;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.configuration.GoalRoleConfiguration;
import com.clemble.casino.goal.lifecycle.configuration.rule.reminder.BasicReminderRule;
import com.clemble.casino.goal.lifecycle.configuration.rule.reminder.NoReminderRule;
import com.clemble.casino.goal.lifecycle.configuration.service.GoalConfigurationService;
import com.clemble.casino.goal.lifecycle.management.GoalRole;
import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.casino.lifecycle.configuration.rule.breach.LooseBreachPunishment;
import com.clemble.casino.lifecycle.configuration.rule.privacy.PrivacyRule;
import com.clemble.casino.lifecycle.configuration.rule.time.MoveTimeRule;
import com.clemble.casino.lifecycle.configuration.rule.time.TotalTimeRule;
import com.google.common.collect.ImmutableList;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.clemble.casino.goal.GoalWebMapping.MY_CONFIGURATIONS;
import static com.clemble.casino.WebMapping.PRODUCES;

/**
 * Created by mavarazy on 9/1/14.
 */
@RestController
public class GoalConfigurationServiceController implements GoalConfigurationService {

    // TODO replace with SMART configurations
    final private static List<GoalConfiguration> DEFAULT_CONFIGURATIONS = ImmutableList.of(
        new GoalConfiguration(
            "week",
            "Week",
            new Bid(Money.create(Currency.FakeMoney, 300), Money.create(Currency.FakeMoney, 500)),
            new BasicReminderRule(TimeUnit.HOURS.toMillis(4)),
            new BasicReminderRule(TimeUnit.HOURS.toMillis(2)),
            new MoveTimeRule(TimeUnit.DAYS.toMillis(1), LooseBreachPunishment.getInstance()),
            new TotalTimeRule(TimeUnit.DAYS.toMillis(7), LooseBreachPunishment.getInstance()),
            PrivacyRule.me,
            new GoalRoleConfiguration(
                GoalRole.supporter,
                new Bid(Money.create(Currency.FakeMoney, 100), Money.create(Currency.FakeMoney, 150)),
                new BasicReminderRule(TimeUnit.HOURS.toMillis(3)),
                NoReminderRule.INSTANCE
            ),
            new GoalRoleConfiguration(
                GoalRole.observer,
                new Bid(Money.create(Currency.FakeMoney, 50), Money.create(Currency.FakeMoney, 70)),
                NoReminderRule.INSTANCE,
                NoReminderRule.INSTANCE
            )
        ),
        new GoalConfiguration(
            "2weeks",
            "2 Weeks",
            new Bid(Money.create(Currency.FakeMoney, 300), Money.create(Currency.FakeMoney, 800)),
            new BasicReminderRule(TimeUnit.HOURS.toMillis(4)),
            new BasicReminderRule(TimeUnit.HOURS.toMillis(2)),
            new MoveTimeRule(TimeUnit.DAYS.toMillis(1), LooseBreachPunishment.getInstance()),
            new TotalTimeRule(TimeUnit.DAYS.toMillis(14), LooseBreachPunishment.getInstance()),
            PrivacyRule.friends,
            new GoalRoleConfiguration(
                GoalRole.supporter,
                new Bid(Money.create(Currency.FakeMoney, 100), Money.create(Currency.FakeMoney, 210)),
                new BasicReminderRule(TimeUnit.HOURS.toMillis(3)),
                NoReminderRule.INSTANCE
            ),
            new GoalRoleConfiguration(
                GoalRole.observer,
                new Bid(Money.create(Currency.FakeMoney, 50), Money.create(Currency.FakeMoney, 90)),
                NoReminderRule.INSTANCE,
                NoReminderRule.INSTANCE
            )
        ),
        new GoalConfiguration(
            "month",
            "Month",
            new Bid(Money.create(Currency.FakeMoney, 300), Money.create(Currency.FakeMoney, 1300)),
            new BasicReminderRule(TimeUnit.HOURS.toMillis(4)),
            new BasicReminderRule(TimeUnit.HOURS.toMillis(2)),
            new MoveTimeRule(TimeUnit.DAYS.toMillis(1), LooseBreachPunishment.getInstance()),
            new TotalTimeRule(TimeUnit.DAYS.toMillis(30), LooseBreachPunishment.getInstance()),
            PrivacyRule.world,
            new GoalRoleConfiguration(
                GoalRole.supporter,
                new Bid(Money.create(Currency.FakeMoney, 100), Money.create(Currency.FakeMoney, 320)),
                new BasicReminderRule(TimeUnit.HOURS.toMillis(3)),
                NoReminderRule.INSTANCE
            ),
            new GoalRoleConfiguration(
                GoalRole.observer,
                new Bid(Money.create(Currency.FakeMoney, 50), Money.create(Currency.FakeMoney, 130)),
                NoReminderRule.INSTANCE,
                NoReminderRule.INSTANCE
            )
        )
    );

    @RequestMapping(method = RequestMethod.GET, value = MY_CONFIGURATIONS, produces = PRODUCES)
    @ResponseStatus(HttpStatus.OK)
    public List<GoalConfiguration> getConfigurations() {
        return DEFAULT_CONFIGURATIONS;
    }

}
