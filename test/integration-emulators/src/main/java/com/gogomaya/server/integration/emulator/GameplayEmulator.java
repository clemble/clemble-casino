package com.gogomaya.server.integration.emulator;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.configuration.GameSpecificationOptions;
import com.gogomaya.server.game.configuration.SelectSpecificationOptions;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.integration.game.GameOperations;

public class GameplayEmulator<State extends GameState> {

    final private GameOperations<State> gameOperations;

    final private GameActor<State> actor;

    final private Map<GameSpecification, Collection<PlayerEmulator<State>>> playerEmulators = new HashMap<GameSpecification, Collection<PlayerEmulator<State>>>();

    private ScheduledExecutorService executorService;

    public GameplayEmulator(final GameOperations<State> gameOperations, final GameActor<State> gameActor) {
        this.gameOperations = checkNotNull(gameOperations);
        this.actor = gameActor;
    }

    public void emulate() {
        List<GameSpecification> specifications = new ArrayList<GameSpecification>();
        // Step 1. Fetching specification options for the game
        GameSpecificationOptions specificatinOptions = gameOperations.getOptions();
        if (specificatinOptions instanceof SelectSpecificationOptions) {
            SelectSpecificationOptions selectSpecificationOptions = (SelectSpecificationOptions) specificatinOptions;
            // Step 1.1 Adding all possible specifications to the list of GameSpecifications
            specifications.addAll(selectSpecificationOptions.getSpecifications());
        } else {
            throw new UnsupportedOperationException("This kind of specification not supported");
        }
        // Step 2. Creating Players to emulate gaming
        if (specifications.size() == 0)
            throw new RuntimeException("Specification list is empty check your configurations");
        // Step 3. For each specification creating player emulator
        executorService = Executors.newScheduledThreadPool(specifications.size() + 1);
        for (GameSpecification specification : specifications) {
            playerEmulators.put(specification, new ArrayList<PlayerEmulator<State>>());
            createEmulator(specification);
        }
        // Step 4. Creating executor to run all emulators separately
        // executorService.scheduleAtFixedRate(new PlayerEmulatorManager(), 15, 15, TimeUnit.SECONDS);
    }

    private void createEmulator(GameSpecification specification) {
        PlayerEmulator<State> playerEmulator = new PlayerEmulator<State>(actor, gameOperations, specification);
        playerEmulators.get(specification).add(playerEmulator);
        executorService.submit(playerEmulator);
    }

    private class PlayerEmulatorManager implements Runnable {

        @Override
        public void run() {
            for(GameSpecification specification: playerEmulators.keySet()) {
                List<PlayerEmulator<State>> damagedEmulators = new ArrayList<PlayerEmulator<State>>();
                // Step 1. Checking all emulators from existing player emulators
                // Step 1.1. Check if there is no pending users
                boolean hasWaiting = false;
                for (PlayerEmulator<State> emulator : playerEmulators.get(specification)) {
                    if (!emulator.isAlive()) {
                        emulator.stop();
                        damagedEmulators.add(emulator);
                    }
                    hasWaiting = hasWaiting || !emulator.isActive();
                }
                // Step 2. Removing all playerEmulators from the list
                playerEmulators.get(specification).removeAll(damagedEmulators);
                // Step 3. Creating new PlayerEmulators for the damaged specification
                for (PlayerEmulator<State> emulator : damagedEmulators) {
                    createEmulator(emulator.getSpecification());
                }
                // Step 4. If there is no pending or damaged users create one
                if(hasWaiting && damagedEmulators.size() == 0)
                    createEmulator(specification);
            }
        }

    }
}
