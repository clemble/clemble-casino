package com.gogomaya.server.game;

import java.util.Collection;

import javax.persistence.Embeddable;

import org.codehaus.jackson.annotate.JsonProperty;

import com.gogomaya.server.game.rule.GameRule;

@Embeddable
public class GameRuleOptions {

    @JsonProperty("default")
    private GameRule defaultOption;

    @JsonProperty("all")
    private Collection<GameRule> allOptions;

    public GameRule getDefault() {
        return defaultOption;
    }

    public void setDefault(GameRule defaultOption) {
        this.defaultOption = defaultOption;
    }

    public Collection<GameRule> getAll() {
        return allOptions;
    }

    public void setAll(Collection<GameRule> allOptions) {
        this.allOptions = allOptions;
    }

}
