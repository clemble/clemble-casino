package com.clemble.casino.goal.configuration.controller;

import com.clemble.casino.goal.lifecycle.configuration.*;
import com.clemble.casino.goal.lifecycle.configuration.service.GoalConfigurationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.clemble.casino.goal.GoalWebMapping.*;
import static com.clemble.casino.WebMapping.PRODUCES;

/**
 * Created by mavarazy on 9/1/14.
 */
@RestController
public class GoalConfigurationServiceController implements GoalConfigurationService {

    // TODO replace with SMART configurations
    final private List<GoalConfiguration> configurations;
    final private GoalConfigurationChoices choises;
    final private IntervalGoalConfigurationBuilder intervalBuilder;

    public GoalConfigurationServiceController(
        List<GoalConfiguration> configurations,
        GoalConfigurationChoices choises,
        IntervalGoalConfigurationBuilder intervalBuilder
    ) {
        this.configurations = configurations;
        this.choises = choises;
        this.intervalBuilder = intervalBuilder;
    }

    @RequestMapping(method = RequestMethod.GET, value = MY_CONFIGURATIONS, produces = PRODUCES)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public List<GoalConfiguration> getConfigurations() {
        return configurations;
    }

    @RequestMapping(method = RequestMethod.GET, value = MY_CONFIGURATIONS_CHOICES, produces = PRODUCES)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public GoalConfigurationChoices getChoises() {
        return choises;
    }

    @RequestMapping(method = RequestMethod.GET, value = MY_CONFIGURATIONS_INTERVAL, produces = PRODUCES)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public IntervalGoalConfigurationBuilder getIntervalBuilder() {
        return intervalBuilder;
    }

}
