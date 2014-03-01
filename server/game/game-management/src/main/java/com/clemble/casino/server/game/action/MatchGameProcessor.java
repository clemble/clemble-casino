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
import com.clemble.casino.player.PlayerAwareUtils;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

public class MatchGameProcessor implements GameProcessor<MatchGameRecord, Event> {

    final private MatchGameContext context;
    final private MatchGameConfiguration configuration;
    final private GameManagerFactory managerFactory;

    public MatchGameProcessor(MatchGameContext context, MatchGameConfiguration configuration, GameManagerFactory managerFactory) {
        this.context = context;
        this.configuration = configuration;
        this.managerFactory = managerFactory;
    }

    @Override
    public GameManagementEvent process(MatchGameRecord record, Event event) {
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
                    return new MatchEndedEvent(context.getSession(), new PlayerWonOutcome(leader.getKey()), context, null);
                }
                // Step 3. If no games left mark as a draw
                if (gamesLeft == 0)
                    return new MatchEndedEvent(context.getSession(), new DrawOutcome(), context, null);
            }
        }
        // Step 4. Constructing next match initiation
        int gameNum = context.getOutcomes().size();
        GameSessionKey nextSessionKey = context.getSession().append(String.valueOf(gameNum));
        context.setCurrentSession(nextSessionKey);
        GameInitiation subInitiation = new GameInitiation(
                nextSessionKey,
                configuration.getConfigurations().get(gameNum),
                PlayerAwareUtils.toPlayerList(context.getPlayerContexts()));
        GameManager<?> manager = managerFactory.start(subInitiation, context);
        record.getSubRecords().add(manager.getRecord().getSession());
        // Step 5. Sending Game Changed event
        return new MatchChangedEvent(context.getSession(), context, nextSessionKey);
    }

}
