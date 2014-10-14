package com.clemble.casino.server.game.action;

import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.OptimisticLockException;

import com.clemble.casino.game.lifecycle.initiation.GameInitiation;
import com.clemble.casino.game.lifecycle.management.*;
import com.clemble.casino.game.lifecycle.management.event.GameManagementEvent;
import com.clemble.casino.game.lifecycle.configuration.MatchGameConfiguration;
import com.clemble.casino.game.lifecycle.configuration.RoundGameConfiguration;
import com.clemble.casino.game.lifecycle.configuration.TournamentGameConfiguration;
import com.clemble.casino.server.action.ClembleManager;
import com.clemble.casino.server.action.ClembleManagerFactory;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.game.repository.GameRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameManagerFactoryFacade {

    final private Logger LOG = LoggerFactory.getLogger(GameManagerFactoryFacade.class);

    final private ConcurrentHashMap<String, ClembleManager<GameManagementEvent, ? extends GameState>> sessionToManager = new ConcurrentHashMap<>();

    final private RoundManagerFactory roundManagerFactory;
    final private MatchManagerFactory matchManagerFactory;
    final private TournamentManagerFactory tournamentManagerFactory;

    public GameManagerFactoryFacade(
        RoundStateFactoryFacade stateFactory,
        ClembleManagerFactory<RoundGameConfiguration> roundGameManagerFactory,
        ClembleManagerFactory<MatchGameConfiguration> matchGameManagerFactory,
        ClembleManagerFactory<TournamentGameConfiguration> tournamentGameManagerFactory,
        GameRecordRepository recordRepository,
        PlayerNotificationService notificationService) {
        this.roundManagerFactory = new RoundManagerFactory(stateFactory, roundGameManagerFactory, recordRepository, notificationService);
        this.matchManagerFactory = new MatchManagerFactory(this, stateFactory, matchGameManagerFactory, recordRepository, notificationService);
        this.tournamentManagerFactory = new TournamentManagerFactory();
    }

    @SuppressWarnings("unchecked")
    public <GC extends GameContext> ClembleManager<GameManagementEvent, ? extends GameState> get(String sessionKey) {
        ClembleManager<GameManagementEvent, ? extends GameState> gameManager = sessionToManager.get(sessionKey);
        if(gameManager == null)
            LOG.warn("{} can't find in sessionToManager mapping {}", sessionKey, hashCode());
        return gameManager;
    }

    public ClembleManager<GameManagementEvent, ?> start(GameInitiation initiation, GameContext<?> parent) {
        try {
            LOG.debug("{} starting", initiation.getSessionKey());
            ClembleManager<GameManagementEvent, ? extends GameState> manager;
            if (initiation.getConfiguration() instanceof RoundGameConfiguration) {
                manager = roundManagerFactory.start(initiation, parent);
            } else if (initiation.getConfiguration() instanceof MatchGameConfiguration) {
                manager = matchManagerFactory.start(initiation, parent);
            } else if (initiation.getConfiguration() instanceof TournamentGameConfiguration) {
                manager = tournamentManagerFactory.start(initiation, parent);
            } else {
                throw new IllegalArgumentException();
            }
            LOG.debug("{} adding new manager", initiation.getSessionKey(), manager);
            sessionToManager.put(initiation.getSessionKey(), manager);
            LOG.debug("{} starting  manager", initiation.getSessionKey(), manager);
            manager.start();
            return manager;
        } catch (OptimisticLockException lockException) {
            // TODO Be really careful with this shit
            return start(initiation, parent);
        }
    }

}
