package com.gogomaya.server.money;

import com.gogomaya.server.game.rule.GameRule;

public enum Currency implements GameRule {

    FakeMoney;

    final public static Currency DEFAULT = Currency.FakeMoney;

}
