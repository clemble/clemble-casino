package com.clemble.casino.integration.game;

import java.io.Closeable;
import java.util.Collection;
import java.util.List;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.lifecycle.management.outcome.Outcome;
import com.clemble.casino.game.lifecycle.record.GameRecord;
import com.clemble.casino.game.GameSessionAware;
import com.clemble.casino.game.lifecycle.configuration.GameConfigurationAware;
import com.clemble.casino.game.event.GameEvent;
import com.clemble.casino.game.lifecycle.configuration.GameConfiguration;
import com.clemble.casino.player.PlayerAware;

public interface GamePlayer extends GameSessionAware, PlayerAware, Closeable, GameConfigurationAware {

    public ClembleCasinoOperations playerOperations();

    public boolean isAlive();

    public GamePlayer syncWith(GamePlayer anotherState);

    public List<GameEvent> getEvents();

    public GameConfiguration getConfiguration();

    public GamePlayer waitForEnd();

    public GamePlayer waitForEnd(long timeout);

    public GamePlayer waitForStart();

    public GamePlayer waitForStart(long timeout);

    public int getVersion();

    public GamePlayer waitVersion(int version);

    public Outcome getOutcome();

    public GamePlayer giveUp();

    public void close();

    public GameRecord getRecord();

    public GamePlayer addDependent(GamePlayer dependent);

    public GamePlayer addDependent(Collection<? extends GamePlayer> dependent);

}
