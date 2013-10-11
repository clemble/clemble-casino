package com.clemble.casino.game.rule.time;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.game.configuration.GameRuleOptions;
import com.clemble.casino.game.event.client.surrender.MoveTimeoutSurrenderEvent;
import com.fasterxml.jackson.annotation.JsonTypeName;

@Embeddable
@JsonTypeName("moveTime")
public class MoveTimeRule implements TimeRule {

    /**
     * Generated 09/04/13
     */
    private static final long serialVersionUID = -2949008185370674021L;

    final public static MoveTimeRule DEFAULT = new MoveTimeRule().setPunishment(TimeBreachPunishment.loose).setLimit(0);

    final public static GameRuleOptions<MoveTimeRule> DEFAULT_OPTIONS = new GameRuleOptions<MoveTimeRule>(DEFAULT);

    @Column(name = "MOVE_TIME_PUNISHMENT")
    @Enumerated(EnumType.STRING)
    private TimeBreachPunishment punishment;

    @Column(name = "MOVE_TIME_LIMIT")
    private long limit;

    public TimeBreachPunishment getPunishment() {
        return punishment;
    }

    public MoveTimeRule setPunishment(TimeBreachPunishment punishment) {
        this.punishment = punishment;
        return this;
    }

    @Override
    public long getBreachTime(long totalTimeSpent) {
        return limit;
    }

    @Override
    public ClientEvent toTimeBreachedEvent(String player) {
        return new MoveTimeoutSurrenderEvent(player);
    }

    @Override
    public long getLimit() {
        return limit;
    }

    public MoveTimeRule setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (limit ^ (limit >>> 32));
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
        MoveTimeRule other = (MoveTimeRule) obj;
        if (limit != other.limit)
            return false;
        if (punishment != other.punishment)
            return false;
        return true;
    }

}