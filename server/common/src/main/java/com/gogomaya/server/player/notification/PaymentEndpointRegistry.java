package com.gogomaya.server.player.notification;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentEndpointRegistry {

    final private String paymentEndpoint;

    @JsonCreator
    public PaymentEndpointRegistry(@JsonProperty("paymentEndpoint") String paymentEndpoint) {
        this.paymentEndpoint = paymentEndpoint;
    }

    public String getPaymentEndpoint() {
        return paymentEndpoint;
    }

}
