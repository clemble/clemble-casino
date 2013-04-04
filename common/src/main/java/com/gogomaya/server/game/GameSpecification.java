package com.gogomaya.server.game;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.gogomaya.server.game.rule.GameRuleSpecification;
import com.gogomaya.server.game.table.rule.GameTableSpecification;

@Embeddable
public class GameSpecification implements Serializable {

    /**
     * Generated 27/03/13
     */
    private static final long serialVersionUID = -7713576722470320974L;

    final public static GameSpecification DEFAULT_SPECIFICATION = GameSpecification.create(null, null);

    @Embedded
    private GameTableSpecification tableSpecification;

    @Embedded
    private GameRuleSpecification ruleSpecification;

    private GameSpecification() {
    }

    private GameSpecification(final GameTableSpecification tableSpecification, final GameRuleSpecification ruleSpecification) {
        this.tableSpecification = tableSpecification == null ? GameTableSpecification.DEFAULT_TABLE_SPECIFICATION : tableSpecification;
        this.ruleSpecification = ruleSpecification == null ? GameRuleSpecification.DEFAULT_RULE_SPECIFICATION : ruleSpecification;
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

}
