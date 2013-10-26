package com.clemble.casino.game.rule.time;

import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.game.rule.GameRule;

public interface TimeRule extends GameRule {

    public TimeBreachPunishment getPunishment();

    public long getLimit();

    public long getBreachTime(long totalTimeSpent);

    public ClientEvent toTimeBreachedEvent(String player);

}