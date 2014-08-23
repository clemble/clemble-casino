package com.clemble.casino.goal.task;

import com.clemble.casino.goal.GoalJudgeDuty;
import com.clemble.casino.goal.GoalJudgeDutyStatus;
import com.clemble.casino.goal.repository.GoalJudgeDutyRepository;
import com.clemble.casino.server.event.goal.SystemGoalJudgeDutyDueEvent;
import com.clemble.casino.server.player.notification.SystemNotificationService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mavarazy on 8/23/14.
 */
public class GoalJudgeDutyDueDateTask implements Runnable {

    final private GoalJudgeDutyRepository dutyRepository;
    final private SystemNotificationService notificationService;

    public GoalJudgeDutyDueDateTask(SystemNotificationService notificationService, GoalJudgeDutyRepository dutyRepository) {
        this.dutyRepository = dutyRepository;
        this.notificationService = notificationService;
    }

    @Override
    public void run() {
        // Step 1. Updating all pending duties
        Date now = new Date();
        // Step 2. Searching for Due Duties
        List<GoalJudgeDuty> dueDuties = dutyRepository.findByStatusAndDueDateBefore(GoalJudgeDutyStatus.pending, now);
        // Step 3. Generating dueDuties to save
        List<GoalJudgeDuty> dueDutiesToSave = new ArrayList<GoalJudgeDuty>();
        for(GoalJudgeDuty duty: dueDuties) {
            dueDutiesToSave.add(duty.cloneWithStatus(GoalJudgeDutyStatus.due));
        }
        // Step 4. Updating mongo repository
        List<GoalJudgeDuty> savedDuties =  dutyRepository.save(dueDutiesToSave);
        // Step 5. Sending notification to interested parties
        for(GoalJudgeDuty savedDuty: savedDuties)
            notificationService.notify(new SystemGoalJudgeDutyDueEvent(savedDuty));
    }
}
