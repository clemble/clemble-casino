package com.gogomaya.server.game.action;

import java.util.Collection;

import org.springframework.scheduling.Trigger;

import com.gogomaya.event.ClientEvent;
import com.gogomaya.game.GameAware;
import com.gogomaya.game.SessionAware;

public interface GameEventTask extends Trigger, SessionAware, GameAware {

    public Collection<ClientEvent> execute();

}
