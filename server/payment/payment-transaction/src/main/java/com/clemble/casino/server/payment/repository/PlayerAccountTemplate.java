package com.clemble.casino.server.payment.repository;

import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PendingTransaction;
import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.money.Money;

import java.util.Collection;

public interface PlayerAccountTemplate {

    public PlayerAccount findOne(String player);

    // TODO this is really bad solution switch to Actors model
    public PlayerAccount process(String transactionKey, PaymentOperation paymentOperation);

    public PendingTransaction freeze(PendingTransaction transaction);

}
