package com.gogomaya.server.game;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.gogomaya.server.game.rule.GameRuleSpecification;
import com.gogomaya.server.game.table.GameTableSpecification;

@Embeddable
public class GameSpecification implements Serializable {

    /**
     * Generated 27/03/13
     */
    private static final long serialVersionUID = -7713576722470320974L;

    final public static GameSpecification DEFAULT = GameSpecification.create(GameTableSpecification.DEFAULT, GameRuleSpecification.DEFAULT);

    @Embedded
    private GameTableSpecification tableSpecification;

    @Embedded
    private GameRuleSpecification ruleSpecification;

    private GameSpecification() {
    }

    private GameSpecification(final GameTableSpecification tableSpecification, final GameRuleSpecification ruleSpecification) {
        this.tableSpecification = tableSpecification == null ? GameTableSpecification.DEFAULT : tableSpecification;
        this.ruleSpecification = ruleSpecification == null ? GameRuleSpecification.DEFAULT : ruleSpecification;
    }

    public GameTableSpecification getTableSpecification() {
        return tableSpecification;
    }

    public GameSpecification setTableSpecification(GameTableSpecification tableSpecification) {
        this.tableSpecification = tableSpecification;
        return this;
    }

    public GameRuleSpecification getRuleSpecification() {
        return ruleSpecification;
    }

    public GameSpecification setRuleSpecification(GameRuleSpecification ruleSpecification) {
        this.ruleSpecification = ruleSpecification;
        return this;
    }

    @JsonCreator
    public static GameSpecification create(
            @JsonProperty("tableSpecification") final GameTableSpecification tableSpecification,
            @JsonProperty("ruleSpecification") final GameRuleSpecification ruleSpecification) {
        return new GameSpecification(tableSpecification, ruleSpecification);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ruleSpecification == null) ? 0 : ruleSpecification.hashCode());
        result = prime * result + ((tableSpecification == null) ? 0 : tableSpecification.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GameSpecification other = (GameSpecification) obj;
        if (ruleSpecification == null) {
            if (other.ruleSpecification != null)
                return false;
        } else if (!ruleSpecification.equals(other.ruleSpecification))
            return false;
        if (tableSpecification == null) {
            if (other.tableSpecification != null)
                return false;
        } else if (!tableSpecification.equals(other.tableSpecification))
            return false;
        return true;
    }

}
