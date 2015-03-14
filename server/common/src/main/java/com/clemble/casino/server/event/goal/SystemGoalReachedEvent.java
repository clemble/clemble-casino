package com.clemble.casino.server.event.goal;

import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.player.PlayerAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 2/3/15.
 *
 * TODO maybe need to generalize this
 */
public class SystemGoalReachedEvent implements SystemGoalEvent, PlayerAware {

    final public static String CHANNEL = "sys:goal:reached";

    final private String goalKey;
    final private String player;
    final private GoalState state;

    @JsonCreator
    public SystemGoalReachedEvent(
        @JsonProperty(GOAL_KEY) String goalKey,
        @JsonProperty(PLAYER) String player,
        @JsonProperty("state") GoalState state) {
        this.player = player;
        this.goalKey = goalKey;
        this.state = state;
    }

    @Override
    public String getGoalKey() {
        return goalKey;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    public GoalState getState() {
        return state;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SystemGoalReachedEvent)) return false;

        SystemGoalReachedEvent that = (SystemGoalReachedEvent) o;

        if (!state.equals(that.state)) return false;
        if (!goalKey.equals(that.goalKey)) return false;
        if (!player.equals(that.player)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = goalKey.hashCode();
        result = 31 * result + state.hashCode();
        result = 31 * result + player.hashCode();
        return result;
    }

}
