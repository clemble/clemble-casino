package com.clemble.casino.goal.configuration.controller;

import com.clemble.casino.bet.Bet;
import com.clemble.casino.goal.lifecycle.configuration.*;
import com.clemble.casino.goal.lifecycle.configuration.rule.GoalRuleValue;
import com.clemble.casino.goal.lifecycle.configuration.rule.IntervalGoalRule;
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
            new Bet(Money.create(Currency.FakeMoney, 300), Money.create(Currency.FakeMoney, 500)),
            new BasicReminderRule(TimeUnit.HOURS.toMillis(4)),
            new BasicReminderRule(TimeUnit.HOURS.toMillis(2)),
            new TimeoutRule(new PenaltyBreachPunishment(Money.create(Currency.FakeMoney, 10)), new EODTimeoutCalculator(1)),
            new TimeoutRule(LooseBreachPunishment.getInstance(), new EODTimeoutCalculator(7)),
            PrivacyRule.me,
            new GoalRoleConfiguration(
                new Bet(Money.create(Currency.FakeMoney, 50), Money.create(Currency.FakeMoney, 70)),
                3,
                NoReminderRule.INSTANCE,
                NoReminderRule.INSTANCE
            ),
            ShareRule.none
        ),
        new GoalConfiguration(
            "2weeks",
            "2 Weeks",
            new Bet(Money.create(Currency.FakeMoney, 300), Money.create(Currency.FakeMoney, 800)),
            new BasicReminderRule(TimeUnit.HOURS.toMillis(4)),
            new BasicReminderRule(TimeUnit.HOURS.toMillis(2)),
            new TimeoutRule(new PenaltyBreachPunishment(Money.create(Currency.FakeMoney, 10)), new EODTimeoutCalculator(1)),
            new TimeoutRule(LooseBreachPunishment.getInstance(), new EODTimeoutCalculator(14)),
            PrivacyRule.friends,
            new GoalRoleConfiguration(
                new Bet(Money.create(Currency.FakeMoney, 50), Money.create(Currency.FakeMoney, 90)),
                7,
                NoReminderRule.INSTANCE,
                NoReminderRule.INSTANCE
            ),
            ShareRule.none
        ),
        new GoalConfiguration(
            "month",
            "Month",
            new Bet(Money.create(Currency.FakeMoney, 300), Money.create(Currency.FakeMoney, 1300)),
            new BasicReminderRule(TimeUnit.HOURS.toMillis(4)),
            new BasicReminderRule(TimeUnit.HOURS.toMillis(2)),
            new TimeoutRule(new PenaltyBreachPunishment(Money.create(Currency.FakeMoney, 10)), new EODTimeoutCalculator(1)),
            new TimeoutRule(LooseBreachPunishment.getInstance(), new EODTimeoutCalculator(30)),
            PrivacyRule.world,
            new GoalRoleConfiguration(new Bet(Money.create(Currency.FakeMoney, 50), Money.create(Currency.FakeMoney, 130)), 15, NoReminderRule.INSTANCE, NoReminderRule.INSTANCE),
            ShareRule.twitter
        )
    );

    final private GoalConfigurationChoices DEFAULT_CHOICES = new GoalConfigurationChoices(
        ImmutableList.of(Money.create(Currency.FakeMoney, 200), Money.create(Currency.FakeMoney, 300), Money.create(Currency.FakeMoney, 400)),
        ImmutableList.of(
            new GoalRuleValue<TimeoutRule>(new TimeoutRule(LooseBreachPunishment.getInstance(), new EODTimeoutCalculator(7)), 30),
            new GoalRuleValue<TimeoutRule>(new TimeoutRule(LooseBreachPunishment.getInstance(), new EODTimeoutCalculator(14)), 70),
            new GoalRuleValue<TimeoutRule>(new TimeoutRule(LooseBreachPunishment.getInstance(), new EODTimeoutCalculator(21)), 130)
        ),
        ImmutableList.of(
            new GoalRuleValue<TimeoutRule>(new TimeoutRule(new PenaltyBreachPunishment(Money.create(Currency.FakeMoney, 20)), new EODTimeoutCalculator(1)), 0)
        ),
        ImmutableList.of(
            new GoalRuleValue<ReminderRule>(new BasicReminderRule(TimeUnit.HOURS.toMillis(4)), 0)
        ),
        ImmutableList.of(
            new GoalRuleValue<ReminderRule>(new BasicReminderRule(TimeUnit.HOURS.toMillis(2)), 0)
        ),
        ImmutableList.of(
            new GoalRuleValue<PrivacyRule>(PrivacyRule.me, 0),
            new GoalRuleValue<PrivacyRule>(PrivacyRule.friends, 30)
        ),
        ImmutableList.of(
            new GoalRuleValue<GoalRoleConfiguration>(new GoalRoleConfiguration(new Bet(Money.create(Currency.FakeMoney, 50), Money.create(Currency.FakeMoney, 130)), 3, NoReminderRule.INSTANCE, NoReminderRule.INSTANCE), 10)
        ),
        ImmutableList.of(
            new GoalRuleValue<ShareRule>(ShareRule.none, 0),
            new GoalRuleValue<ShareRule>(ShareRule.twitter, 50)
        )
    );

    final private IntervalGoalConfigurationBuilder DEFAULT_INTERVAL_BUILDER = new IntervalGoalConfigurationBuilder(
        new GoalConfiguration(
            "week",
            "Week",
            new Bet(Money.create(Currency.FakeMoney, 0), Money.create(Currency.FakeMoney, 0)),
            new BasicReminderRule(TimeUnit.HOURS.toMillis(4)),
            new BasicReminderRule(TimeUnit.HOURS.toMillis(2)),
            new TimeoutRule(new PenaltyBreachPunishment(Money.create(Currency.FakeMoney, 10)), new EODTimeoutCalculator(7)),
            new TimeoutRule(LooseBreachPunishment.getInstance(), new EODTimeoutCalculator(7)),
            PrivacyRule.me,
            new GoalRoleConfiguration(
                new Bet(Money.create(Currency.FakeMoney, 50), Money.create(Currency.FakeMoney, 70)),
                3,
                NoReminderRule.INSTANCE,
                NoReminderRule.INSTANCE
            ),
            ShareRule.none
        ),
        100,
        50,
        30,
        ImmutableList.<IntervalGoalRule>of(
            new IntervalGoalRule(PrivacyRule.friends, 50, 15),
            new IntervalGoalRule(PrivacyRule.world, 50, 15),
            new IntervalGoalRule(new TimeoutRule(new PenaltyBreachPunishment(Money.create(Currency.FakeMoney, 10)), new EODTimeoutCalculator(2)), 50, 15),
            new IntervalGoalRule(new TimeoutRule(new PenaltyBreachPunishment(Money.create(Currency.FakeMoney, 20)), new EODTimeoutCalculator(1)), 50, 15),
            new IntervalGoalRule(ShareRule.twitter, 50, 15),
            new IntervalGoalRule(ShareRule.facebook, 50, 15)
        )
    );

    @RequestMapping(method = RequestMethod.GET, value = MY_CONFIGURATIONS, produces = PRODUCES)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public List<GoalConfiguration> getConfigurations() {
        return DEFAULT_CONFIGURATIONS;
    }

    @RequestMapping(method = RequestMethod.GET, value = MY_CONFIGURATIONS_CHOICES, produces = PRODUCES)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public GoalConfigurationChoices getChoises() {
        return DEFAULT_CHOICES;
    }

    @RequestMapping(method = RequestMethod.GET, value = MY_CONFIGURATIONS_INTERVAL, produces = PRODUCES)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public IntervalGoalConfigurationBuilder getIntervalBuilder() {
        return DEFAULT_INTERVAL_BUILDER;
    }

}
