package com.clemble.casino.server.game.action;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.*;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.event.server.GameEndedEvent;
import com.clemble.casino.game.event.server.GameManagementEvent;
import com.clemble.casino.game.event.server.MatchChangedEvent;
import com.clemble.casino.game.event.server.MatchEndedEvent;
import com.clemble.casino.game.outcome.DrawOutcome;
import com.clemble.casino.game.outcome.GameOutcome;
import com.clemble.casino.game.outcome.PlayerWonOutcome;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.game.unit.GameUnit;
import com.clemble.casino.player.PlayerAwareUtils;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

public class MatchGameState implements GameState<MatchGameContext, Event> {

    final static private Logger LOG = LoggerFactory.getLogger(MatchGameState.class);

    final private MatchGameContext context;
    final private MatchGameConfiguration configuration;
    final private GameManagerFactory managerFactory;

    public MatchGameState(MatchGameContext context, MatchGameConfiguration configuration, GameManagerFactory managerFactory) {
        this.context = context;
        this.configuration = configuration;
        this.managerFactory = managerFactory;
    }

    @Override
    public MatchGameContext getContext(){
        return context;
    }

    @Override
    public GameManagementEvent process(Event event) {
        LOG.debug("{} match processing {}", context.getSession(), event);
        if(event instanceof GameEndedEvent) {
            context.addOutcome(((GameEndedEvent<?>) event).getOutcome());
            int gamesLeft = configuration.getConfigurations().size() - context.getOutcomes().size();
            if (context.getOutcomes().size() > gamesLeft) {
                MultiValueMap<String, PlayerWonOutcome> wonOutcomes = new LinkedMultiValueMap<>();
                for(GameOutcome outcome: context.getOutcomes())
                    if(outcome instanceof  PlayerWonOutcome)
                        wonOutcomes.add(((PlayerWonOutcome) outcome).getWinner(), (PlayerWonOutcome) outcome);
                // Step 1. Searching for a leader in the pot
                List<Entry<String, List<PlayerWonOutcome>>> sortedContexts = new ArrayList<>(wonOutcomes.entrySet());
                Collections.sort(sortedContexts, ComparatorUtils.WON_OUT_COMPARATOR);
                Entry<String, List<PlayerWonOutcome>> leader = sortedContexts.get(0);
                int leaderScore = leader.getValue().size();
                int nextAfterLeaderScore = 0;
                if (sortedContexts.size() > 1) {
                    Entry<String, List<PlayerWonOutcome>> nextAfterLeader = sortedContexts.get(1);
                    nextAfterLeaderScore = nextAfterLeader.getValue().size();
                }
                // Step 2. Checking leader can be reached
                if (leaderScore > nextAfterLeaderScore && 
                   (nextAfterLeaderScore + gamesLeft < leaderScore)) {
                    LOG.debug("{} match winner determined {}", context.getSession(), leader.getKey());
                    return new MatchEndedEvent(context.getSession(), new PlayerWonOutcome(leader.getKey()), context);
                }
                // Step 3. If no games left mark as a draw
                if (gamesLeft == 0) {
                    LOG.debug("{} no more games left, considering this as a draw");
                    return new MatchEndedEvent(context.getSession(), new DrawOutcome(), context);
                }
            }
        }
        // Step 4. Constructing next match initiation
        int gameNum = context.getOutcomes().size();
        GameSessionKey nextSessionKey = context.getSession().append(String.valueOf(gameNum));
        LOG.debug("{} launching new game {} with key {}", context.getSession(), gameNum, nextSessionKey);
        context.setCurrentSession(nextSessionKey);
        GameInitiation subInitiation = new GameInitiation(
                nextSessionKey,
                configuration.getConfigurations().get(gameNum),
                PlayerAwareUtils.toPlayerList(context.getPlayerContexts()));
        GameManager<?> manager = managerFactory.start(subInitiation, context);
        // Step 5. Sending Game Changed event
        return new MatchChangedEvent(context.getSession(), context, nextSessionKey);
    }

    @Override
    public GameUnit getState() {
        return null;
    }

    @Override
    public int getVersion() {
        return context.getOutcomes().size();
    }
}
