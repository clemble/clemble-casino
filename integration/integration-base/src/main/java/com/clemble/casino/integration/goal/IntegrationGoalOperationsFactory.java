package com.clemble.casino.integration.goal;

import com.clemble.casino.client.goal.GoalOperations;
import com.clemble.casino.goal.configuration.controller.GoalConfigurationServiceController;
import com.clemble.casino.goal.construction.controller.GoalConstructionServiceController;
import com.clemble.casino.goal.construction.controller.GoalInitiationServiceController;
import com.clemble.casino.goal.controller.GoalActionServiceController;
import com.clemble.casino.goal.controller.GoalRecordServiceController;
import com.clemble.casino.goal.controller.GoalTimelineServiceController;
import com.clemble.casino.goal.lifecycle.management.service.GoalActionService;

/**
 * Created by mavarazy on 9/15/14.
 */
public class IntegrationGoalOperationsFactory {

    final private GoalConfigurationServiceController configurationService;
    final private GoalInitiationServiceController initiationService;
    final private GoalConstructionServiceController constructionService;
    final private GoalTimelineServiceController timelineService;
    final private GoalActionServiceController actionService;
    final private GoalRecordServiceController recordService;

    public IntegrationGoalOperationsFactory(
        GoalConfigurationServiceController configurationService,
        GoalInitiationServiceController initiationService,
        GoalConstructionServiceController constructionService,
        GoalTimelineServiceController timelineService,
        GoalActionServiceController actionService,
        GoalRecordServiceController recordService) {
        this.configurationService = configurationService;
        this.initiationService = initiationService;
        this.constructionService = constructionService;
        this.actionService = actionService;
        this.recordService = recordService;
        this.timelineService = timelineService;
    }

    public GoalOperations construct(String player) {
        return new IntegrationGoalOperations(player,
            configurationService,
            initiationService,
            constructionService,
            actionService,
            timelineService,
            recordService);
    }

}
