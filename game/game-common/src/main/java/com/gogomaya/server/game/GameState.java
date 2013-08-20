package com.gogomaya.server.game;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gogomaya.server.game.outcome.GameOutcome;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface GameState extends GamePlayerStateAware, GamePlayerIteratorAware, Serializable {

    public GameOutcome getOutcome();

    public GameState setOutcome(GameOutcome outcome);

    public int getVersion();

}
