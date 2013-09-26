package com.gogomaya.game;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gogomaya.base.ActionLatch;
import com.gogomaya.event.ClientEvent;
import com.gogomaya.game.account.GameAccount;
import com.gogomaya.game.event.server.GameServerEvent;
import com.gogomaya.game.iterator.GamePlayerIterator;
import com.gogomaya.game.outcome.GameOutcome;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface GameState extends Serializable {

    public <State extends GameState> GameServerEvent<State> process(GameSession<State> session,ClientEvent move);

    public GameAccount getAccount();

    public GamePlayerIterator getPlayerIterator();

    public ActionLatch getActionLatch();

    public GameOutcome getOutcome();

    public int getVersion();

}
