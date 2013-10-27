package com.clemble.casino.game;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.clemble.casino.payment.PaymentTransactionKey;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Embeddable
public class GameSessionKey implements GameAware, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7812547584769033422L;

    @Column(name = "GAME")
    private Game game;

    @Column(name = "SESSION_ID")
    private String session;

    public GameSessionKey() {
    }

    @JsonCreator
    public GameSessionKey(@JsonProperty("game") Game game, @JsonProperty("session") String session) {
        this.game = game;
        this.session = session;
    }

    @Override
    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }
    
    public PaymentTransactionKey toPaymentTransactionKey(){
        return new PaymentTransactionKey(game.name(), session);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((game == null) ? 0 : game.hashCode());
        result = prime * result + ((session == null) ? 0 : session.hashCode());
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
        GameSessionKey other = (GameSessionKey) obj;
        return game == other.game && session.equals(other.session);
    }

    @Override
    public String toString() {
        return game + ":" + session;
    }

}
