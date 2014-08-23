package com.clemble.casino.goal.listener;

import com.clemble.casino.goal.GoalJudgeDuty;
import com.clemble.casino.goal.GoalJudgeInvitation;
import com.clemble.casino.goal.repository.GoalJudgeDutyRepository;
import com.clemble.casino.server.event.goal.SystemGoalJudgeInvitationAcceptedEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;

/**
 * Created by mavarazy on 8/23/14.
 */
public class GoalJudgeDutyCreatorEventListener implements SystemEventListener<SystemGoalJudgeInvitationAcceptedEvent> {

    final private GoalJudgeDutyRepository dutyRepository;

    public GoalJudgeDutyCreatorEventListener(GoalJudgeDutyRepository dutyRepository) {
        this.dutyRepository = dutyRepository;
    }

    @Override
    public void onEvent(SystemGoalJudgeInvitationAcceptedEvent event) {
        // Step 1. Extracting Invitation
        GoalJudgeInvitation invitation = event.getInvitation();
        // Step 2. Converting to goal
        GoalJudgeDuty duty = GoalJudgeDuty.fromInvitation(invitation);
        // Step 3. Saving GoalJudgeDuty
        dutyRepository.save(duty);
    }

    @Override
    public String getChannel() {
        return SystemGoalJudgeInvitationAcceptedEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemGoalJudgeInvitationAcceptedEvent.CHANNEL + " > goal:judge:duty";
    }

}
