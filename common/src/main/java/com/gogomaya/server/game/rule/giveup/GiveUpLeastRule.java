package com.gogomaya.server.game.rule.giveup;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

final public class GiveUpLeastRule extends GiveUpRule {

    /**
     * Generated 09/04/13
     */
    private static final long serialVersionUID = 400326180244339700L;

    final private int minPart;

    @JsonIgnore
    private GiveUpLeastRule(final int minPart) {
        if (minPart > 100)
            throw new IllegalArgumentException("Min part can't exceed 100");
        if (minPart < 0)
            throw new IllegalArgumentException("Min part can't be less than 0");
        this.minPart = minPart;
    }

    public int getMinPart() {
        return minPart;
    }

    @JsonCreator
    public static GiveUpLeastRule create(@JsonProperty("min") int minPart) {
        return new GiveUpLeastRule(minPart);
    }

}