package com.clemble.casino.server.game.action;

import java.util.Collection;

import org.springframework.scheduling.Trigger;

import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.game.SessionAware;

public interface GameEventTask extends Trigger, SessionAware {

    public Collection<ClientEvent> execute();

}
