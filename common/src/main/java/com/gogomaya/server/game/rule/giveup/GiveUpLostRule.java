package com.gogomaya.server.game.rule.giveup;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

final public class GiveUpLostRule extends GiveUpRule {

    /**
     * Generated 09/04/13
     */
    private static final long serialVersionUID = -3416635969563391335L;

    public static GiveUpLostRule INSTANCE = new GiveUpLostRule();

    @JsonIgnore
    private GiveUpLostRule() {
    }

    @JsonCreator
    // This constructor is a workaround for Jackson deserializer
    // There can't be Creator without at least one element
    public static GiveUpLostRule create(@JsonProperty("looseType") String looseType) {
        return INSTANCE;
    }

}