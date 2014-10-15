package com.clemble.casino.server.payment.repository;

import com.clemble.casino.payment.PendingOperation;
import com.clemble.casino.payment.PendingTransaction;
import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.money.Money;

import java.util.Collection;

public interface PlayerAccountTemplate {

    public PlayerAccount findOne(String player);

    // TODO this is really bad solution switch to Actors model
    public void debit(String player, Money amount);

    public void credit(String player, Money amount);

    public PendingTransaction freeze(Collection<String> players, String transactionKey, Money amount);
}
