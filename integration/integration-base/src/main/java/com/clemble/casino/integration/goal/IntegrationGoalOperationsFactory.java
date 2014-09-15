package com.clemble.casino.integration.goal;

import com.clemble.casino.client.goal.GoalOperations;
import com.clemble.casino.goal.configuration.controller.GoalConfigurationServiceController;
import com.clemble.casino.goal.construction.controller.GoalConstructionServiceController;
import com.clemble.casino.goal.construction.controller.GoalInitiationServiceController;
import com.clemble.casino.goal.controller.GoalJudgeDutyServiceController;
import com.clemble.casino.goal.controller.GoalJudgeInvitationServiceController;
import com.clemble.casino.server.goal.controller.GoalServiceController;

/**
 * Created by mavarazy on 9/15/14.
 */
public class IntegrationGoalOperationsFactory {

    final private GoalConfigurationServiceController configurationService;
    final private GoalInitiationServiceController initiationService;
    final private GoalConstructionServiceController constructionService;
    final private GoalServiceController goalController;
    final private GoalJudgeDutyServiceController dutyServiceController;
    final private GoalJudgeInvitationServiceController invitationServiceController;

    public IntegrationGoalOperationsFactory(
        GoalConfigurationServiceController configurationService,
        GoalInitiationServiceController initiationService,
        GoalConstructionServiceController constructionService,
        GoalServiceController goalController,
        GoalJudgeDutyServiceController dutyServiceController,
        GoalJudgeInvitationServiceController invitationServiceController) {
        this.configurationService = configurationService;
        this.initiationService = initiationService;
        this.constructionService = constructionService;
        this.goalController = goalController;
        this.dutyServiceController = dutyServiceController;
        this.invitationServiceController = invitationServiceController;
    }

    public GoalOperations construct(String player) {
        return new IntegrationGoalOperations(player,
            configurationService,
            initiationService,
            constructionService,
            goalController,
            dutyServiceController,
            invitationServiceController);
    }

}
