package com.clemble.casino.server.game.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import com.clemble.casino.ImmutablePair;
import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.TournamentGameContext;
import com.clemble.casino.game.TournamentLeaf;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.event.server.GameEndedEvent;
import com.clemble.casino.game.event.server.GameManagementEvent;
import com.clemble.casino.game.event.server.TournamentEndedEvent;
import com.clemble.casino.game.outcome.GameOutcome;
import com.clemble.casino.game.outcome.PlayerWonOutcome;
import com.clemble.casino.game.specification.GameConfiguration;
import com.clemble.casino.game.specification.GameConfigurationAware;
import com.clemble.casino.game.specification.TournamentGameConfiguration;
import com.clemble.casino.game.unit.GameUnit;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.player.PlayerAwareUtils;

/**
 * Created by mavarazy on 12/02/14.
 */
public class TournamentGameState implements GameState<TournamentGameContext, Event> {

    final private String TOURNAME_SEPARATOR = "_";
    final private GameManagerFactory managerFactory;
    final private TournamentGameContext context;
    final private List<TournamentLevel> levels = new ArrayList<>();
    final private TournamentGameConfiguration configuration;

    public TournamentGameState(TournamentGameConfiguration configuration, TournamentGameContext context, GameManagerFactory managerFactory, List<String> players) {
        this.context = context;
        this.configuration = configuration;
        this.managerFactory = managerFactory;
        // Step 1. Calculating number of levels
        int numLevels = Double.valueOf(Math.log(players.size()) / Math.log(configuration.getNumberRule().getMinPlayers())).intValue();
        for(int i = 0; i < numLevels; i++)
            levels.add(new TournamentLevel(context.getSession().append(TOURNAME_SEPARATOR + i), configuration.getConfiguration(), context));
        TournamentLevel tournamentLevel = levels.get(levels.size() - 1);
        // Step 2. Deviding in groups
        int groupSize = configuration.getConfiguration().getNumberRule().getMinPlayers();
        for(int i = 0; i < players.size(); i++) {
            tournamentLevel.add(i / groupSize,new TournamentLeaf(players.get(i), context.getSession(), null));
        }
    }

    public TournamentGameContext getContext(){
        return context;
    }

    public GameManagementEvent process(Event event) {
        if (event instanceof GameEndedEvent) {
            // Step 0. Reading session key and outcome
            GameSessionKey sessionKey = ((GameEndedEvent) event).getContext().getSession();
            GameOutcome outcome = ((GameEndedEvent<?>) event).getOutcome();
            TournamentGameContext leafContext = (TournamentGameContext) ((GameEndedEvent<?>) event).getContext().getParent();
            if (outcome instanceof PlayerWonOutcome) {
                String leader = ((PlayerWonOutcome) outcome).getWinner();
                TournamentLeaf leaf = leafContext.getLeaf();
                String sessionStr = leafContext.getSession().getSession();
                // Step 1. Reading group and level
                int firstSeparator = sessionStr.indexOf(TOURNAME_SEPARATOR);
                int lastSeparator = sessionStr.lastIndexOf(TOURNAME_SEPARATOR);
                int level = Integer.valueOf(sessionStr.substring(firstSeparator, lastSeparator));
                int group = Integer.valueOf(sessionStr.substring(lastSeparator));
                TournamentLeaf finalLeaf = new TournamentLeaf(((PlayerWonOutcome) outcome).getWinner(), leaf.getSession(), leaf.getLeafs());
                // Step 2. Processing level & group
                if (level == 0) {
                    return new TournamentEndedEvent(context.getSession(), outcome, context);
                } else {
                    // Step 3. Creating final leaf
                    Entry<Integer, Integer> levToGroup = new ImmutablePair<>(level, group);
                    levels.get(levToGroup.getKey()).add(levToGroup.getValue(), finalLeaf);
                }
            } else {
                // TODO add processing
            }
        } else {
            throw new IllegalArgumentException();
        }
        return null;
    }

    @Override
    public GameUnit getState() {
        throw new IllegalArgumentException();
    }

    @Override
    public int getVersion() {
        throw new IllegalArgumentException();
    }

    public class TournamentLevel implements GameConfigurationAware {

        final private AtomicInteger level;
        final private GameContext<?> parent;
        final private GameSessionKey session;
        final private GameConfiguration configuration;
        final private List<TournamentLeaf> waitingList = new ArrayList<>();

        public TournamentLevel(GameSessionKey session, GameConfiguration configuration, GameContext<?> context) {
            this.level = new AtomicInteger();
            this.parent = context;
            this.session = session;
            this.configuration = configuration;
        }

        public void add(int group, TournamentLeaf pending) {
            // This is synchronous due to GameManager, careful with GameManager
            waitingList.add(pending);
            if(waitingList.size() == configuration.getNumberRule().getMinPlayers()) {
                GameSessionKey sessionKey = session.append(TOURNAME_SEPARATOR + level.getAndIncrement());
                GameInitiation initiation = new GameInitiation(session.append(TOURNAME_SEPARATOR + level), PlayerAwareUtils.toPlayerList(waitingList), configuration);
                TournamentLeaf leaf = new TournamentLeaf(PlayerAware.DEFAULT_PLAYER, sessionKey, waitingList);
                TournamentGameContext parentContext = new TournamentGameContext(initiation, leaf, parent);
                managerFactory.start(initiation, parentContext);
            }
            waitingList.clear();
        }

        @Override
        public GameConfiguration getConfiguration() {
            return configuration;
        }

    }


}
