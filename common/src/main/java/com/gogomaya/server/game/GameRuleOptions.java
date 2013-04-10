package com.gogomaya.server.game;

import java.util.Collection;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.gogomaya.server.game.rule.GameRule;
import com.google.common.collect.ImmutableList;

public class GameRuleOptions<T extends GameRule> {

    @JsonProperty("default")
    final private T defaultOption;

    @JsonProperty("all")
    final private Collection<T> allOptions;

    @JsonCreator
    public GameRuleOptions(@JsonProperty("default") T defaultOption, @JsonProperty("all") Collection<T> allOptions) {
        this.defaultOption = defaultOption;
        this.allOptions = ImmutableList.<T> copyOf(allOptions);
    }

    public T getDefault() {
        return defaultOption;
    }

    public Collection<T> getOptions() {
        return allOptions;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((allOptions == null) ? 0 : allOptions.hashCode());
        result = prime * result + ((defaultOption == null) ? 0 : defaultOption.hashCode());
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GameRuleOptions<T> other = (GameRuleOptions<T>) obj;
        if (allOptions == null) {
            if (other.allOptions != null)
                return false;
        } else if (!allOptions.equals(other.allOptions))
            return false;
        if (defaultOption == null) {
            if (other.defaultOption != null)
                return false;
        } else if (!defaultOption.equals(other.defaultOption))
            return false;
        return true;
    }

}
