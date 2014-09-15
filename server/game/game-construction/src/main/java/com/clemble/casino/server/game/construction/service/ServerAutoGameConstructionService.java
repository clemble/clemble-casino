package com.clemble.casino.server.game.construction.service;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.construction.AutomaticGameRequest;
import com.clemble.casino.game.construction.GameConstruction;
import com.clemble.casino.game.construction.GameInitiation;
import com.clemble.casino.game.construction.service.AutoGameConstructionService;
import com.clemble.casino.player.PlayerPresence;
import com.clemble.casino.player.Presence;
import com.clemble.casino.server.event.game.SystemGameReadyEvent;
import com.clemble.casino.server.game.construction.GameSessionKeyGenerator;
import com.clemble.casino.server.game.construction.repository.GameConstructionRepository;
import com.clemble.casino.server.player.lock.PlayerLockService;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.player.presence.ServerPlayerPresenceService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class ServerAutoGameConstructionService implements AutoGameConstructionService {

    final private LoadingCache<String, Queue<GameConstruction>> PENDING_CONSTRUCTIONS = CacheBuilder.newBuilder().build(
            new CacheLoader<String, Queue<GameConstruction>>() {
                @Override
                public Queue<GameConstruction> load(String key) throws Exception {
                    return new ArrayBlockingQueue<GameConstruction>(100);
                }
            });

    final private Map<String, GameConstruction> playerConstructions = new ConcurrentHashMap<>();

    final private GameSessionKeyGenerator sessionKeyGenerator;

    final private GameConstructionRepository constructionRepository;
    final private SystemNotificationService notificationService;

    final private PlayerLockService playerLockService;
    final private ServerPlayerPresenceService playerStateManager;

    public ServerAutoGameConstructionService(
            final GameSessionKeyGenerator sessionKeyGenerator,
            final SystemNotificationService initiatorService,
            final GameConstructionRepository constructionRepository,
            final PlayerLockService playerLockService,
            final ServerPlayerPresenceService playerStateManager) {
        this.sessionKeyGenerator = checkNotNull(sessionKeyGenerator);
        this.notificationService = checkNotNull(initiatorService);
        this.constructionRepository = checkNotNull(constructionRepository);
        this.playerLockService = checkNotNull(playerLockService);
        this.playerStateManager = checkNotNull(playerStateManager);
    }

    @Override
    public GameConstruction construct(AutomaticGameRequest request) {
        throw new UnsupportedOperationException();
    }

    public GameConstruction construct(String player, AutomaticGameRequest request) {
        // Step 1. Sanity check
        if (request == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameConstructionInvalidState);
        PlayerPresence playerPresence = playerStateManager.getPresence(player);
        if (playerPresence.getPresence() == Presence.playing) {
            GameConstruction activeConstruction = constructionRepository.findOne(playerPresence.getSessionKey());
            if (activeConstruction != null)
                return activeConstruction;
        }
        // Step 2. Fetching associated Queue
        playerLockService.lock(player);
        GameConstruction pendingConstuction;
        try {
            pendingConstuction = playerConstructions.get(player);
            // Step 2.1 Check there is no pending constructions for the user
            if (pendingConstuction != null)
                return pendingConstuction;
            // Step 2.2. Acquire and process Queue
            Queue<GameConstruction> specificationQueue = null;
            try {
                String constructionKey = request.getConfiguration().getConfigurationKey();
                specificationQueue = PENDING_CONSTRUCTIONS.get(constructionKey);
            } catch (ExecutionException e) {
                throw ClembleCasinoException.fromError(ClembleCasinoError.ServerCacheError);
            }
            // Step 3. Processing request
            pendingConstuction = specificationQueue.poll();
            if (pendingConstuction == null) {
                // Step 3.1 Construction was not present, creating new one
                pendingConstuction = request.toConstruction(player, sessionKeyGenerator.generate(request.getConfiguration()));
                pendingConstuction = constructionRepository.save(pendingConstuction);

                playerConstructions.put(player, pendingConstuction);
                specificationQueue.add(pendingConstuction);
            } else {
                // Step 3.2 Construction was present appending to existing one
                pendingConstuction.getParticipants().add(player);
                // Step 3.3 If number rule satisfied process further
                if (pendingConstuction.getParticipants().size() >= pendingConstuction.getConfiguration().getNumberRule().getMinPlayers()) {
                    GameInitiation initiation = new GameInitiation(pendingConstuction.getSessionKey(), pendingConstuction.getParticipants(), pendingConstuction.getConfiguration());
                    notificationService.notify(new SystemGameReadyEvent(initiation));
                    for (String participant : initiation.getParticipants())
                        playerConstructions.remove(participant);
                } else {
                    playerConstructions.put(player, pendingConstuction);
                    specificationQueue.add(pendingConstuction);
                }
            }
        } finally {
            playerLockService.unlock(player);
        }
        return pendingConstuction;
    }

    public Collection<GameConstruction> getPending(String player) {
        // Step 1. Fetching AutomaticGameConstruction
        GameConstruction construction = playerConstructions.get(player);
        if(construction == null)
            return Collections.emptyList();
        // Step 2. Extracting GameConstruction and returning as singleton
        return Collections.singletonList(construction);
    }

}
