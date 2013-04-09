package com.gogomaya.server.game;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.Collection;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.gogomaya.server.game.rule.GameRule;
import com.google.common.collect.ImmutableList;

public class GameRuleOptions implements Serializable {

    /**
     * Generated 09/04/12
     */
    private static final long serialVersionUID = 6572099322803191748L;

    @JsonProperty("default")
    final private GameRule defaultOption;

    @JsonProperty("options")
    final private Collection<GameRule> options;

    @JsonCreator
    public GameRuleOptions(@JsonProperty("default") final GameRule defaultOption, @JsonProperty("options") final Collection<GameRule> allOptions) {
        this.defaultOption = checkNotNull(defaultOption);
        if (!checkNotNull(allOptions).contains(defaultOption))
            allOptions.add(defaultOption);
        this.options = ImmutableList.<GameRule> copyOf(allOptions);
    }

    public GameRule getDefault() {
        return defaultOption;
    }

    public Collection<GameRule> getOptions() {
        return options;
    }

}
