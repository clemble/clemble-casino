package com.clemble.casino.goal.listener;

import com.clemble.casino.goal.Goal;
import com.clemble.casino.goal.GoalJudgeInvitation;
import com.clemble.casino.goal.repository.GoalJudgeInvitationRepository;
import com.clemble.casino.server.event.goal.SystemGoalCreatedEvent;
import com.clemble.casino.server.event.goal.SystemGoalJudgeInvitationCreatedEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.player.notification.SystemNotificationService;

/**
 * Created by mavarazy on 8/18/14.
 */
public class GoalJudgeInvitationCreatorEventListener implements SystemEventListener<SystemGoalCreatedEvent> {

    final private GoalJudgeInvitationRepository invitationRepository;
    final private SystemNotificationService notificationService;

    public GoalJudgeInvitationCreatorEventListener(GoalJudgeInvitationRepository invitationRepository, SystemNotificationService notificationService) {
        this.invitationRepository = invitationRepository;
        this.notificationService = notificationService;
    }

    @Override
    public void onEvent(SystemGoalCreatedEvent event) {
        // Step 1. Extracting dependent goal
        Goal newGoal = event.getGoal();
        // Step 2. Checking if there is a need for invitation
        if (newGoal.getJudge().equals(newGoal.getPlayer()))
            return;
        // Step 3. Creating new GoalJudgeInvitation
        GoalJudgeInvitation invitationToSave = GoalJudgeInvitation.fromGoal(newGoal);
        GoalJudgeInvitation savedInvitation = invitationRepository.save(invitationToSave);
        // Step 4. Notifying system of new invitation
        notificationService.notify(new SystemGoalJudgeInvitationCreatedEvent(savedInvitation));
    }

    @Override
    public String getChannel() {
        return SystemGoalCreatedEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemGoalCreatedEvent.CHANNEL + " > goal:judge:invitation";
    }
}
