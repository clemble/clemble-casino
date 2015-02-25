package com.clemble.casino.goal.configuration.spring;

import com.clemble.casino.bet.Bet;
import com.clemble.casino.goal.configuration.controller.GoalConfigurationServiceController;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfigurationChoices;
import com.clemble.casino.goal.lifecycle.configuration.GoalRoleConfiguration;
import com.clemble.casino.goal.lifecycle.configuration.IntervalGoalConfigurationBuilder;
import com.clemble.casino.goal.lifecycle.configuration.rule.GoalRuleValue;
import com.clemble.casino.goal.lifecycle.configuration.rule.IntervalGoalRule;
import com.clemble.casino.goal.lifecycle.configuration.rule.reminder.BasicReminderRule;
import com.clemble.casino.goal.lifecycle.configuration.rule.reminder.NoReminderRule;
import com.clemble.casino.goal.lifecycle.configuration.rule.reminder.ReminderRule;
import com.clemble.casino.goal.lifecycle.configuration.rule.share.ShareRule;
import com.clemble.casino.lifecycle.configuration.rule.bet.LimitedBetRule;
import com.clemble.casino.lifecycle.configuration.rule.breach.LooseBreachPunishment;
import com.clemble.casino.lifecycle.configuration.rule.breach.PenaltyBreachPunishment;
import com.clemble.casino.lifecycle.configuration.rule.timeout.EODTimeoutCalculator;
import com.clemble.casino.lifecycle.configuration.rule.timeout.MoveTimeoutCalculator;
import com.clemble.casino.lifecycle.configuration.rule.timeout.TimeoutRule;
import com.clemble.casino.lifecycle.configuration.rule.timeout.TotalTimeoutCalculator;
import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.social.SocialProvider;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by mavarazy on 9/15/14.
 */
@Configuration
@Import({ GoalConfigurationSpringConfiguration.Cloud.class, GoalConfigurationSpringConfiguration.DefaultAndTest.class})
public class GoalConfigurationSpringConfiguration implements SpringConfiguration {

    @Configuration
    @Profile({ SpringConfiguration.CLOUD })
    public static class Cloud implements SpringConfiguration {

        final private static List<GoalConfiguration> CLOUD_CONFIGURATIONS = ImmutableList.of(
            new GoalConfiguration(
                "week",
                "Week",
                new Bet(Money.create(Currency.point, 300), Money.create(Currency.point, 500)),
                new BasicReminderRule(TimeUnit.HOURS.toMillis(4)),
                new BasicReminderRule(TimeUnit.HOURS.toMillis(2)),
                new TimeoutRule(new PenaltyBreachPunishment(Money.create(Currency.point, 10)), new EODTimeoutCalculator(1)),
                new TimeoutRule(LooseBreachPunishment.getInstance(), new EODTimeoutCalculator(7)),
                new GoalRoleConfiguration(
                    3,
                    LimitedBetRule.create(50, 100),
                    50,
                    NoReminderRule.INSTANCE,
                    NoReminderRule.INSTANCE
                ),
                ShareRule.EMPTY
            )
        );

        final private GoalConfigurationChoices CLOUD_CHOICES = new GoalConfigurationChoices(
            ImmutableList.of(Money.create(Currency.point, 200), Money.create(Currency.point, 300), Money.create(Currency.point, 400)),
            ImmutableList.of(new GoalRuleValue<TimeoutRule>(new TimeoutRule(LooseBreachPunishment.getInstance(), new EODTimeoutCalculator(7)), 30)),
            ImmutableList.of(new GoalRuleValue<TimeoutRule>(new TimeoutRule(new PenaltyBreachPunishment(Money.create(Currency.point, 20)), new EODTimeoutCalculator(1)), 0)),
            ImmutableList.of(new GoalRuleValue<ReminderRule>(new BasicReminderRule(TimeUnit.HOURS.toMillis(4)), 0)),
            ImmutableList.of(new GoalRuleValue<ReminderRule>(new BasicReminderRule(TimeUnit.HOURS.toMillis(2)), 0)),
            ImmutableList.of(new GoalRuleValue<GoalRoleConfiguration>(new GoalRoleConfiguration(3, LimitedBetRule.create(50, 100), 50, NoReminderRule.INSTANCE, NoReminderRule.INSTANCE), 10)),
            ImmutableList.of(new GoalRuleValue<ShareRule>(ShareRule.EMPTY, 0))
        );

        final private IntervalGoalConfigurationBuilder CLOUD_INTERVAL_BUILDER = new IntervalGoalConfigurationBuilder(
            new GoalConfiguration(
                "week",
                "Week",
                new Bet(Money.create(Currency.point, 0), Money.create(Currency.point, 0)),
                new BasicReminderRule(TimeUnit.HOURS.toMillis(4)),
                new BasicReminderRule(TimeUnit.HOURS.toMillis(2)),
                new TimeoutRule(new PenaltyBreachPunishment(Money.create(Currency.point, 10)), new EODTimeoutCalculator(7)),
                new TimeoutRule(LooseBreachPunishment.getInstance(), new EODTimeoutCalculator(7)),
                new GoalRoleConfiguration(
                    3,
                    LimitedBetRule.create(50, 100),
                    50,
                    NoReminderRule.INSTANCE,
                    NoReminderRule.INSTANCE
                ),
                ShareRule.EMPTY
            ),
            100,
            50,
            30,
            ImmutableList.<IntervalGoalRule>of(
                new IntervalGoalRule(new TimeoutRule(new PenaltyBreachPunishment(Money.create(Currency.point, 10)), new EODTimeoutCalculator(2)), 100, 15),
                new IntervalGoalRule(new TimeoutRule(new PenaltyBreachPunishment(Money.create(Currency.point, 20)), new EODTimeoutCalculator(1)), 100, 20),
                new IntervalGoalRule(new ShareRule(ImmutableSet.of(SocialProvider.twitter)), 100, 15),
                new IntervalGoalRule(new ShareRule(ImmutableSet.of(SocialProvider.facebook)), 100, 20),
                new IntervalGoalRule(LimitedBetRule.create(100, 200), 100, 20),
                new IntervalGoalRule(LimitedBetRule.create(150, 250), 100, 20)
            )
        );

        @Bean
        public GoalConfigurationServiceController goalConfigurationServiceController() {
            return new GoalConfigurationServiceController(CLOUD_CONFIGURATIONS, CLOUD_CHOICES, CLOUD_INTERVAL_BUILDER);
        }

    }

    @Configuration
    @Profile({ SpringConfiguration.TEST, SpringConfiguration.DEFAULT, SpringConfiguration.INTEGRATION_TEST })
    public static class DefaultAndTest implements SpringConfiguration {
        final private static List<GoalConfiguration> DEFAULT_CONFIGURATIONS = ImmutableList.of(
            new GoalConfiguration(
                "week",
                "Week",
                new Bet(Money.create(Currency.point, 300), Money.create(Currency.point, 500)),
                new BasicReminderRule(TimeUnit.HOURS.toMillis(4)),
                new BasicReminderRule(TimeUnit.HOURS.toMillis(2)),
                new TimeoutRule(new PenaltyBreachPunishment(Money.create(Currency.point, 10)), new EODTimeoutCalculator(1)),
                new TimeoutRule(LooseBreachPunishment.getInstance(), new EODTimeoutCalculator(7)),
                new GoalRoleConfiguration(
                    3,
                    LimitedBetRule.create(50, 100),
                    50,
                    NoReminderRule.INSTANCE,
                    NoReminderRule.INSTANCE
                ),
                ShareRule.EMPTY
            )
        );

        final private GoalConfigurationChoices DEFAULT_CHOICES = new GoalConfigurationChoices(
            ImmutableList.of(Money.create(Currency.point, 200), Money.create(Currency.point, 300), Money.create(Currency.point, 400)),
            ImmutableList.of(new GoalRuleValue<TimeoutRule>(new TimeoutRule(LooseBreachPunishment.getInstance(), new EODTimeoutCalculator(7)), 30)),
            ImmutableList.of(new GoalRuleValue<TimeoutRule>(new TimeoutRule(new PenaltyBreachPunishment(Money.create(Currency.point, 20)), new EODTimeoutCalculator(1)), 0)),
            ImmutableList.of(new GoalRuleValue<ReminderRule>(new BasicReminderRule(TimeUnit.HOURS.toMillis(4)), 0)),
            ImmutableList.of(new GoalRuleValue<ReminderRule>(new BasicReminderRule(TimeUnit.HOURS.toMillis(2)), 0)),
            ImmutableList.of(new GoalRuleValue<GoalRoleConfiguration>(new GoalRoleConfiguration(3, LimitedBetRule.create(50, 100), 50, NoReminderRule.INSTANCE, NoReminderRule.INSTANCE), 10)),
            ImmutableList.of(new GoalRuleValue<ShareRule>(ShareRule.EMPTY, 0))
        );

        final private IntervalGoalConfigurationBuilder DEFAULT_INTERVAL_BUILDER = new IntervalGoalConfigurationBuilder(
            new GoalConfiguration(
                "week",
                "Week",
                new Bet(Money.create(Currency.point, 0), Money.create(Currency.point, 0)),
                new BasicReminderRule(TimeUnit.HOURS.toMillis(4)),
                new BasicReminderRule(TimeUnit.HOURS.toMillis(2)),
                new TimeoutRule(new PenaltyBreachPunishment(Money.create(Currency.point, 10)), new MoveTimeoutCalculator(TimeUnit.MINUTES.toMillis(5))),
                new TimeoutRule(LooseBreachPunishment.getInstance(), new TotalTimeoutCalculator(TimeUnit.MINUTES.toMillis(35))),
                new GoalRoleConfiguration(
                    3,
                    LimitedBetRule.create(50, 100),
                    50,
                    NoReminderRule.INSTANCE,
                    NoReminderRule.INSTANCE
                ),
                ShareRule.EMPTY
            ),
            100,
            50,
            30,
            ImmutableList.<IntervalGoalRule>of(
                new IntervalGoalRule(new TimeoutRule(new PenaltyBreachPunishment(Money.create(Currency.point, 10)), new MoveTimeoutCalculator(TimeUnit.MINUTES.toMillis(3))), 100, 15),
                new IntervalGoalRule(new TimeoutRule(new PenaltyBreachPunishment(Money.create(Currency.point, 20)), new MoveTimeoutCalculator(TimeUnit.MINUTES.toMillis(2))), 100, 20),
                new IntervalGoalRule(new ShareRule(ImmutableSet.of(SocialProvider.twitter)), 100, 15),
                new IntervalGoalRule(new ShareRule(ImmutableSet.of(SocialProvider.facebook)), 100, 20),
                new IntervalGoalRule(LimitedBetRule.create(100, 200), 100, 20),
                new IntervalGoalRule(LimitedBetRule.create(150, 250), 100, 20)
            )
        );

        @Bean
        public GoalConfigurationServiceController goalConfigurationServiceController() {
            return new GoalConfigurationServiceController(DEFAULT_CONFIGURATIONS, DEFAULT_CHOICES, DEFAULT_INTERVAL_BUILDER);
        }
    }

}
