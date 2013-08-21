package com.gogomaya.server.game;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gogomaya.server.ActionLatch;
import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.event.server.GameServerEvent;
import com.gogomaya.server.game.outcome.GameOutcome;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface GameState extends Serializable {

    public <State extends GameState> GameServerEvent<State> process(ClientEvent move);

    public GameAccount getAccount();

    public GamePlayerIterator getPlayerIterator();

    public ActionLatch getActionLatch();

    public GameOutcome getOutcome();

    public int getVersion();

}
