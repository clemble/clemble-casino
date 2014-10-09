package com.clemble.casino.server.event.bet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 8/9/14.
 */
public class SystemBetCanceledEvent implements SystemBetEvent {

    final public static String CHANNEL = "sys:bet:canceled";

    final private String player;
    final private String transactionKey;

    @JsonCreator
    public SystemBetCanceledEvent(@JsonProperty(PLAYER) String player, @JsonProperty(TRANSACTION_KEY) String transactionKey) {
        this.player = player;
        this.transactionKey = transactionKey;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    @Override
    public String getTransactionKey() {
        return transactionKey;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SystemBetCanceledEvent that = (SystemBetCanceledEvent) o;

        if (!transactionKey.equals(that.transactionKey)) return false;
        if (!player.equals(that.player)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = player.hashCode();
        result = 31 * result + transactionKey.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return transactionKey + ":" + player + " > " + CHANNEL;
    }

}
