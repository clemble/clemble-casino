package com.clemble.casino.goal.configuration.controller;

import com.clemble.casino.bet.Bid;
import com.clemble.casino.goal.configuration.GoalConfiguration;
import com.clemble.casino.goal.rule.judge.JudgeRule;
import com.clemble.casino.goal.configuration.service.GoalConfigurationService;
import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.casino.rule.bet.LimitedBetRule;
import com.clemble.casino.rule.breach.LooseBreachPunishment;
import com.clemble.casino.rule.privacy.PrivacyRule;
import com.clemble.casino.rule.time.MoveTimeRule;
import com.clemble.casino.rule.time.TotalTimeRule;
import com.google.common.collect.ImmutableList;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.clemble.casino.goal.GoalWebMapping.MY_CONFIGURATIONS;
import static com.clemble.casino.web.mapping.WebMapping.PRODUCES;

/**
 * Created by mavarazy on 9/1/14.
 */
@RestController
public class GoalConfigurationServiceController implements GoalConfigurationService {

    // TODO replace with SMART configurations
    final private static List<GoalConfiguration> DEFAULT_CONFIGURATIONS = ImmutableList.of(
        new GoalConfiguration(
            new Bid(Money.create(Currency.FakeMoney, 50), Money.create(Currency.FakeMoney, 5)),
            LimitedBetRule.create(5, 50),
            JudgeRule.self,
            new MoveTimeRule(TimeUnit.MILLISECONDS.toDays(1), LooseBreachPunishment.getInstance()),
            new TotalTimeRule(TimeUnit.MILLISECONDS.toDays(7), LooseBreachPunishment.getInstance()),
            PrivacyRule.everybody
        )
    );

    @RequestMapping(method = RequestMethod.GET, value = MY_CONFIGURATIONS, produces = PRODUCES)
    @ResponseStatus(HttpStatus.OK)
    public List<GoalConfiguration> getConfigurations() {
        return DEFAULT_CONFIGURATIONS;
    }

}
