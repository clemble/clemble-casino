package com.gogomaya.game.construct;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gogomaya.game.SessionAware;

@Entity
@Table(name = "GAME_SCHEDULE")
public class ScheduledGame implements SessionAware {

    /**
     * Generated 10/07/13
     */
    private static final long serialVersionUID = 1773102437262489956L;

    @Id
    @Column(name = "CONSTRUCTION_ID")
    private long session;

    @Column(name = "START_TIME")
    private long startDate;

    public ScheduledGame() {
    }

    public ScheduledGame( long session, long startDate) {
        this.session = session;
        this.startDate = startDate;
    }

    @Override
    public long getSession() {
        return session;
    }

    public void setSession(long construction) {
        this.session = construction;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startTime) {
        this.startDate = startTime;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (session ^ (session >>> 32));
        result = prime * result + (int) (startDate ^ (startDate >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ScheduledGame other = (ScheduledGame) obj;
        if (session != other.session)
            return false;
        if (startDate != other.startDate)
            return false;
        return true;
    }

}
