package com.gogomaya.server.integration.emulator;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.PreDestroy;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.configuration.GameSpecificationOptions;
import com.gogomaya.server.game.configuration.SelectSpecificationOptions;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.integration.game.construction.GameConstructionOperations;
import com.gogomaya.server.integration.player.PlayerOperations;

public class GameplayEmulator<State extends GameState> {

    final private GameConstructionOperations<State> gameOperations;

    final private PlayerOperations playerOperations;

    final private GameActor<State> actor;

    final private Map<GameSpecification, Collection<PlayerEmulator<State>>> playerEmulators = new HashMap<GameSpecification, Collection<PlayerEmulator<State>>>();

    private ScheduledExecutorService executorService;

    public GameplayEmulator(final PlayerOperations playerOperations, final GameConstructionOperations<State> gameOperations, final GameActor<State> gameActor) {
        this.gameOperations = checkNotNull(gameOperations);
        this.actor = checkNotNull(gameActor);
        this.playerOperations = checkNotNull(playerOperations);
    }

    @PreDestroy
    public void clean() {
        System.out.println("Cleaning Emulators");
        for (GameSpecification specification : playerEmulators.keySet()) {
            for (PlayerEmulator<State> emulator : playerEmulators.get(specification)) {
                try {
                    emulator.stop();
                } catch (Throwable ignore) {
                }
            }
        }
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
        // executorService.scheduleAtFixedRate(new PlayerEmulatorManager(), 30, 30, TimeUnit.SECONDS);
    }

    private void createEmulator(GameSpecification specification) {
        PlayerEmulator<State> playerEmulator = new PlayerEmulator<State>(actor, playerOperations, gameOperations, specification);
        playerEmulators.get(specification).add(playerEmulator);
        executorService.submit(playerEmulator);
    }

    public class PlayerEmulatorManager implements Runnable {

        @Override
        public void run() {
            for (GameSpecification specification : playerEmulators.keySet()) {
                List<PlayerEmulator<State>> emulatorsToRelease = new ArrayList<PlayerEmulator<State>>();
                // Step 1. Checking all emulators from existing player emulators
                // Step 1.1. Check if there is no pending users
                for (PlayerEmulator<State> emulator : playerEmulators.get(specification)) {
                    if (!emulator.isAlive()) {
                        emulator.stop();
                        emulatorsToRelease.add(emulator);
                    }
                }
                // Step 2. Removing all playerEmulators from the list
                playerEmulators.get(specification).removeAll(emulatorsToRelease);
                // Step 3. Creating new PlayerEmulators for the damaged specification
                for (PlayerEmulator<State> emulator : emulatorsToRelease) {
                    createEmulator(emulator.getSpecification());
                }
            }
        }

    }
}
