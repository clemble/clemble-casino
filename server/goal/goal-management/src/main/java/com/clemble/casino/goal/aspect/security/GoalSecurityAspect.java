package com.clemble.casino.goal.aspect.security;

import com.clemble.casino.client.PlayerActionTypeSelector;
import com.clemble.casino.event.Event;
import com.clemble.casino.goal.aspect.GoalAspect;
import com.clemble.casino.lifecycle.management.event.action.PlayerAction;
import com.clemble.casino.lifecycle.management.event.action.bet.BidAction;
import com.clemble.casino.player.PlayerAware;

/**
 * Created by mavarazy on 1/17/15.
 */
public class GoalSecurityAspect extends GoalAspect<PlayerAction<?>> implements PlayerAware {

    final private String player;

    public GoalSecurityAspect(String player) {
        super(new PlayerActionTypeSelector(Event.class));
        this.player = player;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    @Override
    protected void doEvent(PlayerAction<?> event) {
        // Allow only BidActions from different users
        if (!event.getPlayer().equals(player)) {
            // Case 1. Player is different Player
            if (!(event.getAction() instanceof BidAction))
                throw new IllegalArgumentException();
        }
    }
}
