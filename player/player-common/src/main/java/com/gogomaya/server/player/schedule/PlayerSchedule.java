package com.gogomaya.server.player.schedule;

import java.util.PriorityQueue;
import java.util.Queue;

import com.gogomaya.server.player.PlayerAware;

public class PlayerSchedule implements PlayerAware {

    /**
     * Generated 19/06/13
     */
    private static final long serialVersionUID = -6860964578927436141L;

    final private long playerId;

    final private PriorityQueue<PlayerScheduleEvent> scheduledEvents = new PriorityQueue<PlayerScheduleEvent>();

    public PlayerSchedule(long playerId) {
        this.playerId = playerId;
    }

    @Override
    public long getPlayerId() {
        return playerId;
    }

    public Queue<PlayerScheduleEvent> getScheduledEvents() {
        return scheduledEvents;
    }

}
