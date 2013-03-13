package com.gogomaya.server.game.rule.time;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.gogomaya.server.game.rule.time.TimeRuleFormat.CustomTimeRuleDeserializer;
import com.gogomaya.server.game.rule.time.TimeRuleFormat.CustomTimeRuleSerializer;

@JsonSerialize(using = CustomTimeRuleSerializer.class)
@JsonDeserialize(using = CustomTimeRuleDeserializer.class)
final public class LimitedMoveTimeRule extends TimeRule {

    /**
     * Generated 09/04/13
     */
    private static final long serialVersionUID = -2949008185370674021L;

    final private int moveTimeLimit;

    private LimitedMoveTimeRule(final TimeBreachBehavior timeBreachBehavior, final int timeLimit) {
        super(TimeRuleType.LimitedMoveTime, timeBreachBehavior);
        this.moveTimeLimit = timeLimit;
    }

    public int getMoveTimeLimit() {
        return moveTimeLimit;
    }

    public static LimitedMoveTimeRule create(final TimeBreachBehavior timeRuleBreachBehavior, final int timeLimit) {
        return new LimitedMoveTimeRule(timeRuleBreachBehavior, timeLimit);
    }

}