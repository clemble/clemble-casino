package com.clemble.casino.goal.configuration.controller;

import com.clemble.casino.bet.Bid;
import com.clemble.casino.goal.lifecycle.configuration.*;
import com.clemble.casino.goal.lifecycle.configuration.rule.reminder.BasicReminderRule;
import com.clemble.casino.goal.lifecycle.configuration.rule.reminder.NoReminderRule;
import com.clemble.casino.goal.lifecycle.configuration.rule.reminder.ReminderRule;
import com.clemble.casino.goal.lifecycle.configuration.rule.share.ShareRule;
import com.clemble.casino.goal.lifecycle.configuration.service.GoalConfigurationService;
import com.clemble.casino.lifecycle.configuration.rule.breach.PenaltyBreachPunishment;
import com.clemble.casino.lifecycle.configuration.rule.timeout.EODTimeoutCalculator;
import com.clemble.casino.lifecycle.configuration.rule.timeout.TimeoutRule;
import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.casino.lifecycle.configuration.rule.breach.LooseBreachPunishment;
import com.clemble.casino.lifecycle.configuration.rule.privacy.PrivacyRule;
import com.google.common.collect.ImmutableList;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.clemble.casino.goal.GoalWebMapping.*;
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
            new TimeoutRule(new PenaltyBreachPunishment(Money.create(Currency.FakeMoney, 10)), new EODTimeoutCalculator(1)),
            new TimeoutRule(LooseBreachPunishment.getInstance(), new EODTimeoutCalculator(7)),
            PrivacyRule.me,
            new GoalRoleConfiguration(
                new Bid(Money.create(Currency.FakeMoney, 50), Money.create(Currency.FakeMoney, 70)),
                3,
                NoReminderRule.INSTANCE,
                NoReminderRule.INSTANCE
            ),
            ShareRule.none
        ),
        new GoalConfiguration(
            "2weeks",
            "2 Weeks",
            new Bid(Money.create(Currency.FakeMoney, 300), Money.create(Currency.FakeMoney, 800)),
            new BasicReminderRule(TimeUnit.HOURS.toMillis(4)),
            new BasicReminderRule(TimeUnit.HOURS.toMillis(2)),
            new TimeoutRule(new PenaltyBreachPunishment(Money.create(Currency.FakeMoney, 10)), new EODTimeoutCalculator(1)),
            new TimeoutRule(LooseBreachPunishment.getInstance(), new EODTimeoutCalculator(14)),
            PrivacyRule.friends,
            new GoalRoleConfiguration(
                new Bid(Money.create(Currency.FakeMoney, 50), Money.create(Currency.FakeMoney, 90)),
                7,
                NoReminderRule.INSTANCE,
                NoReminderRule.INSTANCE
            ),
            ShareRule.none
        ),
        new GoalConfiguration(
            "month",
            "Month",
            new Bid(Money.create(Currency.FakeMoney, 300), Money.create(Currency.FakeMoney, 1300)),
            new BasicReminderRule(TimeUnit.HOURS.toMillis(4)),
            new BasicReminderRule(TimeUnit.HOURS.toMillis(2)),
            new TimeoutRule(new PenaltyBreachPunishment(Money.create(Currency.FakeMoney, 10)), new EODTimeoutCalculator(1)),
            new TimeoutRule(LooseBreachPunishment.getInstance(), new EODTimeoutCalculator(30)),
            PrivacyRule.world,
            new GoalRoleConfiguration(new Bid(Money.create(Currency.FakeMoney, 50), Money.create(Currency.FakeMoney, 130)), 15, NoReminderRule.INSTANCE, NoReminderRule.INSTANCE),
            ShareRule.twitter
        )
    );

    final private GoalConfigurationChoices choices = new GoalConfigurationChoices(
        ImmutableList.of(Money.create(Currency.FakeMoney, 200), Money.create(Currency.FakeMoney, 300), Money.create(Currency.FakeMoney, 400)),
        ImmutableList.of(
            new GoalConfigurationValue<TimeoutRule>(new TimeoutRule(LooseBreachPunishment.getInstance(), new EODTimeoutCalculator(7)), 30),
            new GoalConfigurationValue<TimeoutRule>(new TimeoutRule(LooseBreachPunishment.getInstance(), new EODTimeoutCalculator(14)), 70),
            new GoalConfigurationValue<TimeoutRule>(new TimeoutRule(LooseBreachPunishment.getInstance(), new EODTimeoutCalculator(21)), 130)
        ),
        ImmutableList.of(
            new GoalConfigurationValue<TimeoutRule>(new TimeoutRule(new PenaltyBreachPunishment(Money.create(Currency.FakeMoney, 20)), new EODTimeoutCalculator(1)), 0)
        ),
        ImmutableList.of(
            new GoalConfigurationValue<ReminderRule>(new BasicReminderRule(TimeUnit.HOURS.toMillis(4)), 0)
        ),
        ImmutableList.of(
            new GoalConfigurationValue<ReminderRule>(new BasicReminderRule(TimeUnit.HOURS.toMillis(2)), 0)
        ),
        ImmutableList.of(
            new GoalConfigurationValue<PrivacyRule>(PrivacyRule.me, 0),
            new GoalConfigurationValue<PrivacyRule>(PrivacyRule.friends, 30)
        ),
        ImmutableList.of(
            new GoalConfigurationValue<GoalRoleConfiguration>(new GoalRoleConfiguration(new Bid(Money.create(Currency.FakeMoney, 50), Money.create(Currency.FakeMoney, 130)), 3, NoReminderRule.INSTANCE, NoReminderRule.INSTANCE), 10)
        ),
        ImmutableList.of(
            new GoalConfigurationValue<ShareRule>(ShareRule.none, 0),
            new GoalConfigurationValue<ShareRule>(ShareRule.twitter, 50)
        )
    );

    @RequestMapping(method = RequestMethod.GET, value = MY_CONFIGURATIONS_CHOICES, produces = PRODUCES)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public GoalConfigurationChoices getChoices() {
        return choices;
    }

    @RequestMapping(method = RequestMethod.GET, value = MY_CONFIGURATIONS, produces = PRODUCES)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public List<GoalConfiguration> getConfigurations() {
        return DEFAULT_CONFIGURATIONS;
    }

}
