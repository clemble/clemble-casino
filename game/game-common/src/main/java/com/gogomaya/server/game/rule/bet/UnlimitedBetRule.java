package com.gogomaya.server.game.rule.bet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

final public class UnlimitedBetRule implements BetRule {

    /**
     * Generated 09/04/13
     */
    private static final long serialVersionUID = 6788161410535376939L;

    @JsonIgnore
    private UnlimitedBetRule() {
    }

    public static UnlimitedBetRule INSTANCE = new UnlimitedBetRule();

    @JsonCreator
    // This constructor is a workaround for Jackson deserializer
    // There can't be Creator without at least one element
    public static UnlimitedBetRule create(@JsonProperty("betType") String betType) {
        return INSTANCE;
    }

}