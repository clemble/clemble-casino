package com.gogomaya.server.game.construct;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.GameTable;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.specification.SpecificationName;
import com.gogomaya.server.player.lock.PlayerLockService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class AutomaticGameInitiatorManager {

    final public static class AutomaticGameConstruction {
        final private GameConstruction construction;
        final private GameSpecification specification;
        final private LinkedHashSet<Long> participants = new LinkedHashSet<Long>();

        public AutomaticGameConstruction(GameConstruction construction) {
            this.construction = construction;
            this.specification = construction.getRequest().getSpecification();
            this.participants.add(construction.getRequest().getPlayerId());
        }

        public GameConstruction getConstruction() {
            return construction;
        }

        public boolean append(AutomaticGameRequest request) {
            if (request != null) {
                participants.add(request.getPlayerId());
            }
            return participants.size() >= specification.getNumberRule().getMinPlayers();
        }

        public boolean ready() {
            return specification.getNumberRule().getMinPlayers() <= participants.size();
        }

        public GameInitiation toInitiation() {
            // Step 1. Creating instant game request
            return new GameInitiation(construction.getConstruction(), participants, specification);
        }
    }

    final private LoadingCache<SpecificationName, Queue<AutomaticGameConstruction>> PENDING_CONSTRUCTIONS = CacheBuilder.newBuilder().build(
            new CacheLoader<SpecificationName, Queue<AutomaticGameConstruction>>() {
                @Override
                public Queue<AutomaticGameConstruction> load(SpecificationName key) throws Exception {
                    return new ArrayBlockingQueue<AutomaticGameConstruction>(100);
                }
            });

    final private Map<Long, AutomaticGameConstruction> playerConstructions = new ConcurrentHashMap<>();

    final private GameInitiatorService initiatorService;
    final private GameConstructionRepository constructionRepository;
    final private PlayerLockService playerLockService;

    public AutomaticGameInitiatorManager(final GameInitiatorService initiatorService, final GameConstructionRepository constructionRepository, final PlayerLockService playerLockService) {
        this.initiatorService = checkNotNull(initiatorService);
        this.constructionRepository = checkNotNull(constructionRepository);
        this.playerLockService = checkNotNull(playerLockService);
    }

    public GameConstruction register(AutomaticGameRequest request) {
        // Step 1. Sanity check
        if (request == null)
            throw GogomayaException.fromError(GogomayaError.GameConstructionInvalidState);
        // Step 2. Fetching associated Queue
        long player = request.getPlayerId();
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
                SpecificationName constructionKey = request.getSpecification().getName();
                specificationQueue = PENDING_CONSTRUCTIONS.get(constructionKey);
            } catch (ExecutionException e) {
                throw GogomayaException.fromError(GogomayaError.ServerCacheError);
            }
            // Step 3. Processing request
            pendingConstuction = specificationQueue.poll();
            if (pendingConstuction == null) {
                // Step 3.1 Construction was not present, creating new one
                GameConstruction construction = constructionRepository.saveAndFlush(new GameConstruction(request));

                pendingConstuction = new AutomaticGameConstruction(construction);
                playerConstructions.put(player, pendingConstuction);
                specificationQueue.add(pendingConstuction);
            } else {
                // Step 3.2 Construction was present appending to existing one
                if (pendingConstuction.append(request)) {
                    GameInitiation initiation = pendingConstuction.toInitiation();
                    playerLockService.lock(initiation.getParticipants());
                    try {
                        GameTable<?> table = initiatorService.initiate(initiation);
                        pendingConstuction.getConstruction().setSession(table.getCurrentSession().getSession());

                        for (Long participant : initiation.getParticipants())
                            playerConstructions.remove(participant);
                    } finally {
                        playerLockService.unlock(initiation.getParticipants());
                    }
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
