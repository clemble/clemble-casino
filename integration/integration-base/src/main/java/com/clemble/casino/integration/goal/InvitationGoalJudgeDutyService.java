package com.clemble.casino.integration.goal;

import com.clemble.casino.goal.GoalJudgeDuty;
import com.clemble.casino.goal.controller.GoalJudgeDutyServiceController;
import com.clemble.casino.goal.service.GoalJudgeDutyService;
import com.clemble.casino.player.PlayerAware;

import java.util.List;

/**
 * Created by mavarazy on 8/23/14.
 */
public class InvitationGoalJudgeDutyService implements GoalJudgeDutyService, PlayerAware {

    final private String player;
    final private GoalJudgeDutyServiceController dutyServiceController;

    public InvitationGoalJudgeDutyService(String player, GoalJudgeDutyServiceController dutyServiceController) {
        this.player = player;
        this.dutyServiceController = dutyServiceController;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    @Override
    public List<GoalJudgeDuty> myDuties() {
        return dutyServiceController.myDuties(player);
    }

}
