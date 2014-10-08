package com.clemble.casino.integration.emulator;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import com.clemble.casino.game.lifecycle.configuration.GameConfiguration;
import com.clemble.casino.game.lifecycle.configuration.GameConfigurationAware;
import com.clemble.casino.integration.game.GamePlayer;
import com.clemble.casino.integration.game.construction.GameScenarios;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.integration.game.construction.PlayerScenarios;

public class PlayerEmulator implements Runnable, GameConfigurationAware {

    final private Logger LOG = LoggerFactory.getLogger(PlayerEmulator.class);

    final private GamePlayerActor actor;
    final private GameConfiguration specification;
    final private PlayerScenarios playerOperations;
    final private GameScenarios sessionPlayerFactory;
    final private AtomicBoolean continueEmulation = new AtomicBoolean(true);
    final private AtomicLong lastMoved = new AtomicLong();
    final private AtomicReference<GamePlayer> currentPlayer = new AtomicReference<GamePlayer>();

    public PlayerEmulator(
            final GamePlayerActor actor,
            final PlayerScenarios playerOperations,
            final GameScenarios sessionPlayerFactory,
            final GameConfiguration specification) {
        this.specification = checkNotNull(specification);
        this.actor = checkNotNull(actor);
        this.sessionPlayerFactory = checkNotNull(sessionPlayerFactory);
        this.playerOperations = checkNotNull(playerOperations);
    }

    @Override
    public GameConfiguration getConfiguration() {
        return specification;
    }

    @Override
    public void run() {
        while (continueEmulation.get()) {
            ClembleCasinoOperations player = null;
            try {
                player = playerOperations.createPlayer();
                // Step 1. Start player emulator
                GamePlayer playerState = sessionPlayerFactory.construct(specification, player);
                LOG.info("Registered {} with construction {} ", playerState.playerOperations(), playerState.getSessionKey());
                currentPlayer.set(playerState);
                lastMoved.set(System.currentTimeMillis());
                actor.play(playerState);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            } finally {
                player.close();
            }
        }
    }

    public boolean isAlive() {
        return currentPlayer.get().isAlive() || (lastMoved.get() + TimeUnit.MINUTES.toMillis(15) < System.currentTimeMillis());
    }

    public void stop() {
        this.continueEmulation.set(false);
        this.currentPlayer.get().close();
    }

    public long getLastMoved() {
        return lastMoved.get();
    }
}
