package com.clemble.casino.goal.aspect.judge;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.goal.aspect.GoalAspect;
import com.clemble.casino.lifecycle.management.event.surrender.TotalTimeoutSurrenderEvent;

/**
 * Created by mavarazy on 10/8/14.
 */
public class GoalJudgeAspect extends GoalAspect<TotalTimeoutSurrenderEvent>{

    public GoalJudgeAspect() {
        super(new EventTypeSelector(TotalTimeoutSurrenderEvent.class));
    }

    @Override
    public void doEvent(TotalTimeoutSurrenderEvent event) {
    }

}
