package com.gogomaya.server.game;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gogomaya.server.ActionLatch;
import com.gogomaya.server.game.outcome.GameOutcome;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface GameState extends Serializable {

    public GameAccount getAccount();

    public GamePlayerIterator getPlayerIterator();

    public ActionLatch getActionLatch();

    public GameOutcome getOutcome();

    public int getVersion();

}
