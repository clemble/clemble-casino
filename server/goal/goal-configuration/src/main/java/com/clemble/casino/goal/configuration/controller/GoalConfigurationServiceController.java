package com.clemble.casino.goal.configuration.controller;

import com.clemble.casino.bet.Bid;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.configuration.ShortGoalConfiguration;
import com.clemble.casino.goal.lifecycle.configuration.rule.due.GoalDueRule;
import com.clemble.casino.goal.lifecycle.configuration.rule.reminder.EmailReminderRule;
import com.clemble.casino.goal.lifecycle.configuration.rule.reminder.PhoneReminderRule;
import com.clemble.casino.goal.lifecycle.configuration.rule.start.GoalStartRule;
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
public class GoalConfigurationServiceController<T extends GoalConfiguration> implements GoalConfigurationService<T> {

    // TODO replace with SMART configurations
    final private static GoalConfiguration[] DEFAULT_CONFIGURATIONS = new GoalConfiguration[]{
        new ShortGoalConfiguration(
            "Solo",
            new Bid(Money.create(Currency.FakeMoney, 50), Money.create(Currency.FakeMoney, 5)),
            ForbiddenBetRule.INSTANCE,
            new MoveTimeRule(TimeUnit.DAYS.toMillis(1), LooseBreachPunishment.getInstance()),
            new TotalTimeRule(TimeUnit.DAYS.toMillis(7), LooseBreachPunishment.getInstance()),
            PrivacyRule.me,
            new GoalDueRule("facebook"),
            new EmailReminderRule(TimeUnit.HOURS.toMillis(4)),
            new PhoneReminderRule(TimeUnit.HOURS.toMillis(2)),
            new GoalStartRule(30000)
        ),
        new ShortGoalConfiguration(
            "Friends",
            new Bid(Money.create(Currency.FakeMoney, 100), Money.create(Currency.FakeMoney, 20)),
            MonoBidRule.create(new Bid(Money.create(Currency.FakeMoney, 50), Money.create(Currency.FakeMoney, 5))),
            new MoveTimeRule(TimeUnit.DAYS.toMillis(1), LooseBreachPunishment.getInstance()),
            new TotalTimeRule(TimeUnit.DAYS.toMillis(7), LooseBreachPunishment.getInstance()),
            PrivacyRule.friends,
            new GoalDueRule("vkontakte"),
            new EmailReminderRule(TimeUnit.HOURS.toMillis(4)),
            new PhoneReminderRule(TimeUnit.HOURS.toMillis(2)),
            new GoalStartRule(TimeUnit.DAYS.toMillis(1))
        ),
        new ShortGoalConfiguration(
            "World",
            new Bid(Money.create(Currency.FakeMoney, 120), Money.create(Currency.FakeMoney, 30)),
            MonoBidRule.create(new Bid(Money.create(Currency.FakeMoney, 75), Money.create(Currency.FakeMoney, 15))),
            new MoveTimeRule(TimeUnit.DAYS.toMillis(1), LooseBreachPunishment.getInstance()),
            new TotalTimeRule(TimeUnit.DAYS.toMillis(7), LooseBreachPunishment.getInstance()),
            PrivacyRule.world,
            new GoalDueRule("google"),
            new EmailReminderRule(TimeUnit.HOURS.toMillis(4)),
            new PhoneReminderRule(TimeUnit.HOURS.toMillis(2)),
            new GoalStartRule(TimeUnit.DAYS.toMillis(3))
        )
    };

    public List<T> getConfigurations() {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(method = RequestMethod.GET, value = MY_CONFIGURATIONS, produces = PRODUCES)
    @ResponseStatus(HttpStatus.OK)
    public GoalConfiguration[] getConfigurationsArray(){
        // This is done to work around ObjectMapper limits (It serializes type with array, but not with List of inherited type)
        return DEFAULT_CONFIGURATIONS;
    }

}
