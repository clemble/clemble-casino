package com.clemble.casino.integration.goal;

import com.clemble.casino.client.goal.GoalOperations;
import com.clemble.casino.goal.configuration.GoalConfiguration;
import com.clemble.casino.goal.configuration.controller.GoalConfigurationServiceController;
import com.clemble.casino.goal.configuration.service.GoalConfigurationService;
import com.clemble.casino.goal.construction.controller.GoalConstructionServiceController;
import com.clemble.casino.goal.construction.controller.GoalInitiationServiceController;
import com.clemble.casino.goal.construction.service.GoalConstructionService;
import com.clemble.casino.goal.construction.service.GoalInitiationService;
import com.clemble.casino.goal.controller.GoalJudgeDutyServiceController;
import com.clemble.casino.goal.controller.GoalJudgeInvitationServiceController;
import com.clemble.casino.goal.service.GoalJudgeDutyService;
import com.clemble.casino.goal.service.GoalJudgeInvitationService;
import com.clemble.casino.goal.service.GoalService;
import com.clemble.casino.server.goal.controller.GoalServiceController;

/**
 * Created by mavarazy on 9/15/14.
 */
public class IntegrationGoalOperations implements GoalOperations {

    final private GoalConfigurationService configurationService;
    final private GoalConstructionService constructionService;
    final private GoalInitiationService initiationService;
    final private GoalService goalService;
    final private GoalJudgeDutyService judgeDutyService;
    final private GoalJudgeInvitationService judgeInvitationService;

    public IntegrationGoalOperations(String player,
        GoalConfigurationServiceController configurationService,
        GoalInitiationServiceController initiationService,
        GoalConstructionServiceController constructionService,
        GoalServiceController goalController,
        GoalJudgeDutyServiceController dutyServiceController,
        GoalJudgeInvitationServiceController invitationServiceController) {
        this.configurationService = new IntegrationGoalConfigurationService(player, configurationService);
        this.initiationService = new IntegrationGoalInitiationService(player, initiationService);
        this.constructionService = new IntegrationGoalConstructionService(player, constructionService);
        this.goalService = new IntegrationGoalService(player, goalController);
        this.judgeDutyService = new InvitationGoalJudgeDutyService(player, dutyServiceController);
        this.judgeInvitationService = new IntegrationGoalJudgeInvitationService(player, invitationServiceController);

    }

    @Override
    public GoalConfigurationService configurationService() {
        return configurationService;
    }

    @Override
    public GoalConstructionService constructionService() {
        return constructionService;
    }

    @Override
    public GoalInitiationService initiationService() {
        return initiationService;
    }

    @Override
    public GoalService goalService() {
        return goalService;
    }

    @Override
    public GoalJudgeInvitationService goalInvitationOperations() {
        return judgeInvitationService;
    }

    @Override
    public GoalJudgeDutyService goalDutyOperations() {
        return judgeDutyService;
    }
}
