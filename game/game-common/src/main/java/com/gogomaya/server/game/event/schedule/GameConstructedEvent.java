package com.gogomaya.server.game.event.schedule;

import com.gogomaya.server.event.GameConstructionEvent;

public class GameConstructedEvent implements GameConstructionEvent {

    /**
     * Generated 24/06/13
     */
    private static final long serialVersionUID = 1069615920429317027L;

    final private long construction;

    public GameConstructedEvent(long session) {
        this.construction = session;
    }

    @Override
    public long getConstruction() {
        return construction;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (construction ^ (construction >>> 32));
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
        GameConstructedEvent other = (GameConstructedEvent) obj;
        if (construction != other.construction)
            return false;
        return true;
    }

}
