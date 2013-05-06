package com.gogomaya.server.game.event;

import java.util.Date;

import com.gogomaya.server.event.GogomayaEvent;

public class GameEvent<State> implements GogomayaEvent {

    /**
     * Generated 07/05/13
     */
    private static final long serialVersionUID = -4837244615682915463L;

    private State state;

    private Date publishDate = new Date();

    public State getState() {
        return state;
    }

    public GameEvent<State> setState(State state) {
        this.state = state;
        return this;
    }

    @Override
    public Date getPublishDate() {
        return publishDate;
    }

    public GameEvent<State> setPublishDate(Date date) {
        this.publishDate = date;
        return this;
    }

}
