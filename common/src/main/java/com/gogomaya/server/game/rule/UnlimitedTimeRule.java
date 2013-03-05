package com.gogomaya.server.game.rule;

public class UnlimitedTimeRule extends TimeRule {
    public UnlimitedTimeRule(){
        super(TimeRuleBreachBehavior.Fail);
    }
}