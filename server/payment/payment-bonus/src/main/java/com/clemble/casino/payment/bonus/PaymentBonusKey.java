package com.clemble.casino.payment.bonus;

import com.clemble.casino.player.PlayerAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentBonusKey implements PlayerAware {

    /**
     * Generated 14/12/13
     */
    private static final long serialVersionUID = -7590978707802281074L;

    final private String player;
    final private PaymentBonusSource source;

    @JsonCreator
    public PaymentBonusKey(@JsonProperty("player") String player, @JsonProperty("source") PaymentBonusSource source) {
        this.player = player;
        this.source = source;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    public PaymentBonusSource getSource() {
        return source;
    }

    @Override
    public String toString() {
        return source + ":" + player;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((player == null) ? 0 : player.hashCode());
        result = prime * result + ((source == null) ? 0 : source.hashCode());
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
        PaymentBonusKey other = (PaymentBonusKey) obj;
        if (player == null) {
            if (other.player != null)
                return false;
        } else if (!player.equals(other.player))
            return false;
        if (source != other.source)
            return false;
        return true;
    }

}
