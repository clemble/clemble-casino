package com.clemble.casino.game.account;

import java.io.Serializable;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.clemble.casino.payment.money.Money;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface GameAccount extends Serializable {

    public Money getBank();

    public Collection<GamePlayerAccount> getPlayerAccounts();

    public GamePlayerAccount getPlayerAccount(String player);

    public void subMoneyLeft(String player, long amount);

}
