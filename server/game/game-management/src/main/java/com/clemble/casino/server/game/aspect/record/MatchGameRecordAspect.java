package com.clemble.casino.server.game.aspect.record;

import com.clemble.casino.event.GameEvent;
import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.action.MadeMove;
import com.clemble.casino.server.game.aspect.BasicGameAspect;
import com.clemble.casino.server.repository.game.MadeMoveRepository;

import java.util.Date;

/**
 * Created by mavarazy on 12/03/14.
 */
public class MatchGameRecordAspect extends BasicGameAspect<GameEvent> {

    final private GameSessionKey sessionKey;
    final private MadeMoveRepository moveRepository;

    private GameEvent pending;

    public MatchGameRecordAspect(GameContext<?> context, MadeMoveRepository moveRepository) {
        super(null);
        this.moveRepository = moveRepository;
        this.sessionKey = context.getSession();
    }

    @Override
    public void doEvent(GameEvent event) {
        if (pending == null) {
            pending = event;
        } else {
            MadeMove move = new MadeMove()
                .setRequest(pending)
                .setResponse(event)
                .setCreated(new Date());
            // Step 3. Saving made move
            moveRepository.save(sessionKey, move);
            pending = null;
        }
    }
}
