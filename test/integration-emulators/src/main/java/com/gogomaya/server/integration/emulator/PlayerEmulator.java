package com.gogomaya.server.integration.emulator;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.atomic.AtomicBoolean;

import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.integration.game.GameOperations;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.integration.player.PlayerOperations;

public class PlayerEmulator<State extends GameState> implements Runnable {

    final private GameSpecification specification;

    final private PlayerOperations playerOperations;

    final private GameOperations<State> gameOperations;

    final private AtomicBoolean continueEmulation = new AtomicBoolean(true);

    public PlayerEmulator(final PlayerOperations playerOperations, final GameOperations<State> gameOperations, final GameSpecification specification) {
        this.specification = checkNotNull(specification);
        this.playerOperations = checkNotNull(playerOperations);
        this.gameOperations = checkNotNull(gameOperations);
    }

    public GameSpecification getSpecification() {
        return specification;
    }

    @Override
    public void run() {
        while (continueEmulation.get()) {
            // Step 1. Creating new player
            Player player = playerOperations.createPlayer();
            // Step 2. Starting a game
            
        }
    }

    public boolean isAlive() {
        return true;
    }

    public void stop() {
        continueEmulation.set(false);
    }
}
