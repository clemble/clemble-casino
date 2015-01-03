package com.clemble.casino.integration.goal;

import com.clemble.casino.client.goal.GoalOperations;
import com.clemble.casino.goal.configuration.controller.GoalConfigurationServiceController;
import com.clemble.casino.goal.construction.controller.FriendInitiationServiceController;
import com.clemble.casino.goal.construction.controller.GoalConstructionServiceController;
import com.clemble.casino.goal.construction.controller.GoalInitiationServiceController;
import com.clemble.casino.goal.controller.GoalActionServiceController;
import com.clemble.casino.goal.controller.GoalRecordServiceController;
import com.clemble.casino.goal.controller.FriendGoalServiceController;
import com.clemble.casino.goal.suggestion.controller.GoalSuggestionServiceController;

/**
 * Created by mavarazy on 9/15/14.
 */
public class IntegrationGoalOperationsFactory {

    final private GoalConfigurationServiceController configurationService;
    final private GoalInitiationServiceController initiationService;
    final private GoalSuggestionServiceController suggestionService;
    final private FriendInitiationServiceController friendInitiationService;
    final private GoalConstructionServiceController constructionService;
    final private FriendGoalServiceController timelineService;
    final private GoalActionServiceController actionService;
    final private GoalRecordServiceController recordService;

    public IntegrationGoalOperationsFactory(
        GoalConfigurationServiceController configurationService,
        GoalInitiationServiceController initiationService,
        GoalSuggestionServiceController suggestionService,
        FriendInitiationServiceController friendInitiationService,
        GoalConstructionServiceController constructionService,
        FriendGoalServiceController timelineService,
        GoalActionServiceController actionService,
        GoalRecordServiceController recordService) {
        this.configurationService = configurationService;
        this.initiationService = initiationService;
        this.suggestionService = suggestionService;
        this.friendInitiationService = friendInitiationService;
        this.constructionService = constructionService;
        this.actionService = actionService;
        this.recordService = recordService;
        this.timelineService = timelineService;
    }

    public GoalOperations construct(String player) {
        return new IntegrationGoalOperations(player,
            configurationService,
            initiationService,
            suggestionService,
            friendInitiationService,
            constructionService,
            actionService,
            timelineService,
            recordService);
    }

}
