package com.clemble.casino.server.game.aspect.next;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.lifecycle.management.event.MatchChangedEvent;
import com.clemble.casino.game.lifecycle.management.event.MatchEvent;
import com.clemble.casino.game.lifecycle.management.event.MatchStartedEvent;
import com.clemble.casino.server.game.action.GameManagerFactoryFacade;
import com.clemble.casino.server.game.aspect.GameAspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mavarazy on 14/10/14.
 */
public class MatchNextGameAspect extends GameAspect<MatchEvent> {

    final private Logger LOG = LoggerFactory.getLogger(MatchNextGameAspect.class);

    final private GameManagerFactoryFacade managerFactory;

    public MatchNextGameAspect(GameManagerFactoryFacade managerFactory) {
        super(new EventTypeSelector(MatchEvent.class));
        this.managerFactory = managerFactory;
    }

    @Override
    public void doEvent(MatchEvent event) {
        if (event instanceof MatchChangedEvent) {
            managerFactory.start(((MatchChangedEvent) event).getNextInitiation(), event.getState().getContext());
        } else if(event instanceof MatchStartedEvent) {
            managerFactory.start(((MatchStartedEvent) event).getNextInitiation(), event.getState().getContext());
        }
    }

}
