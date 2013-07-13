package com.gogomaya.server.game.construct;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gogomaya.server.game.ConstructionAware;

@Entity
@Table(name = "GAME_SCHEDULE")
public class ScheduledGame implements ConstructionAware {

    /**
     * Generated 10/07/13
     */
    private static final long serialVersionUID = 1773102437262489956L;

    @Id
    @Column(name = "CONSTRUCTION_ID")
    private long construction;

    @Column(name = "START_TIME")
    private long startDate;

    public ScheduledGame() {
    }

    public ScheduledGame( long construction, long startDate) {
        this.construction = construction;
        this.startDate = startDate;
    }

    @Override
    public long getConstruction() {
        return construction;
    }

    public void setConstruction(long construction) {
        this.construction = construction;
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
        result = prime * result + (int) (construction ^ (construction >>> 32));
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
        if (construction != other.construction)
            return false;
        if (startDate != other.startDate)
            return false;
        return true;
    }

}
