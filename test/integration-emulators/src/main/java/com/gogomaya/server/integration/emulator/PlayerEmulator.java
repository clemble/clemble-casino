package com.gogomaya.server.integration.emulator;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.integration.game.GameSessionPlayer;
import com.gogomaya.server.integration.game.construction.GameConstructionOperations;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.integration.player.PlayerOperations;

public class PlayerEmulator<State extends GameState> implements Runnable {

    final private Logger logger = LoggerFactory.getLogger(PlayerEmulator.class);

    final private GameSpecification specification;

    final private PlayerOperations playerOperations;

    final private GameConstructionOperations<State> gameOperations;

    final private GameActor<State> actor;

    final private AtomicBoolean continueEmulation = new AtomicBoolean(true);

    final private AtomicLong lastMoved = new AtomicLong();

    final private AtomicReference<GameSessionPlayer<State>> currentPlayer = new AtomicReference<GameSessionPlayer<State>>();

    public PlayerEmulator(final GameActor<State> actor, final PlayerOperations playerOperations, final GameConstructionOperations<State> gameOperations,
            final GameSpecification specification) {
        this.specification = checkNotNull(specification);
        this.gameOperations = checkNotNull(gameOperations);
        this.actor = checkNotNull(actor);
        this.playerOperations = checkNotNull(playerOperations);
    }

    public GameSpecification getSpecification() {
        return specification;
    }

    @Override
    public void run() {
        while (continueEmulation.get()) {
            try {
                Player player = playerOperations.createPlayer();
                // Step 1. Start player emulator
                GameSessionPlayer<State> playerState = gameOperations.constructAutomatic(player, specification);
                logger.info("Registered {} with construction {} ", playerState.getPlayerId(), playerState.getSession());
                currentPlayer.set(playerState);
                lastMoved.set(System.currentTimeMillis());
                while (playerState.getState().getOutcome() == null) {
                    // Step 2. Waiting for player turn
                    playerState.waitForTurn();
                    // Step 3. Performing action
                    if (playerState.isToMove())
                        actor.move(playerState);
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    public boolean isAlive() {
        Boolean isAlive = currentPlayer.get().isAlive();
        return (isAlive == null || isAlive) || (lastMoved.get() + TimeUnit.MINUTES.toMillis(15) < System.currentTimeMillis());
    }

    public void stop() {
        this.continueEmulation.set(false);
        this.currentPlayer.get().close();
    }

    public long getLastMoved() {
        return lastMoved.get();
    }
}
