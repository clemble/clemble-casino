package com.gogomaya.server.game;

import java.util.Collection;

public class GameSpecificationOptions {

    private Collection<GameRuleOptions> genericOptions;

    private Collection<Collection<GameRuleOptions>> specificOption;

    public Collection<GameRuleOptions> getGenericOptions() {
        return genericOptions;
    }

    public void setGenericOptions(Collection<GameRuleOptions> genericOptions) {
        this.genericOptions = genericOptions;
    }

    public Collection<Collection<GameRuleOptions>> getSpecificOption() {
        return specificOption;
    }

    public void setSpecificOption(Collection<Collection<GameRuleOptions>> specificOption) {
        this.specificOption = specificOption;
    }
}
