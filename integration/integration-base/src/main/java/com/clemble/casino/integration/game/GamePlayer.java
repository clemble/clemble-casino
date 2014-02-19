package com.clemble.casino.integration.game;

import java.io.Closeable;
import java.util.Collection;
import java.util.List;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.game.GameSessionAware;
import com.clemble.casino.game.GameSessionAwareEvent;
import com.clemble.casino.game.outcome.GameOutcome;
import com.clemble.casino.game.specification.GameConfigurationKeyAware;
import com.clemble.casino.player.PlayerAware;

public interface GamePlayer extends GameSessionAware, PlayerAware, Closeable, GameConfigurationKeyAware {

    public ClembleCasinoOperations playerOperations();

    public boolean isAlive();

    public void syncWith(GamePlayer anotherState);

    public List<GameSessionAwareEvent> getEvents();

    public void waitForEnd();

    public void waitForStart();

    public void waitForStart(long timeout);

    public int getVersion();

    public void waitVersion(int version);

    public GameOutcome getOutcome();

    public void giveUp();

    public void close();

    public void addDependent(GamePlayer dependent);

    public void addDependent(Collection<? extends GamePlayer> dependent);

}
