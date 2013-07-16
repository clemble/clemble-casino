package com.gogomaya.integration.player;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.integration.player.PlayerOperations;
import com.gogomaya.server.integration.player.wallet.WalletOperations;
import com.gogomaya.server.money.Currency;
import com.gogomaya.server.player.wallet.PlayerWallet;
import com.gogomaya.server.spring.integration.TestConfiguration;
import com.gogomaya.server.test.RedisCleaner;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { TestConfiguration.class })
@TestExecutionListeners(listeners = { RedisCleaner.class, DependencyInjectionTestExecutionListener.class })
public class PlayerWalletOperationsTest {

    @Autowired
    public PlayerOperations playerOperations;

    @Autowired
    public WalletOperations walletOperations;

    @Rule
    public ExpectedException expectedException;

    @Test
    public void testAmountAfterRegistration() {
        // Step 1. Creating player
        Player randomPlayer = playerOperations.createPlayer();
        // Step 2. Checking there is at least one
        PlayerWallet wallet = randomPlayer.getWalletOperations().getWallet();
        // Step 3. Checking that there are some fake moneys in the newly created account
        assertNotNull(wallet);
        assertNotNull(wallet.getMoney(Currency.FakeMoney));
        assertTrue(wallet.getMoney(Currency.FakeMoney).getAmount() > 0);
    }

}
