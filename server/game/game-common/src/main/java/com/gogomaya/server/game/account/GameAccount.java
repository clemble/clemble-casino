package com.gogomaya.server.game.account;

import java.io.Serializable;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gogomaya.server.money.Money;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface GameAccount extends Serializable {

    public Money getBank();

    public Collection<GamePlayerAccount> getPlayerAccounts();

    public GamePlayerAccount getPlayerAccount(long playerId);

    public void subMoneyLeft(long playerId, long amount);

}
