package com.gogomaya.server.game.rule;

import javax.persistence.Column;

import com.gogomaya.server.game.GameRule;
import com.gogomaya.server.player.wallet.CashType;

abstract public class BetRule extends GameRule {

    @Column(name = "CASH_TYPE")
    private CashType cashType;

}
