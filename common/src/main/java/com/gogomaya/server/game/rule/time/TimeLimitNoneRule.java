package com.gogomaya.server.game.rule.time;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

final public class TimeLimitNoneRule extends TimeLimitRule {

    /**
     * Generated 09/04/13
     */
    private static final long serialVersionUID = 1731476218744551572L;

    @JsonIgnore
    private TimeLimitNoneRule() {
        super(TimeBreachBehavior.nothing);
    }

    public static TimeLimitNoneRule INSTANCE = new TimeLimitNoneRule();

    @JsonCreator
    // This constructor is a workaround for Jackson deserializer
    // There can't be Creator without at least one element
    public static TimeLimitNoneRule create(@JsonProperty("timeLimitType") String limitType) {
        return INSTANCE;
    }
}