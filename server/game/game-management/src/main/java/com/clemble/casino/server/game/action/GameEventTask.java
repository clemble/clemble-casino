package com.clemble.casino.server.game.action;

import java.util.Collection;

import org.springframework.scheduling.Trigger;

import com.clemble.casino.game.GameSessionAware;
import com.clemble.casino.game.event.client.GameAction;

public interface GameEventTask extends Trigger, GameSessionAware {

    public Collection<GameAction> execute();

}
