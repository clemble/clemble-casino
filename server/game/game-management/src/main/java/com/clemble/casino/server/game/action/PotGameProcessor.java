package com.clemble.casino.server.game.action;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.*;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.event.server.GameEndedEvent;
import com.clemble.casino.game.event.server.GameManagementEvent;
import com.clemble.casino.game.event.server.GamePotChangedEvent;
import com.clemble.casino.game.event.server.GamePotEndedEvent;
import com.clemble.casino.game.outcome.DrawOutcome;
import com.clemble.casino.game.outcome.GameOutcome;
import com.clemble.casino.game.outcome.PlayerWonOutcome;
import com.clemble.casino.game.specification.PotGameConfiguration;
import com.clemble.casino.player.PlayerAwareUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.Map.Entry;

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
            MultiValueMap<String, PlayerWonOutcome> wonOutcomes = new LinkedMultiValueMap<>();
            for(GameOutcome outcome: context.getOutcomes())
                if(outcome instanceof  PlayerWonOutcome)
                    wonOutcomes.add(((PlayerWonOutcome) outcome).getWinner(), (PlayerWonOutcome) outcome);
            // Step 1. Searching for a leader in the pot
            List<Entry<String, List<PlayerWonOutcome>>> sortedContexts = new ArrayList<>(wonOutcomes.entrySet());
            Collections.sort(sortedContexts, ComparatorUtils.WON_OUT_COMPARATOR);
            Entry<String, List<PlayerWonOutcome>> leader = sortedContexts.get(0);
            Entry<String, List<PlayerWonOutcome>> nextAfterLeader = sortedContexts.get(1);
            int leaderScore = leader.getValue().size();
            int nextAfterLeaderScore = nextAfterLeader.getValue().size();
            // Step 2. Checking values
            if (leaderScore > nextAfterLeaderScore && 
               (nextAfterLeaderScore + gamesLeft < leaderScore)) {
                return new GamePotEndedEvent(context.getSession(), new PlayerWonOutcome(leader.getKey()), context);
            }
            // Step 3. If no games left mark as a draw
            if (gamesLeft == 0)
                return new GamePotEndedEvent(context.getSession(), new DrawOutcome(), context);
        }
        // Step 4. Constructing next match initiation
        int gameNum = context.getOutcomes().size();
        GameSessionKey nextSessionKey = context.getSession().append(String.valueOf(gameNum));
        GameInitiation subInitiation = new GameInitiation(
                nextSessionKey,
                configuration.getConfigurations().get(gameNum),
                PlayerAwareUtils.toPlayerList(context.getPlayerContexts()));
        GameManager<?> manager = managerFactory.start(subInitiation, context);
        record.getSubRecords().add(manager.getRecord().getSession());
        // Step 5. Sending Game Changed event
        return new GamePotChangedEvent(context.getSession(), context, nextSessionKey);
    }

}
