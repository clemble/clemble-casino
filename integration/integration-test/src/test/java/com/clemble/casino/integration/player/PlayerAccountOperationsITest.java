package com.clemble.casino.integration.player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
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

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.integration.spring.TestConfiguration;
import com.clemble.casino.integration.util.ClembleCasinoExceptionMatcherFactory;
import com.clemble.casino.money.MoneySource;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.payment.money.Currency;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { TestConfiguration.class })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class })
public class PlayerAccountOperationsITest {

    @Autowired
    public PlayerOperations playerOperations;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Ignore // TODO security was temporary disabled
    @Test
    public void testAmountAfterRegistration() {
        // Step 1. Creating player
        Player player = playerOperations.createPlayer();
        // Step 2. Checking there is at least one
        PlayerAccount accountA = player.getWalletOperations().getAccount();
        // Step 3. Checking that there are some fake moneys in the newly created account
        assertNotNull(accountA);
        assertNotNull(accountA.getMoney(Currency.FakeMoney));
        assertTrue(accountA.getMoney(Currency.FakeMoney).getAmount() > 0);
        // Step 4. Checking that there are some fake moneys in the newly created account, accesed through WalletOperations
        PlayerAccount accountB = player.getWalletOperations().getAccount();
        assertNotNull(accountB);
        assertNotNull(accountB.getMoney(Currency.FakeMoney));
        assertEquals(accountB, accountA);
        // Step 5. Checking that there are some fake moneys in the newly created account, accesed through another WalletOperations
        PlayerAccount anotherWallet = player.getWalletOperations().getAccount();
        assertNotNull(anotherWallet);
        assertNotNull(anotherWallet.getMoney(Currency.FakeMoney));
        assertEquals(anotherWallet, accountA);

//        Player anotherPlayer = playerOperations.createPlayer();
//
//        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.PlayerAccountAccessDenied));
//        player.getWalletOperations().getAccount();
    }

    @Ignore // TODO security was temporary disabled
    @Test
    public void testTransactionsListAccess() {
        // Step 1. Checking player has no transactions to access
        Player player = playerOperations.createPlayer();
        List<PaymentTransaction> transactions = player.getWalletOperations().listPlayerTransaction();
        Assert.assertFalse(transactions.isEmpty());
        // Step 2. Checking no other player can't access the transactions
        Player anotherPlayer = playerOperations.createPlayer();
        // TODO Assert.assertFalse(player.getWalletOperations().getPaymentTransaction(MoneySource.Registration, player.getPlayer()));
        // Step 3. Checking no other player can access the transactions
        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.PaymentTransactionAccessDenied));
        player.getWalletOperations().getPaymentTransaction(MoneySource.registration, anotherPlayer.getPlayer());
    }

    @Test
    public void testAccessNonExistent() {
        // Step 1. Checking player has no transactions to access
        Player player = playerOperations.createPlayer();
        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.PaymentTransactionNotExists));
        player.getWalletOperations().getPaymentTransaction(MoneySource.TicTacToe, "-1");
    }

}
