package com.gogomaya.server.game.configuration;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.cloudfoundry.org.codehaus.jackson.annotate.JsonCreator;
import org.cloudfoundry.org.codehaus.jackson.annotate.JsonProperty;

import com.gogomaya.server.game.GameSpecification;
import com.google.common.collect.ImmutableList;

public class SelectSpecificationOptions implements GameSpecificationOptions {

    /**
     * Generated 13/04/13
     */
    private static final long serialVersionUID = 8784325146021105406L;

    public List<? extends GameSpecification> specifications;

    public SelectSpecificationOptions() {
    }

    @JsonCreator
    public SelectSpecificationOptions(@JsonProperty("specifications") List<? extends GameSpecification> gameSpecifications) {
        this.specifications = ImmutableList.<GameSpecification> copyOf(checkNotNull(gameSpecifications));
    }

    public List<? extends GameSpecification> getSpecifications() {
        return specifications;
    }

    @Override
    public boolean valid(GameSpecification specification) {
        return specifications.contains(specification);
    }

}
