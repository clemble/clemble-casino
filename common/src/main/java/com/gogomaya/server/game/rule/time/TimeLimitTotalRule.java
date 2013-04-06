package com.gogomaya.server.game.rule.time;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;

final public class TimeLimitTotalRule extends TimeLimitRule {

    /**
     * Generated 09/04/13
     */
    private static final long serialVersionUID = 7452918511506230595L;

    final private int gameTimeLimit;

    @JsonIgnore
    private TimeLimitTotalRule(final TimeBreachBehavior timeBreachBehavior, final int timeLimit) {
        super(timeBreachBehavior);
        if (timeLimit < 0)
            throw GogomayaException.create(GogomayaError.ClientJsonInvalidError);
        this.gameTimeLimit = timeLimit;
    }

    public int getGameTimeLimit() {
        return gameTimeLimit;
    }

    @JsonCreator
    public static TimeLimitTotalRule create(@JsonProperty("punishment") final TimeBreachBehavior timeRuleBreachBehavior, @JsonProperty("limit") final int timeLimit) {
        return new TimeLimitTotalRule(timeRuleBreachBehavior, timeLimit);
    }

}