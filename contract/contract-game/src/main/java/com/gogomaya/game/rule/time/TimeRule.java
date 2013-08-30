package com.gogomaya.game.rule.time;

import com.gogomaya.event.ClientEvent;
import com.gogomaya.game.rule.GameRule;

public interface TimeRule extends GameRule {

    public TimeBreachPunishment getPunishment();

    public long getLimit();

    public long getBreachTime(long totalTimeSpent);

    public ClientEvent toTimeBreachedEvent(long player);

}