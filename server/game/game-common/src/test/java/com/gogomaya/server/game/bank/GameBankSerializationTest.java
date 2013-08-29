package com.gogomaya.server.game.bank;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogomaya.money.Currency;
import com.gogomaya.money.Money;
import com.gogomaya.server.game.account.GameAccount;
import com.gogomaya.server.game.account.GamePlayerAccount;
import com.gogomaya.server.game.account.InvisibleGameAccount;
import com.gogomaya.server.game.account.VisibleGameAccount;
import com.gogomaya.server.spring.common.JsonSpringConfiguration;
import com.google.common.collect.ImmutableList;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JsonSpringConfiguration.class)
public class GameBankSerializationTest {

    @Autowired
    public ObjectMapper objectMapper;

    @Test
    public void testVisibleBankSerialization() throws IOException {
        Money money = Money.create(Currency.FakeMoney, 100);
        GamePlayerAccount firstAccount = new GamePlayerAccount(1, 50);
        GamePlayerAccount secondAccount = new GamePlayerAccount(2, 50);

        GameAccount savedBank = new VisibleGameAccount(money, ImmutableList.<GamePlayerAccount> of(firstAccount, secondAccount));

        String visibleBankPresentation = objectMapper.writeValueAsString(savedBank);

        GameAccount readBank = objectMapper.readValue(visibleBankPresentation, GameAccount.class);

        assertEquals(savedBank, readBank);
    }

    @Test
    public void testInvisibleBankSerialization() throws IOException {
        Money money = Money.create(Currency.FakeMoney, 100);
        GamePlayerAccount firstAccount = new GamePlayerAccount(1, 50);
        GamePlayerAccount secondAccount = new GamePlayerAccount(2, 50);

        GameAccount savedBank = new InvisibleGameAccount(money, ImmutableList.<GamePlayerAccount> of(firstAccount, secondAccount));

        String inVisibleBankPresentation = objectMapper.writeValueAsString(savedBank);

        GameAccount readBank = objectMapper.readValue(inVisibleBankPresentation, GameAccount.class);

        assertNotEquals(savedBank, readBank);
        assertEquals(savedBank.getBank(), readBank.getBank());
        assertTrue(readBank.getPlayerAccounts().isEmpty());
    }

}
