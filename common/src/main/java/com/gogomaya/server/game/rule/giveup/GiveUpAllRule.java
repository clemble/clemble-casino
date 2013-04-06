package com.gogomaya.server.game.rule.giveup;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

final public class GiveUpAllRule extends GiveUpRule {

    /**
     * Generated 09/04/13
     */
    private static final long serialVersionUID = 2325950774148394868L;

    public static GiveUpAllRule INSTANCE = new GiveUpAllRule();

    @JsonIgnore
    private GiveUpAllRule() {
    }

    @JsonCreator
    // This constructor is a workaround for Jackson deserializer
    // There can't be Creator without at least one element
    public static GiveUpAllRule create(@JsonProperty("looseType") String looseType) {
        return INSTANCE;
    }

}