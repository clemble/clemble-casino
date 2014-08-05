package com.clemble.casino.server.payment.repository;

import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.money.Money;

public interface PlayerAccountTemplate {

    public PlayerAccount findOne(String player);

    // TODO this is really bad solution switch to Actors model
    public void debit(String player, Money amount);

    public void credit(String player, Money amount);

}
