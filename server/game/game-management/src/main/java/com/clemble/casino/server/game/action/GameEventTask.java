package com.clemble.casino.server.game.action;

import java.util.Collection;

import org.springframework.scheduling.Trigger;

import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.game.GameSessionAware;

public interface GameEventTask extends Trigger, GameSessionAware {

    public Collection<ClientEvent> execute();

}
