package com.gogomaya.server.game.rule.time;

import static com.google.common.base.Preconditions.checkNotNull;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.gogomaya.server.game.GameRule;
import com.gogomaya.server.game.rule.time.TimeRuleFormat.CustomTimeRuleDeseriler;
import com.gogomaya.server.game.rule.time.TimeRuleFormat.CustomTimeRuleSerializer;

@JsonSerialize(using = CustomTimeRuleSerializer.class)
@JsonDeserialize(using = CustomTimeRuleDeseriler.class)
abstract public class TimeRule implements GameRule {

    final private TimeRuleType timeRuleType;

    final private TimeBreachBehavior breachBehavior;

    protected TimeRule(final TimeRuleType timeRuleType, final TimeBreachBehavior exceedBehavior) {
        this.breachBehavior = checkNotNull(exceedBehavior);
        this.timeRuleType = checkNotNull(timeRuleType);
    }

    public TimeBreachBehavior getBreachBehavior() {
        return breachBehavior;
    }

    public TimeRuleType getRuleType() {
        return timeRuleType;
    }

}