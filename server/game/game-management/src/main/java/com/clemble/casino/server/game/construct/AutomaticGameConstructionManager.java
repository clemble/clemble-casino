package com.clemble.casino.server.game.construct;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.construct.AutomaticGameRequest;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.specification.GameSpecification;
import com.clemble.casino.game.specification.GameSpecificationKey;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.player.PlayerPresence;
import com.clemble.casino.player.Presence;
import com.clemble.casino.server.player.lock.PlayerLockService;
import com.clemble.casino.server.player.presence.ServerPlayerPresenceService;
import com.clemble.casino.server.repository.game.GameConstructionRepository;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class AutomaticGameConstructionManager implements GameConstructionManager<AutomaticGameRequest> {

    final public static class AutomaticGameConstruction {
        final private GameConstruction construction;
        final private GameSpecification specification;
        final private List<String> participants = new ArrayList<>();

        public AutomaticGameConstruction(GameConstruction construction) {
            this.construction = construction;
            this.specification = construction.getRequest().getSpecification();
            this.participants.add(((AutomaticGameRequest) construction.getRequest()).getPlayer());
        }

        public GameConstruction getConstruction() {
            return construction;
        }

        public boolean append(AutomaticGameRequest request) {
            if (request != null) {
                participants.add(request.getPlayer());
            }
            return participants.size() >= specification.getNumberRule().getMinPlayers();
        }

        public boolean ready() {
            return specification.getNumberRule().getMinPlayers() <= participants.size();
        }

        public GameInitiation toInitiation() {
            // Step 1. Creating instant game request
            return new GameInitiation(construction.getSession(), participants, specification);
        }
    }

    final private LoadingCache<GameSpecificationKey, Queue<AutomaticGameConstruction>> PENDING_CONSTRUCTIONS = CacheBuilder.newBuilder().build(
            new CacheLoader<GameSpecificationKey, Queue<AutomaticGameConstruction>>() {
                @Override
                public Queue<AutomaticGameConstruction> load(GameSpecificationKey key) throws Exception {
                    return new ArrayBlockingQueue<AutomaticGameConstruction>(100);
                }
            });

    final private Map<String, AutomaticGameConstruction> playerConstructions = new ConcurrentHashMap<>();

    final private GameConstructionRepository constructionRepository;
    final private ServerGameInitiationService initiatorService;

    final private PlayerLockService playerLockService;
    final private ServerPlayerPresenceService playerStateManager;

    public AutomaticGameConstructionManager(
            final ServerGameInitiationService initiatorService,
            final GameConstructionRepository constructionRepository,
            final PlayerLockService playerLockService,
            final ServerPlayerPresenceService playerStateManager) {
        this.initiatorService = checkNotNull(initiatorService);
        this.constructionRepository = checkNotNull(constructionRepository);
        this.playerLockService = checkNotNull(playerLockService);
        this.playerStateManager = checkNotNull(playerStateManager);
    }

    @Override
    public GameConstruction register(AutomaticGameRequest request, String id) {
        // Step 1. Sanity check
        if (request == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameConstructionInvalidState);
        PlayerPresence playerPresence = playerStateManager.getPresence(request.getPlayer());
        if (playerPresence.getPresence() == Presence.playing) {
            GameConstruction activeConstruction = constructionRepository.findOne(playerPresence.getSession());
            if (activeConstruction != null)
                return activeConstruction;
        }
        // Step 2. Fetching associated Queue
        String player = request.getPlayer();
        playerLockService.lock(player);
        AutomaticGameConstruction pendingConstuction;
        try {
            pendingConstuction = playerConstructions.get(player);
            // Step 2.1 Check there is no pending constructions for the user
            if (pendingConstuction != null)
                return pendingConstuction.getConstruction();
            // Step 2.2. Acquire and process Queue
            Queue<AutomaticGameConstruction> specificationQueue = null;
            try {
                GameSpecificationKey constructionKey = request.getSpecification().getName();
                specificationQueue = PENDING_CONSTRUCTIONS.get(constructionKey);
            } catch (ExecutionException e) {
                throw ClembleCasinoException.fromError(ClembleCasinoError.ServerCacheError);
            }
            // Step 3. Processing request
            pendingConstuction = specificationQueue.poll();
            if (pendingConstuction == null) {
                // Step 3.1 Construction was not present, creating new one
                GameConstruction construction = new GameConstruction(request);
                construction.setSession(new GameSessionKey(request.getSpecification().getName().getGame(), id));
                construction = constructionRepository.saveAndFlush(construction);

                pendingConstuction = new AutomaticGameConstruction(construction);
                playerConstructions.put(player, pendingConstuction);
                specificationQueue.add(pendingConstuction);
            } else {
                // Step 3.2 Construction was present appending to existing one
                if (pendingConstuction.append(request)) {
                    GameInitiation initiation = pendingConstuction.toInitiation();
                    initiatorService.start(initiation);
                    for (PlayerAware participant : initiation.getParticipants())
                        playerConstructions.remove(participant.getPlayer());
                } else {
                    playerConstructions.put(player, pendingConstuction);
                    specificationQueue.add(pendingConstuction);
                }
            }
        } finally {
            playerLockService.unlock(player);
        }
        return pendingConstuction.getConstruction();
    }
}
