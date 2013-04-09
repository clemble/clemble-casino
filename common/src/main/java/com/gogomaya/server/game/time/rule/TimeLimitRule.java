package com.gogomaya.server.game.time.rule;

import static com.google.common.base.Preconditions.checkNotNull;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;

import com.gogomaya.server.game.rule.GameRule;

@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "timeLimitType")
@JsonSubTypes({ @Type(name = "move", value = TimeLimitMoveRule.class), @Type(name = "total", value = TimeLimitTotalRule.class),
        @Type(name = "none", value = TimeLimitNoneRule.class) })
abstract public class TimeLimitRule implements GameRule {

    /**
     * Generated 09/04/13
     */
    private static final long serialVersionUID = -698053197734249764L;

    final public static TimeLimitRule DEFAULT = TimeLimitNoneRule.INSTANCE;

    final private TimeBreachBehavior breachBehavior;

    protected TimeLimitRule(final TimeBreachBehavior exceedBehavior) {
        this.breachBehavior = checkNotNull(exceedBehavior);
    }

    public TimeBreachBehavior getBreachBehavior() {
        return breachBehavior;
    }

}