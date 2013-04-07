package com.gogomaya.server.game.time.rule;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

final public class TimeLimitMoveRule extends TimeLimitRule {

    /**
     * Generated 09/04/13
     */
    private static final long serialVersionUID = -2949008185370674021L;

    final private int moveTimeLimit;

    @JsonIgnore
    private TimeLimitMoveRule(final TimeBreachBehavior timeBreachBehavior, final int timeLimit) {
        super(timeBreachBehavior);
        this.moveTimeLimit = timeLimit;
    }

    public int getMoveTimeLimit() {
        return moveTimeLimit;
    }

    @JsonCreator
    public static TimeLimitMoveRule create(@JsonProperty("punishment") final TimeBreachBehavior timeRuleBreachBehavior, @JsonProperty("limit") final int timeLimit) {
        return new TimeLimitMoveRule(timeRuleBreachBehavior, timeLimit);
    }

}