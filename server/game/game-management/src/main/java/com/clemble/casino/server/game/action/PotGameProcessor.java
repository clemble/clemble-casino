package com.clemble.casino.server.game.action;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.*;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.event.server.GameEndedEvent;
import com.clemble.casino.game.event.server.GameManagementEvent;
import com.clemble.casino.game.event.server.GamePotChangedEvent;
import com.clemble.casino.game.event.server.GamePotEndedeEvent;
import com.clemble.casino.game.outcome.DrawOutcome;
import com.clemble.casino.game.outcome.PlayerWonOutcome;
import com.clemble.casino.game.specification.PotGameConfiguration;
import com.clemble.casino.player.PlayerAwareUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PotGameProcessor implements GameProcessor<PotGameRecord, Event> {

    final private PotGameContext context;
    final private PotGameConfiguration configuration;
    final private GameManagerFactory managerFactory;

    public PotGameProcessor(PotGameContext context, PotGameConfiguration configuration, GameManagerFactory managerFactory) {
        this.context = context;
        this.configuration = configuration;
        this.managerFactory = managerFactory;
    }

    @Override
    public GameManagementEvent process(PotGameRecord record, Event event) {
        if(event instanceof GameEndedEvent) {
            context.addOutcome(((GameEndedEvent) event).getOutcome());
            int gamesLeft = configuration.getConfigurations().size() - context.getOutcomes().size();
            // Step 1. Searching for a leader in the pot
            List<PotGamePlayerContext> sortedContexts = new ArrayList<>(context.getPlayerContexts());
            Collections.sort(sortedContexts, ComparatorUtils.WON_SIZE_COMPARATOR);
            PotGamePlayerContext leader = sortedContexts.get(0);
            PotGamePlayerContext nextAfterLeader = sortedContexts.get(1);
            int leaderScore = leader.getWonOutcomes().size();
            int nextAfterLeaderScore = nextAfterLeader.getWonOutcomes().size();
            // Step 2. Checking values
            if (leaderScore > nextAfterLeaderScore && 
               (nextAfterLeaderScore + gamesLeft < leaderScore)) {
                return new GamePotEndedeEvent(context.getSession(), new PlayerWonOutcome(leader.getPlayer()), context);
            }
            // Step 3. If no games left mark as a draw
            if (gamesLeft == 0)
                return new GamePotEndedeEvent(context.getSession(), new DrawOutcome(), context);
            // Step 4. Constructing next match initiation
            int gameNum = context.getOutcomes().size();
            GameInitiation subInitiation = new GameInitiation(
                    context.getSession().append(String.valueOf(gameNum)), 
                    configuration.getConfigurations().get(gameNum),
                    PlayerAwareUtils.toPlayerList(context.getPlayerContexts()));
            GameManager<?> manager = managerFactory.start(subInitiation, context);
            record.getMatchRecords().add(manager.getRecord());
            // Step 5. Sending Game Changed event
            return new GamePotChangedEvent(context.getSession(), context);
        } else {
            throw new IllegalArgumentException();
        }
    }

}
