package com.gogomaya.server.game.rule;

import com.gogomaya.server.game.GameRule;


abstract public class TimeRule extends GameRule {

    final private TimeRuleBreachBehavior timeExceedBehavior;

    public TimeRule(TimeRuleBreachBehavior exceedBehavior) {
        this.timeExceedBehavior = exceedBehavior;
    }

    public TimeRuleBreachBehavior getTimeExceedBehavior() {
        return timeExceedBehavior;
    }
}