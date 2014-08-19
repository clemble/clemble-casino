package com.clemble.casino.server.goal;

import com.clemble.casino.goal.GoalAware;
import com.clemble.casino.goal.GoalKey;
import com.clemble.casino.goal.GoalStatus;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

import java.util.Collection;
import java.util.TreeSet;

/**
 * Created by mavarazy on 8/16/14.
 */
public class GoalStatusHistory implements GoalAware {

    @Id
    final private GoalKey goalKey;
    final private TreeSet<GoalStatus> statusHistory;

    @JsonCreator
    public GoalStatusHistory(@JsonProperty(GOAL_KEY) GoalKey goalKey, @JsonProperty("statusHistory") Collection<GoalStatus> statusHistory) {
        this.goalKey = goalKey;
        this.statusHistory = new TreeSet<GoalStatus>(statusHistory);
    }

    @Override
    public GoalKey getGoalKey() {
        return goalKey;
    }

    public TreeSet<GoalStatus> getStatusHistory() {
        return statusHistory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GoalStatusHistory that = (GoalStatusHistory) o;

        if (goalKey != null ? !goalKey.equals(that.goalKey) : that.goalKey != null) return false;
        if (statusHistory != null ? !statusHistory.equals(that.statusHistory) : that.statusHistory != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = goalKey != null ? goalKey.hashCode() : 0;
        result = 31 * result + (statusHistory != null ? statusHistory.hashCode() : 0);
        return result;
    }
}
