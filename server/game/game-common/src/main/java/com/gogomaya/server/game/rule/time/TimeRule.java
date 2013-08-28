package com.gogomaya.server.game.rule.time;

import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.rule.GameRule;

public interface TimeRule extends GameRule {

    public TimeBreachPunishment getPunishment();

    public long getLimit();

    public long getBreachTime(long totalTimeSpent);

    public ClientEvent toTimeBreachedEvent(long player);

}