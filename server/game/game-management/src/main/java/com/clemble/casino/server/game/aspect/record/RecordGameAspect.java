package com.clemble.casino.server.game.aspect.record;

import com.clemble.casino.client.event.EventSelector;
import com.clemble.casino.client.event.EventSelectors;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.event.GameEvent;
import com.clemble.casino.game.action.GameAction;
import com.clemble.casino.server.game.aspect.BasicGameAspect;

/**
 * Created by mavarazy on 10/03/14.
 */
public class RecordGameAspect extends BasicGameAspect<GameEvent> {

    public RecordGameAspect() {
        super(EventSelectors.where(new EventTypeSelector(GameEvent.class)));
    }

    @Override
    public void doEvent(GameEvent event) {


    }
}
