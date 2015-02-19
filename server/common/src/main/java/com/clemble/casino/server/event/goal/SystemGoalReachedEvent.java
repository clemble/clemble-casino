package com.clemble.casino.server.event.goal;

import com.clemble.casino.goal.GoalDescriptionAware;
import com.clemble.casino.goal.lifecycle.management.GoalRoleAware;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.tag.TagAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTimeZone;

import java.util.Set;

/**
 * Created by mavarazy on 2/3/15.
 *
 * TODO maybe need to generalize this
 */
public class SystemGoalReachedEvent implements SystemGoalEvent, PlayerAware, GoalDescriptionAware, TagAware, GoalRoleAware {

    final public static String CHANNEL = "sys:goal:reached";

    final private String goalKey;
    final private String player;
    final private Set<String> supporters;
    final private String goal;
    final private DateTimeZone timezone;
    final private String tag;

    @JsonCreator
    public SystemGoalReachedEvent(
        @JsonProperty(GOAL_KEY) String goalKey,
        @JsonProperty("player") String player,
        @JsonProperty("supporters") Set<String> supporters,
        @JsonProperty("goal") String goal,
        @JsonProperty("timezone") DateTimeZone timezone,
        @JsonProperty("tag") String tag) {
        this.goalKey = goalKey;
        this.goal = goal;
        this.timezone = timezone;
        this.supporters = supporters;
        this.player = player;
        this.tag = tag;
    }

    @Override
    public String getGoalKey() {
        return goalKey;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    @Override
    public Set<String> getSupporters() {
        return supporters;
    }

    @Override
    public String getGoal() {
        return goal;
    }

    @Override
    public DateTimeZone getTimezone() {
        return timezone;
    }

    @Override
    public String getTag() {
        return tag;
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

        if (!goal.equals(that.goal)) return false;
        if (!goalKey.equals(that.goalKey)) return false;
        if (!player.equals(that.player)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = goalKey.hashCode();
        result = 31 * result + goal.hashCode();
        result = 31 * result + player.hashCode();
        return result;
    }

}
