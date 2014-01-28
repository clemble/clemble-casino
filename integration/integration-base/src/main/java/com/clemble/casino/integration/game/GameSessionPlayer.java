package com.clemble.casino.integration.game;

import java.io.Closeable;
import java.util.Collection;
import java.util.List;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameSessionAware;
import com.clemble.casino.game.GameSessionAwareEvent;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.action.GameAction;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.outcome.GameOutcome;
import com.clemble.casino.game.specification.GameConfigurationKeyAware;
import com.clemble.casino.player.PlayerAware;

public interface GameSessionPlayer<State extends GameState> extends GameSessionAware, PlayerAware, Closeable, GameConfigurationKeyAware {

    public ClembleCasinoOperations playerOperations();

    public GameConstruction getConstructionInfo();

    public State getState();

    public boolean isAlive();

    public void syncWith(GameSessionPlayer<State> anotherState);

    public List<GameSessionAwareEvent> getEvents();

    public void waitForEnd();

    public void waitForStart();

    public void waitForStart(long timeout);

    public void waitForTurn();

    public int getVersion();

    public void waitVersion(int version);

    public boolean isToMove();

    public Event getNextMove();

    public void perform(GameAction gameAction);

    public GameOutcome getOutcome();

    public void giveUp();

    public void close();

    public void addDependent(GameSessionPlayer<State> dependent);

    public void addDependent(Collection<GameSessionPlayer<State>> dependent);

}
