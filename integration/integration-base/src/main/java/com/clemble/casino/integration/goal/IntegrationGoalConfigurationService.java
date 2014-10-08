package com.clemble.casino.integration.goal;

import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.configuration.controller.GoalConfigurationServiceController;
import com.clemble.casino.goal.lifecycle.configuration.service.GoalConfigurationService;

import java.util.List;

/**
 * Created by mavarazy on 9/15/14.
 */
public class IntegrationGoalConfigurationService implements GoalConfigurationService {

    final private String player;
    final private GoalConfigurationServiceController configurationService;

    public IntegrationGoalConfigurationService(String player, GoalConfigurationServiceController configurationService) {
        this.player = player;
        this.configurationService = configurationService;
    }

    @Override
    public List<GoalConfiguration> getConfigurations() {
        return configurationService.getConfigurations();
    }
}
