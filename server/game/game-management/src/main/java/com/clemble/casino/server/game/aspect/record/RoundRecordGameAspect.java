package com.clemble.casino.server.game.aspect.record;

import com.clemble.casino.client.event.EventSelectors;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.event.GameEvent;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.action.GameAction;
import com.clemble.casino.game.action.MadeMove;
import com.clemble.casino.game.event.server.GameManagementEvent;
import com.clemble.casino.server.game.aspect.BasicGameAspect;
import com.clemble.casino.server.repository.game.MadeMoveRepository;

import java.util.Date;

/**
 * Created by mavarazy on 10/03/14.
 */
public class RoundRecordGameAspect extends BasicGameAspect<GameEvent> {

    final private MadeMoveRepository moveRepository;

    private GameAction pendingAction;

    public RoundRecordGameAspect(MadeMoveRepository moveRepository) {
        super(EventSelectors.where(new EventTypeSelector(GameEvent.class)));
        this.moveRepository = moveRepository;
    }

    @Override
    public void doEvent(GameEvent event) {
        if (event instanceof GameAction) {
            pendingAction = (GameAction) event;
        } else if(event instanceof GameManagementEvent) {
            // Step 1. Extracting session key
            GameSessionKey sessionKey = ((GameManagementEvent) event).getSession();
            // Step 2. Constructing made move
            MadeMove move = new MadeMove()
                .setRequest(pendingAction)
                .setResponse(event)
                .setCreated(new Date());
            // Step 3. Saving made move
            moveRepository.save(sessionKey, move);
        }
    }
}
