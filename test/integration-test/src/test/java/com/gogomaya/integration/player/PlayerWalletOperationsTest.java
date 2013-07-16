package com.gogomaya.integration.player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Assert;
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

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.integration.player.PlayerOperations;
import com.gogomaya.server.integration.player.wallet.WalletOperations;
import com.gogomaya.server.integration.util.GogomayaExceptionMatcherFactory;
import com.gogomaya.server.money.Currency;
import com.gogomaya.server.money.MoneySource;
import com.gogomaya.server.payment.PaymentTransaction;
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
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testAmountAfterRegistration() {
        // Step 1. Creating player
        Player player = playerOperations.createPlayer();
        // Step 2. Checking there is at least one
        PlayerWallet wallet = player.getWalletOperations().getWallet();
        // Step 3. Checking that there are some fake moneys in the newly created account
        assertNotNull(wallet);
        assertNotNull(wallet.getMoney(Currency.FakeMoney));
        assertTrue(wallet.getMoney(Currency.FakeMoney).getAmount() > 0);
        // Step 4. Checking that there are some fake moneys in the newly created account, accesed through WalletOperations
        PlayerWallet walletThrough = walletOperations.getWallet(player);
        assertNotNull(walletThrough);
        assertNotNull(walletThrough.getMoney(Currency.FakeMoney));
        assertEquals(walletThrough, wallet);
        // Step 5. Checking that there are some fake moneys in the newly created account, accesed through another WalletOperations
        PlayerWallet anotherWallet = walletOperations.getWallet(player, player.getPlayerId());
        assertNotNull(anotherWallet);
        assertNotNull(anotherWallet.getMoney(Currency.FakeMoney));
        assertEquals(anotherWallet, wallet);

        Player anotherPlayer = playerOperations.createPlayer();

        expectedException.expect(GogomayaExceptionMatcherFactory.fromErrors(GogomayaError.PlayerWalletAccessDenied));
        walletOperations.getWallet(player, anotherPlayer.getPlayerId());
    }

    @Test
    public void testTransactionsListAccess() {
        // Step 1. Checking player has no transactions to access
        Player player = playerOperations.createPlayer();
        List<PaymentTransaction> transactions = walletOperations.getTransactions(player);
        Assert.assertTrue(transactions.isEmpty());
        // Step 2. Checking no other player can't access the transactions
        Player anotherPlayer = playerOperations.createPlayer();
        Assert.assertTrue(walletOperations.getTransactions(anotherPlayer).isEmpty());
        // Step 3. Checking no other player can access the transactions
        expectedException.expect(GogomayaExceptionMatcherFactory.fromErrors(GogomayaError.PaymentTransactionAccessDenied));
        walletOperations.getTransactions(player, anotherPlayer.getPlayerId());
    }

    @Test
    public void testAccessNonExistent() {
        // Step 1. Checking player has no transactions to access
        Player player = playerOperations.createPlayer();
        expectedException.expect(GogomayaExceptionMatcherFactory.fromErrors(GogomayaError.PaymentTransactionNotExists));
        walletOperations.getTransaction(player, MoneySource.TicTacToe, -1);
    }

}
