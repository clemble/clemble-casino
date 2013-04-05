package com.gogomaya.server.game.rule.time;

import static com.google.common.base.Preconditions.checkNotNull;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.gogomaya.server.game.rule.GameRule;
import com.gogomaya.server.game.rule.time.TimeRuleFormat.CustomTimeRuleDeserializer;
import com.gogomaya.server.game.rule.time.TimeRuleFormat.CustomTimeRuleSerializer;

@JsonSerialize(using = CustomTimeRuleSerializer.class)
@JsonDeserialize(using = CustomTimeRuleDeserializer.class)
abstract public class TimeRule implements GameRule {

    /**
     * Generated 09/04/13
     */
    private static final long serialVersionUID = -698053197734249764L;

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((breachBehavior == null) ? 0 : breachBehavior.hashCode());
        result = prime * result + ((timeRuleType == null) ? 0 : timeRuleType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TimeRule other = (TimeRule) obj;
        if (breachBehavior != other.breachBehavior)
            return false;
        if (timeRuleType != other.timeRuleType)
            return false;
        return true;
    }

}