package com.clemble.casino.goal.aspect.judge;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.goal.aspect.GoalAspect;
import com.clemble.casino.lifecycle.management.event.action.surrender.TotalTimeoutSurrenderAction;

/**
 * Created by mavarazy on 10/8/14.
 */
public class GoalJudgeAspect extends GoalAspect<TotalTimeoutSurrenderAction>{

    public GoalJudgeAspect() {
        super(new EventTypeSelector(TotalTimeoutSurrenderAction.class));
    }

    @Override
    public void doEvent(TotalTimeoutSurrenderAction event) {
    }

}
