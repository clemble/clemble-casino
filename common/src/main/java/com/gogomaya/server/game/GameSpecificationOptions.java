package com.gogomaya.server.game;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDef;

import com.gogomaya.server.hibernate.JsonHibernateType;

//@Entity
//@Table(name = "GAME_SPECIFICATION_OPTIONS")
//@TypeDef(name = "ruleOptions", typeClass = JsonHibernateType.class, parameters = { @Parameter(name = "class", value = "Collection.class") })
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
