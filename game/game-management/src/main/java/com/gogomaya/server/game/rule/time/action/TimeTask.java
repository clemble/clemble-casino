package com.gogomaya.server.game.rule.time.action;

import com.gogomaya.server.game.action.SessionAware;

public class TimeTask implements SessionAware, Comparable<TimeTask> {

    final private long session;
    final private long checkTime;

    public TimeTask(final long session, final long checkTime) {
        this.session = session;
        this.checkTime = checkTime;
    }

    @Override
    public long getSession() {
        return session;
    }

    public long getCheckTime() {
        return checkTime;
    }

    @Override
    public int compareTo(TimeTask o) {
        return (int) (checkTime - o.checkTime);
    }

    @Override
    public int hashCode() {
        return (int) (session ^ (session >>> 32));
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof TimeTask) && session == ((TimeTask) obj).session;
    }

}
