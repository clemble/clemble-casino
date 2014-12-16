package com.clemble.casino.goal.configuration.controller;

import com.clemble.casino.bet.Bid;
import com.clemble.casino.goal.lifecycle.configuration.ChallengeConfiguration;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.configuration.rule.reminder.EmailReminderRule;
import com.clemble.casino.goal.lifecycle.configuration.rule.reminder.PhoneReminderRule;
import com.clemble.casino.goal.lifecycle.configuration.service.GoalConfigurationService;
import com.clemble.casino.lifecycle.configuration.rule.bet.*;
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
            "Solo",
            new Bid(Money.create(Currency.FakeMoney, 100), Money.create(Currency.FakeMoney, 50)),
            ForbiddenBetRule.INSTANCE,
            new MoveTimeRule(TimeUnit.DAYS.toMillis(1), LooseBreachPunishment.getInstance()),
            new TotalTimeRule(TimeUnit.DAYS.toMillis(7), LooseBreachPunishment.getInstance()),
            PrivacyRule.me,
            new EmailReminderRule(TimeUnit.HOURS.toMillis(4)),
            new PhoneReminderRule(TimeUnit.HOURS.toMillis(2)),
            ImmutableList.of(new ChallengeConfiguration(
                new Bid(Money.create(Currency.FakeMoney, 100), Money.create(Currency.FakeMoney, 20)),
                MonoBidRule.create(new Bid(Money.create(Currency.FakeMoney, 50), Money.create(Currency.FakeMoney, 5))),
                new MoveTimeRule(TimeUnit.DAYS.toMillis(1), LooseBreachPunishment.getInstance()),
                new TotalTimeRule(TimeUnit.DAYS.toMillis(7), LooseBreachPunishment.getInstance())
                )
            )
        ),
        new GoalConfiguration(
            "Friends",
            new Bid(Money.create(Currency.FakeMoney, 150), Money.create(Currency.FakeMoney, 75)),
            MonoBidRule.create(new Bid(Money.create(Currency.FakeMoney, 50), Money.create(Currency.FakeMoney, 5))),
            new MoveTimeRule(TimeUnit.DAYS.toMillis(1), LooseBreachPunishment.getInstance()),
            new TotalTimeRule(TimeUnit.DAYS.toMillis(7), LooseBreachPunishment.getInstance()),
            PrivacyRule.friends,
            new EmailReminderRule(TimeUnit.HOURS.toMillis(4)),
            new PhoneReminderRule(TimeUnit.HOURS.toMillis(2)),
            ImmutableList.of(new ChallengeConfiguration(
                new Bid(Money.create(Currency.FakeMoney, 100), Money.create(Currency.FakeMoney, 20)),
                MonoBidRule.create(new Bid(Money.create(Currency.FakeMoney, 50), Money.create(Currency.FakeMoney, 5))),
                new MoveTimeRule(TimeUnit.DAYS.toMillis(1), LooseBreachPunishment.getInstance()),
                new TotalTimeRule(TimeUnit.DAYS.toMillis(7), LooseBreachPunishment.getInstance())
                )
            )
        ),
        new GoalConfiguration(
            "World",
            new Bid(Money.create(Currency.FakeMoney, 200), Money.create(Currency.FakeMoney, 150)),
            MonoBidRule.create(new Bid(Money.create(Currency.FakeMoney, 75), Money.create(Currency.FakeMoney, 15))),
            new MoveTimeRule(TimeUnit.DAYS.toMillis(1), LooseBreachPunishment.getInstance()),
            new TotalTimeRule(TimeUnit.DAYS.toMillis(7), LooseBreachPunishment.getInstance()),
            PrivacyRule.world,
            new EmailReminderRule(TimeUnit.HOURS.toMillis(4)),
            new PhoneReminderRule(TimeUnit.HOURS.toMillis(2)),
            ImmutableList.of(new ChallengeConfiguration(
                new Bid(Money.create(Currency.FakeMoney, 120), Money.create(Currency.FakeMoney, 30)),
                MonoBidRule.create(new Bid(Money.create(Currency.FakeMoney, 75), Money.create(Currency.FakeMoney, 15))),
                new MoveTimeRule(TimeUnit.DAYS.toMillis(1), LooseBreachPunishment.getInstance()),
                new TotalTimeRule(TimeUnit.DAYS.toMillis(7), LooseBreachPunishment.getInstance())
            )
        )
    ));

    @RequestMapping(method = RequestMethod.GET, value = MY_CONFIGURATIONS, produces = PRODUCES)
    @ResponseStatus(HttpStatus.OK)
    public List<GoalConfiguration> getConfigurations() {
        return DEFAULT_CONFIGURATIONS;
    }

}
