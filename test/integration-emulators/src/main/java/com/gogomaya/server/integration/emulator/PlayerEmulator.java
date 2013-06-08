package com.gogomaya.server.integration.emulator;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gogomaya.server.game.action.GameSessionState;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.integration.game.GameOperations;
import com.gogomaya.server.integration.game.GamePlayer;

public class PlayerEmulator<State extends GameState> implements Runnable {

    final private Logger logger = LoggerFactory.getLogger(PlayerEmulator.class);

    final private GameSpecification specification;

    final private GameOperations<State> gameOperations;

    final private GameActor<State> actor;

    final private AtomicBoolean continueEmulation = new AtomicBoolean(true);

    final private AtomicLong lastMoved = new AtomicLong();

    final private AtomicReference<GamePlayer<State>> currentPlayer = new AtomicReference<GamePlayer<State>>();

    public PlayerEmulator(final GameActor<State> actor, final GameOperations<State> gameOperations, final GameSpecification specification) {
        this.specification = checkNotNull(specification);
        this.gameOperations = checkNotNull(gameOperations);
        this.actor = checkNotNull(actor);
    }

    public GameSpecification getSpecification() {
        return specification;
    }

    @Override
    public void run() {
        while (continueEmulation.get()) {
            try {
                // Step 1. Start player emulator
                GamePlayer<State> playerState = gameOperations.construct(specification);
                logger.info("Registered {} on {} with session {}", new Object[] { playerState.getPlayer().getPlayerId(), playerState.getTable().getTableId(),
                        playerState.getSessionId() });
                currentPlayer.set(playerState);
                lastMoved.set(System.currentTimeMillis());
                while (!playerState.getState().complete()) {
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
        GameSessionState sessionState = currentPlayer.get().getTable().getCurrentSession().getSessionState();
        if (sessionState == GameSessionState.inactive)
            return true;
        if (sessionState == GameSessionState.ended)
            return false;
        return lastMoved.get() + TimeUnit.MINUTES.toMillis(15) < System.currentTimeMillis();
    }

    public void stop() {
        this.continueEmulation.set(false);
        this.currentPlayer.get().clear();
    }

    public long getLastMoved() {
        return lastMoved.get();
    }
}
