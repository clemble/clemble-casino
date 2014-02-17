package com.clemble.casino.integration.emulator;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.PreDestroy;

import com.clemble.casino.game.GameState;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.integration.game.MatchGamePlayerFactory;
import com.clemble.casino.integration.game.construction.PlayerScenarios;

public class GameplayEmulator<State extends GameState> {

    final private PlayerScenarios playerOperations;
    final private MatchGamePlayerFactory<State> sessionPlayerFactory;
    final private GameActor<State> actor;
    final private Map<MatchGameConfiguration, Collection<PlayerEmulator<State>>> playerEmulators = new HashMap<MatchGameConfiguration, Collection<PlayerEmulator<State>>>();

    private ScheduledExecutorService executorService;

    public GameplayEmulator(final PlayerScenarios playerOperations, final GameActor<State> gameActor, final MatchGamePlayerFactory<State> sessionPlayerFactory) {
        this.actor = checkNotNull(gameActor);
        this.playerOperations = checkNotNull(playerOperations);
        this.sessionPlayerFactory = checkNotNull(sessionPlayerFactory);
    }

    @PreDestroy
    public void clean() {
        System.out.println("Cleaning Emulators");
        for (MatchGameConfiguration specification : playerEmulators.keySet()) {
            for (PlayerEmulator<State> emulator : playerEmulators.get(specification)) {
                try {
                    emulator.stop();
                } catch (Throwable ignore) {
                }
            }
        }
    }

    public void emulate() {
        // Step 1. Fetching specification options for the game
        List<MatchGameConfiguration> specifications = playerOperations.createPlayer().gameConstructionOperations().getConfigurations().matchConfigurations();
        // Step 2. Creating Players to emulate gaming
        if (specifications.size() == 0)
            throw new RuntimeException("Specification list is empty check your configurations");
        // Step 3. For each specification creating player emulator
        executorService = Executors.newScheduledThreadPool(specifications.size() + 1);
        for (MatchGameConfiguration specification : specifications) {
            playerEmulators.put(specification, new ArrayList<PlayerEmulator<State>>());
            createEmulator(specification);
        }
        // Step 4. Creating executor to run all emulators separately
        // executorService.scheduleAtFixedRate(new PlayerEmulatorManager(), 30, 30, TimeUnit.SECONDS);
    }

    private void createEmulator(MatchGameConfiguration specification) {
        PlayerEmulator<State> playerEmulator = new PlayerEmulator<State>(actor, playerOperations, sessionPlayerFactory, specification);
        playerEmulators.get(specification).add(playerEmulator);
        executorService.submit(playerEmulator);
    }

    public class PlayerEmulatorManager implements Runnable {

        @Override
        public void run() {
            for (MatchGameConfiguration specification : playerEmulators.keySet()) {
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
