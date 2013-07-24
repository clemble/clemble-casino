package com.gogomaya.server.game.rule.time;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.server.game.configuration.GameRuleOptions;

@Embeddable
@JsonTypeName("totalTime")
public class TotalTimeRule implements TimeRule {

    /**
     * Generated 09/04/13
     */
    private static final long serialVersionUID = 7452918511506230595L;

    final public static TotalTimeRule DEFAULT = new TotalTimeRule().setPunishment(TimeBreachPunishment.loose).setLimit(10);
    final public static GameRuleOptions<TotalTimeRule> DEFAULT_OPTIONS = new GameRuleOptions<TotalTimeRule>(DEFAULT);

    @Column(name = "TOTAL_TIME_PUNISHMENT")
    @Enumerated(EnumType.STRING)
    private TimeBreachPunishment punishment;

    @Column(name = "TOTAL_TIME_LIMIT")
    private int limit;

    public TotalTimeRule() {
    }

    public TimeBreachPunishment getPunishment() {
        return punishment;
    }

    public TotalTimeRule setPunishment(TimeBreachPunishment punishment) {
        this.punishment = punishment;
        return this;
    }

    public int getLimit() {
        return limit;
    }

    public TotalTimeRule setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + limit;
        result = prime * result + ((punishment == null) ? 0 : punishment.hashCode());
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
        TotalTimeRule other = (TotalTimeRule) obj;
        if (limit != other.limit)
            return false;
        if (punishment != other.punishment)
            return false;
        return true;
    }

}