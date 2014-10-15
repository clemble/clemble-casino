package com.clemble.casino.integration.payment;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.clemble.casino.integration.game.RoundGamePlayer;
import com.clemble.casino.money.MoneySource;
import com.clemble.casino.payment.bonus.PaymentBonusSource;
import com.clemble.casino.payment.event.PaymentBonusEvent;
import com.clemble.test.concurrent.Get;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.event.EventListener;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.game.Game;
import com.clemble.casino.integration.game.construction.GameScenarios;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.clemble.casino.integration.util.ClembleCasinoExceptionMatcherFactory;
import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.test.concurrent.AsyncCompletionUtils;
import com.clemble.test.concurrent.Check;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { IntegrationTestSpringConfiguration.class })
public class PlayerAccountOperationsITest {

    @Autowired
    public PlayerScenarios playerOperations;

    @Autowired
    public GameScenarios gameOperations;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testInitialAmount() throws InterruptedException {
        // Step 1. Creating random player A
        final ClembleCasinoOperations A = playerOperations.createPlayer();
        // Step 1.1 Registering bonus event listener, and waiting
        final BlockingQueue<PaymentBonusEvent> bonusLatch = new ArrayBlockingQueue<>(2);
        A.listenerOperations().subscribe(new EventTypeSelector(PaymentBonusEvent.class), new EventListener<PaymentBonusEvent>() {
            @Override
            public void onEvent(PaymentBonusEvent event) {
                bonusLatch.add(event);
            }
        });
        AsyncCompletionUtils.check(new Check() {
            @Override
            public boolean check() {
                return A.paymentOperations().myTransactions().size() == 2;
            }
        }, 10_000);
        // Step 2. Fetching account and precondition
        PlayerAccount accountA = A.accountService().myAccount();
        assertTrue(accountA.getMoney().size() > 0);
        assertTrue(accountA.getMoney(Currency.FakeMoney).getAmount() > 0);
        // Step 3. Checking registration transaction
        A.paymentOperations().myTransactions();
        PaymentTransaction transaction = A.paymentOperations().getTransaction(A.getPlayer() + MoneySource.registration);
        Set<PaymentOperation> paymentOperations = transaction.getOperations();
        Money transactionAmount = paymentOperations.iterator().next().getAmount();
        // Step 4. Checking bonus transaction (Which might be delayed, because of the system event delays)
        PaymentTransaction bonusTransaction = AsyncCompletionUtils.get(new Get<PaymentTransaction>(){
            @Override
            public PaymentTransaction get() {
                return A.paymentOperations().myTransactions(PaymentBonusSource.dailybonus).get(0);
            }
        }, 5000);
        paymentOperations = bonusTransaction.getOperations();
        Money bonusAmmount = paymentOperations.iterator().next().getAmount();
        assertEquals(transactionAmount.add(bonusAmmount.getAmount()), A.accountService().myAccount().getMoney(Currency.FakeMoney));
    }

    @Test
    public void runningOutOfMoney() {
        // TODO can fail, because cash transactions are asynchronous (Need to manage this)
        final ClembleCasinoOperations A = playerOperations.createPlayer();
        final ClembleCasinoOperations B = playerOperations.createPlayer();

        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.GameConstructionInsufficientMoney));

        do {
            final Money cashAbefore = A.accountService().myAccount().getMoney(Currency.FakeMoney);
            final Money cashBbefore = B.accountService().myAccount().getMoney(Currency.FakeMoney);

            assertTrue("Unexpected amount " + cashAbefore.getAmount() + " for " + A.getPlayer(), cashAbefore.getAmount() >= 0);
            assertTrue("Unexpected amount " + cashBbefore.getAmount() + " for " + B.getPlayer(),cashBbefore.getAmount() >= 0);

            RoundGamePlayer AvsB = gameOperations.round(Game.num, A, B.getPlayer());
            RoundGamePlayer BvsA = gameOperations.accept(AvsB.getSessionKey(), B);

            AvsB.waitForStart();
            BvsA.waitForStart();

            AvsB.giveUp();

            AsyncCompletionUtils.check(new Check(){
                @Override
                public boolean check() {
                    return cashAbefore.add(-50).equals(A.accountService().myAccount().getMoney(Currency.FakeMoney));
                }
            }, 5_000);
            AsyncCompletionUtils.check(new Check(){
                @Override
                public boolean check() {
                    return cashBbefore.add(+50).equals(B.accountService().myAccount().getMoney(Currency.FakeMoney));
                }
            }, 5_000);
        } while (true);
    }

    @Ignore // TODO security was temporary disabled
    @Test
    public void testAmountAfterRegistration() {
        // Step 1. Creating player
        ClembleCasinoOperations player = playerOperations.createPlayer();
        // Step 2. Checking there is at least one
        PlayerAccount accountA = player.accountService().myAccount();
        // Step 3. Checking that there are some fake moneys in the newly created account
        assertNotNull(accountA);
        assertNotNull(accountA.getMoney(Currency.FakeMoney));
        assertTrue(accountA.getMoney(Currency.FakeMoney).getAmount() > 0);
        // Step 4. Checking that there are some fake moneys in the newly created account, accesed through WalletOperations
        PlayerAccount accountB = player.accountService().myAccount();
        assertNotNull(accountB);
        assertNotNull(accountB.getMoney(Currency.FakeMoney));
        assertEquals(accountB, accountA);
        // Step 5. Checking that there are some fake moneys in the newly created account, accesed through another WalletOperations
        PlayerAccount anotherWallet = player.accountService().myAccount();
        assertNotNull(anotherWallet);
        assertNotNull(anotherWallet.getMoney(Currency.FakeMoney));
        assertEquals(anotherWallet, accountA);
    }

    @Ignore // TODO security was temporary disabled
    @Test
    public void testTransactionsListAccess() {
        // Step 1. Checking player has no transactions to access
        ClembleCasinoOperations player = playerOperations.createPlayer();
        List<PaymentTransaction> transactions = player.paymentOperations().myTransactions();
        Assert.assertFalse(transactions.isEmpty());
        // Step 2. Checking no other player can't access the transactions
        ClembleCasinoOperations anotherPlayer = playerOperations.createPlayer();
        // Step 3. Checking no other player can access the transactions
        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.PaymentTransactionAccessDenied));
        player.paymentOperations().getTransaction(anotherPlayer.getPlayer() + MoneySource.registration);
    }

    @Test
    public void testAccessNonExistent() {
        // Step 1. Checking player has no transactions to access
        ClembleCasinoOperations player = playerOperations.createPlayer();
        assertNull(player.paymentOperations().getTransaction("TicTacToe-1"));
    }

}
