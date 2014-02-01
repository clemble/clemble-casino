package com.clemble.casino.server.game.action;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.Collections;

import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameSessionState;
import com.clemble.casino.game.MatchGameRecord;
import com.clemble.casino.game.PotGameRecord;
import com.clemble.casino.game.action.GameAction;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.event.server.GameManagementEvent;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.game.specification.PotGameConfiguration;
import com.clemble.casino.server.repository.game.PotGameRecordRepository;

public class PotGameManager implements GameManager<PotGameRecord>{

    final private MatchGameManager<?> matchGameManager;
    final private PotGameRecordRepository potRepository;

    public PotGameManager(MatchGameManager<?> matchGameManager, PotGameRecordRepository potRepository) {
        this.matchGameManager = checkNotNull(matchGameManager);
        this.potRepository = checkNotNull(potRepository);
    }

    @Override
    public PotGameRecord start(GameInitiation initiation) {
        // Step 1. Fetching first pot configuration
        PotGameConfiguration potConfiguration = (PotGameConfiguration) initiation.getConfiguration();
        // Step 2. Taking first match from the pot
        MatchGameConfiguration matchConfiguration = potConfiguration.getMatchConfigurations().get(0);
        // Step 3. Constructing match initiation
        GameInitiation matchInitiation = new GameInitiation(initiation.getSession().append("0"), matchConfiguration, initiation.getParticipants());
        // Step 4. Generating new match game record
        MatchGameRecord<?> matchGameRecord = matchGameManager.start(matchInitiation);
        // Step 5. Generating new pot game record
        PotGameRecord potGameRecord = new PotGameRecord(initiation.getSession(), initiation.getConfiguration().getConfigurationKey(), GameSessionState.active, Collections.<MatchGameRecord<?>>singletonList(matchGameRecord));
        // Step 6. Saving pot record
        return potRepository.saveAndFlush(potGameRecord);
    }

    @Override
    public GameManagementEvent process(GameSessionKey sessionKey, GameAction action) {
        // TODO Auto-generated method stub
        return null;
    }


}
