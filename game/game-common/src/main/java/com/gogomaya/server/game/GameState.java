package com.gogomaya.server.game;

import java.io.Serializable;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gogomaya.server.ActionLatch;
import com.gogomaya.server.game.outcome.GameOutcome;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface GameState extends Serializable {

    public Collection<GamePlayerState> getPlayerStates();

    public GamePlayerState getPlayerState(long playerId);

    public GameState setPlayerState(GamePlayerState player);

    public GameState setPlayerStates(Collection<GamePlayerState> playersStates);




    public GamePlayerIterator getPlayerIterator();

    public GameState setPlayerIterator(GamePlayerIterator playerIterator);




    public ActionLatch getActionLatch();




    public GameOutcome getOutcome();

    public GameState setOutcome(GameOutcome outcome);



    public boolean complete();

    public int getVersion();

}
