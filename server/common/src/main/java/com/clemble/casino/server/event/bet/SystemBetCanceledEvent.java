package com.clemble.casino.server.event.bet;

import com.clemble.casino.payment.PaymentTransactionKey;
import com.clemble.casino.player.PlayerAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 8/9/14.
 */
public class SystemBetCanceledEvent implements SystemBetEvent, PlayerAware {

    final public static String CHANNEL = "bet:canceled";

    final private String player;
    final private PaymentTransactionKey transactionKey;

    @JsonCreator
    public SystemBetCanceledEvent(@JsonProperty("player") String player, @JsonProperty("transactionKey") PaymentTransactionKey transactionKey) {
        this.player = player;
        this.transactionKey = transactionKey;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    @Override
    public PaymentTransactionKey getTransactionKey() {
        return transactionKey;
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
    public String getChannel() {
        return CHANNEL;
    }
}
