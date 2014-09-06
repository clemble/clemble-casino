package com.clemble.casino.server.game.action;

import java.util.Collection;

import com.clemble.casino.event.Event;
import org.springframework.scheduling.Trigger;

import com.clemble.casino.game.GameSessionAware;

public interface GameEventTask extends Trigger, GameSessionAware {

    public Collection<Event> execute();

}
