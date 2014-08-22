package com.clemble.casino.server.event.bet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 8/9/14.
 */
public class SystemBetCompletedEvent implements SystemBetEvent {

    final public static String CHANNEL = "bet:completed";

    final private String transactionKey;
    final private String player;

    @JsonCreator
    public SystemBetCompletedEvent(@JsonProperty(TRANSACTION_KEY) String transactionKey, @JsonProperty(PLAYER) String player) {
        this.transactionKey = transactionKey;
        this.player = player;
    }

    @Override
    public String getTransactionKey() {
        return transactionKey;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SystemBetCompletedEvent that = (SystemBetCompletedEvent) o;

        if (!transactionKey.equals(that.transactionKey)) return false;
        if (!player.equals(that.player)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = transactionKey.hashCode();
        result = 31 * result + player.hashCode();
        return result;
    }

}
