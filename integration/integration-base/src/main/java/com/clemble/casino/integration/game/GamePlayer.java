package com.clemble.casino.integration.game;

import java.io.Closeable;
import java.util.Collection;
import java.util.List;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.game.GameSessionAware;
import com.clemble.casino.game.GameSessionAwareEvent;
import com.clemble.casino.game.outcome.GameOutcome;
import com.clemble.casino.game.specification.GameConfiguration;
import com.clemble.casino.game.specification.GameConfigurationKeyAware;
import com.clemble.casino.player.PlayerAware;

public interface GamePlayer extends GameSessionAware, PlayerAware, Closeable, GameConfigurationKeyAware {

    public ClembleCasinoOperations playerOperations();

    public boolean isAlive();

    public GamePlayer syncWith(GamePlayer anotherState);

    public List<GameSessionAwareEvent> getEvents();

    public GameConfiguration getConfiguration();

    public GamePlayer waitForEnd();

    public GamePlayer waitForEnd(long timeout);

    public GamePlayer waitForStart();

    public GamePlayer waitForStart(long timeout);

    public int getVersion();

    public GamePlayer waitVersion(int version);

    public GameOutcome getOutcome();

    public GamePlayer giveUp();

    public void close();

    public GamePlayer addDependent(GamePlayer dependent);

    public GamePlayer addDependent(Collection<? extends GamePlayer> dependent);

}
