package com.clemble.casino.payment.bonus;

import java.io.Serializable;

import com.clemble.casino.payment.PaymentTransactionKey;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentBonusMarker implements Serializable {

    /**
     * Generated 14/12/13
     */
    private static final long serialVersionUID = 4964324368727277208L;

    final private PaymentBonusKey bonusKey;

    final private String marker;

    @JsonCreator
    public PaymentBonusMarker(@JsonProperty("bonusKey") PaymentBonusKey bonusKey, @JsonProperty("reseivingDate") String marker) {
        this.bonusKey = bonusKey;
        this.marker = marker;
    }

    public PaymentBonusKey getBonusKey() {
        return bonusKey;
    }

    public String getMarker() {
        return marker;
    }

    public PaymentTransactionKey toTransactionKey() {
        return new PaymentTransactionKey(bonusKey.getSource() + marker, bonusKey.getPlayer());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bonusKey == null) ? 0 : bonusKey.hashCode());
        result = prime * result + ((marker == null) ? 0 : marker.hashCode());
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
        PaymentBonusMarker other = (PaymentBonusMarker) obj;
        if (bonusKey == null) {
            if (other.bonusKey != null)
                return false;
        } else if (!bonusKey.equals(other.bonusKey))
            return false;
        if (marker == null) {
            if (other.marker != null)
                return false;
        } else if (!marker.equals(other.marker))
            return false;
        return true;
    }

}
