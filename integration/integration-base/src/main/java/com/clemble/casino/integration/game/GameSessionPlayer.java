package com.clemble.casino.integration.game;

import java.io.Closeable;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameSessionAware;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.event.client.GameAction;
import com.clemble.casino.game.specification.GameSpecificationAware;
import com.clemble.casino.player.PlayerAware;

public interface GameSessionPlayer<State extends GameState> extends GameSessionAware, PlayerAware, Closeable, GameSpecificationAware {

    public ClembleCasinoOperations playerOperations();

    public GameConstruction getConstructionInfo();

    public State getState();

    public boolean isAlive();

    public void syncWith(GameSessionPlayer<State> anotherState);

    public void waitForStart();

    public void waitForTurn();

    public int getVersion();

    public void waitVersion(int version);

    public boolean isToMove();

    public Event getNextMove();

    public void perform(GameAction gameAction);

    public void giveUp();

    public void close();

}
