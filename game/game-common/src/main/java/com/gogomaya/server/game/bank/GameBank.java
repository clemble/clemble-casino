package com.gogomaya.server.game.bank;

import java.io.Serializable;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gogomaya.server.money.Money;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface GameBank extends Serializable {

    public Money getBank();

    public Collection<GamePlayerAccount> getPlayerAccounts();

    public GamePlayerAccount getPlayerAccount(long playerId);

    public void subMoneyLeft(long playerId, long amount);

}
