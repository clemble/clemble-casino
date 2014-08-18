package com.clemble.casino.integration.goal;

import com.clemble.casino.goal.GoalJudgeInvitation;
import com.clemble.casino.goal.GoalKey;
import com.clemble.casino.goal.controller.GoalJudgeInvitationServiceController;
import com.clemble.casino.goal.service.GoalJudgeInvitationService;
import com.clemble.casino.player.PlayerAware;

import java.util.Collection;

/**
 * Created by mavarazy on 8/18/14.
 */
public class IntegrationGoalJudgeInvitationService implements GoalJudgeInvitationService, PlayerAware {

    final private String player;
    final private GoalJudgeInvitationServiceController invitationServiceController;

    public IntegrationGoalJudgeInvitationService(String player, GoalJudgeInvitationServiceController invitationServiceController){
        this.player = player;
        this.invitationServiceController = invitationServiceController;
    }

    @Override
    public Collection<GoalJudgeInvitation> myDuties() {
        return invitationServiceController.myDuties(player);
    }

    @Override
    public Collection<GoalJudgeInvitation> myInvitations() {
        return invitationServiceController.myInvitations(player);
    }

    @Override
    public Collection<GoalJudgeInvitation> myDutiesAndInvitations() {
        return invitationServiceController.myDutiesAndInvitations(player);
    }

    @Override
    public GoalJudgeInvitation reply(GoalKey goalKey, GoalJudgeInvitation response) {
        return invitationServiceController.reply(goalKey, response);
    }

    @Override
    public String getPlayer() {
        return player;
    }
}
