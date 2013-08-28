package com.gogomaya.server.game.action;

import java.util.Collection;

import org.springframework.scheduling.Trigger;

import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GameAware;
import com.gogomaya.server.game.SessionAware;

public interface GameEventTask extends Trigger, SessionAware, GameAware {

    public Collection<ClientEvent> execute();

}
