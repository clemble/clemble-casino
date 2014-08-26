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
import com.clemble.casino.game.configuration.GameConfiguration;
import com.clemble.casino.integration.game.GamePlayerFactory;
import com.clemble.casino.integration.game.construction.GameScenarios;
import com.clemble.casino.integration.game.construction.PlayerScenarios;

public class GamePlayerEmulator<State extends GameState> {

    final private PlayerScenarios playerOperations;
    final private GameScenarios gameScenarios;
    final private GamePlayerFactory sessionPlayerFactory;
    final private RoundGamePlayerActor<State> actor;
    final private Map<GameConfiguration, Collection<PlayerEmulator>> playerEmulators = new HashMap<GameConfiguration, Collection<PlayerEmulator>>();

    private ScheduledExecutorService executorService;

    public GamePlayerEmulator(final PlayerScenarios playerOperations, final GameScenarios gameScenarios, final RoundGamePlayerActor<State> gameActor, final GamePlayerFactory sessionPlayerFactory) {
        this.actor = checkNotNull(gameActor);
        this.gameScenarios = checkNotNull(gameScenarios);
        this.playerOperations = checkNotNull(playerOperations);
        this.sessionPlayerFactory = checkNotNull(sessionPlayerFactory);
    }

    @PreDestroy
    public void clean() {
        System.out.println("Cleaning Emulators");
        for (GameConfiguration specification : playerEmulators.keySet()) {
            for (PlayerEmulator emulator : playerEmulators.get(specification)) {
                try {
                    emulator.stop();
                } catch (Throwable ignore) {
                }
            }
        }
    }

    public void emulate() {
        // Step 1. Fetching specification options for the game
        List<GameConfiguration> specifications = playerOperations.createPlayer().gameConstructionOperations().getConfigurations().getConfigurations();
        // Step 2. Creating Players to emulate gaming
        if (specifications.size() == 0)
            throw new RuntimeException("Specification list is empty check your configurations");
        // Step 3. For each specification creating player emulator
        executorService = Executors.newScheduledThreadPool(specifications.size() + 1);
        for (GameConfiguration specification : specifications) {
            playerEmulators.put(specification, new ArrayList<PlayerEmulator>());
            createEmulator(specification);
        }
        // Step 4. Creating executor to run all emulators separately
        // executorService.scheduleAtFixedRate(new PlayerEmulatorManager(), 30, 30, TimeUnit.SECONDS);
    }

    private void createEmulator(GameConfiguration specification) {
        PlayerEmulator playerEmulator = new PlayerEmulator(actor, playerOperations, gameScenarios, specification);
        playerEmulators.get(specification).add(playerEmulator);
        executorService.submit(playerEmulator);
    }

    public class PlayerEmulatorManager implements Runnable {

        @Override
        public void run() {
            for (GameConfiguration specification : playerEmulators.keySet()) {
                List<PlayerEmulator> emulatorsToRelease = new ArrayList<PlayerEmulator>();
                // Step 1. Checking all emulators from existing player emulators
                // Step 1.1. Check if there is no pending users
                for (PlayerEmulator emulator : playerEmulators.get(specification)) {
                    if (!emulator.isAlive()) {
                        emulator.stop();
                        emulatorsToRelease.add(emulator);
                    }
                }
                // Step 2. Removing all playerEmulators from the list
                playerEmulators.get(specification).removeAll(emulatorsToRelease);
                // Step 3. Creating new PlayerEmulators for the damaged specification
                for (PlayerEmulator emulator : emulatorsToRelease) {
                    createEmulator(emulator.getConfiguration());
                }
            }
        }

    }
}
