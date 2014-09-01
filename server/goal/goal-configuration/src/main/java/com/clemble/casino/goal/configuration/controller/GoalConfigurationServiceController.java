package com.clemble.casino.goal.configuration.controller;

import com.clemble.casino.goal.configuration.GoalConfigurations;
import com.clemble.casino.goal.service.GoalConfigurationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.clemble.casino.goal.GoalWebMapping.MY_CONFIGURATIONS;
import static com.clemble.casino.goal.GoalWebMapping.MY_GOALS_GOAL_STATUS;
import static com.clemble.casino.web.mapping.WebMapping.PRODUCES;

/**
 * Created by mavarazy on 9/1/14.
 */
@RestController
public class GoalConfigurationServiceController implements GoalConfigurationService {

    @RequestMapping(method = RequestMethod.GET, value = MY_CONFIGURATIONS, produces = PRODUCES)
    @ResponseStatus(HttpStatus.OK)
    public GoalConfigurations getConfigurations() {
        return null;
    }

}
