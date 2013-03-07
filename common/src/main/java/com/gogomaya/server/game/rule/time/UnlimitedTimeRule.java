package com.gogomaya.server.game.rule.time;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.gogomaya.server.game.rule.time.TimeRuleFormat.CustomTimeRuleDeseriler;
import com.gogomaya.server.game.rule.time.TimeRuleFormat.CustomTimeRuleSerializer;

@JsonSerialize(using = CustomTimeRuleSerializer.class)
@JsonDeserialize(using = CustomTimeRuleDeseriler.class)
final public class UnlimitedTimeRule extends TimeRule {

    private UnlimitedTimeRule(TimeBreachBehavior ruleBreachBehavior) {
        super(TimeRuleType.Unlimited, ruleBreachBehavior);
    }

    public static UnlimitedTimeRule create(TimeBreachBehavior timeRuleBreachBehavior) {
        return new UnlimitedTimeRule(timeRuleBreachBehavior);
    }

}