package com.gogomaya.server.payment;

import com.gogomaya.server.player.PlayerProfile;

public interface PaymentTransactionService {

    public PaymentTransaction register(PlayerProfile playerProfile);

    public PaymentTransaction process(PaymentTransaction walletTransaction);

}
