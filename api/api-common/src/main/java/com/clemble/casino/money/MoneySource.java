package com.clemble.casino.money;

public enum MoneySource {

    pic, // Owner of the cell hidden, bet arbitrary
    pac, // Owner of the cell exposed, bet arbitrary
    poe, // Owner of the cell hidden, bet fixed
    poc, // Owner of the cell exposed, bet fixed
    go,
    num, // Number game for testing
    TicTacToe,
    registration;

}
