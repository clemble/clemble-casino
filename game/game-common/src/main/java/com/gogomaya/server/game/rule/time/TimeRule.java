package com.gogomaya.server.game.rule.time;

import com.gogomaya.server.game.rule.GameRule;

abstract public interface TimeRule extends GameRule {

    public TimeBreachPunishment getPunishment();

    public int getLimit();

}