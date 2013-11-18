package com.clemble.casino.integration.emulator;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.specification.GameSpecification;
import com.clemble.casino.integration.game.GameSessionPlayer;
import com.clemble.casino.integration.game.GameSessionPlayerFactory;
import com.clemble.casino.integration.player.Player;
import com.clemble.casino.integration.player.PlayerOperations;

public class PlayerEmulator<State extends GameState> implements Runnable {

    final private Logger logger = LoggerFactory.getLogger(PlayerEmulator.class);

    final private GameSpecification specification;
    final private PlayerOperations playerOperations;
    final private GameSessionPlayerFactory sessionPlayerFactory;
    final private GameActor<State> actor;
    final private AtomicBoolean continueEmulation = new AtomicBoolean(true);
    final private AtomicLong lastMoved = new AtomicLong();
    final private AtomicReference<GameSessionPlayer<State>> currentPlayer = new AtomicReference<GameSessionPlayer<State>>();

    public PlayerEmulator(final GameActor<State> actor,
            final PlayerOperations playerOperations,
            final GameSessionPlayerFactory sessionPlayerFactory,
            final GameSpecification specification) {
        this.specification = checkNotNull(specification);
        this.actor = checkNotNull(actor);
        this.sessionPlayerFactory = checkNotNull(sessionPlayerFactory);
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
                GameConstruction playerConstruction = player.getGameConstructor(actor.getGame()).constructAutomatch(specification);
                GameSessionPlayer<State> playerState = sessionPlayerFactory.construct(player, playerConstruction);
                logger.info("Registered {} with construction {} ", playerState.getPlayer(), playerState.getSession());
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
