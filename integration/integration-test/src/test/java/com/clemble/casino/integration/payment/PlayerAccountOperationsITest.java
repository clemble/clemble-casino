package com.clemble.casino.integration.payment;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.clemble.casino.integration.game.RoundGamePlayer;
import com.clemble.casino.payment.bonus.PaymentBonusSource;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.test.concurrent.Get;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.event.EventListener;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameState;
import com.clemble.casino.integration.game.construction.GameScenarios;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.clemble.casino.integration.util.ClembleCasinoExceptionMatcherFactory;
import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.payment.event.BonusPaymentEvent;
import com.clemble.casino.payment.money.Currency;
import com.clemble.casino.payment.money.Money;
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
        final BlockingQueue<BonusPaymentEvent> bonusLatch = new ArrayBlockingQueue<>(2);
        A.listenerOperations().subscribe(new EventTypeSelector(BonusPaymentEvent.class), new EventListener<BonusPaymentEvent>() {
            @Override
            public void onEvent(BonusPaymentEvent event) {
                bonusLatch.add(event);
            }
        });
        AsyncCompletionUtils.check(new Check() {
            @Override
            public boolean check() {
                return A.paymentOperations().getPaymentTransactions().size() == 2;
            }
        }, 10_000);
        // Step 2. Fetching account and precondition
        PlayerAccount accountA = A.paymentOperations().getAccount();
        assertTrue(accountA.getMoney().size() > 0);
        assertTrue(accountA.getMoney(Currency.FakeMoney).getAmount() > 0);
        // Step 3. Checking registration transaction
        A.paymentOperations().getPaymentTransactions();
        PaymentTransaction transaction = A.paymentOperations().getPaymentTransaction("registration", A.getPlayer());
        Set<PaymentOperation> paymentOperations = transaction.getPaymentOperations();
        Money transactionAmount = paymentOperations.iterator().next().getAmount();
        // Step 4. Checking bonus transaction (Which might be delayed, because of the system event delays)
        PaymentTransaction bonusTransaction = AsyncCompletionUtils.get(new Get<PaymentTransaction>(){
            @Override
            public PaymentTransaction get() {
                return A.paymentOperations().getPaymentTransactions(PaymentBonusSource.dailybonus).get(0);
            }
        }, 5000);
        paymentOperations = bonusTransaction.getPaymentOperations();
        Money bonusAmmount = paymentOperations.iterator().next().getAmount();
        assertEquals(transactionAmount.add(bonusAmmount.getAmount()), A.paymentOperations().getAccount().getMoney(Currency.FakeMoney));
    }

    @Test
    public void runingOutOfMoney() {
        // TODO can fail, because cash transactions are asynchronous (Need to manage this)
        ClembleCasinoOperations A = playerOperations.createPlayer();
        ClembleCasinoOperations B = playerOperations.createPlayer();

        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.GameConstructionInsufficientMoney));

        do {
            Money cashAbefore = A.paymentOperations().getAccount().getMoney(Currency.FakeMoney);
            Money cashBbefore = B.paymentOperations().getAccount().getMoney(Currency.FakeMoney);

            assertTrue(cashAbefore.getAmount() >= 0);
            assertTrue(cashBbefore.getAmount() >= 0);

            RoundGamePlayer<GameState> AvsB = gameOperations.round(Game.num, A, B.getPlayer());
            RoundGamePlayer<GameState> BvsA = gameOperations.accept(AvsB.getSession(), B);

            AvsB.waitForStart();
            BvsA.waitForStart();

            AvsB.giveUp();

            assertEquals(cashAbefore.add(-50), A.paymentOperations().getAccount().getMoney(Currency.FakeMoney));
            assertEquals(cashBbefore.add(+50), B.paymentOperations().getAccount().getMoney(Currency.FakeMoney));
        } while (true);
    }

    @Ignore // TODO security was temporary disabled
    @Test
    public void testAmountAfterRegistration() {
        // Step 1. Creating player
        ClembleCasinoOperations player = playerOperations.createPlayer();
        // Step 2. Checking there is at least one
        PlayerAccount accountA = player.paymentOperations().getAccount();
        // Step 3. Checking that there are some fake moneys in the newly created account
        assertNotNull(accountA);
        assertNotNull(accountA.getMoney(Currency.FakeMoney));
        assertTrue(accountA.getMoney(Currency.FakeMoney).getAmount() > 0);
        // Step 4. Checking that there are some fake moneys in the newly created account, accesed through WalletOperations
        PlayerAccount accountB = player.paymentOperations().getAccount();
        assertNotNull(accountB);
        assertNotNull(accountB.getMoney(Currency.FakeMoney));
        assertEquals(accountB, accountA);
        // Step 5. Checking that there are some fake moneys in the newly created account, accesed through another WalletOperations
        PlayerAccount anotherWallet = player.paymentOperations().getAccount();
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
        ClembleCasinoOperations player = playerOperations.createPlayer();
        List<PaymentTransaction> transactions = player.paymentOperations().getPaymentTransactions();
        Assert.assertFalse(transactions.isEmpty());
        // Step 2. Checking no other player can't access the transactions
        ClembleCasinoOperations anotherPlayer = playerOperations.createPlayer();
        // TODO Assert.assertFalse(player.getWalletOperations().getPaymentTransaction(MoneySource.Registration, player.getPlayer()));
        // Step 3. Checking no other player can access the transactions
        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.PaymentTransactionAccessDenied));
        player.paymentOperations().getPaymentTransaction("registration", anotherPlayer.getPlayer());
    }

    @Test
    public void testAccessNonExistent() {
        // Step 1. Checking player has no transactions to access
        ClembleCasinoOperations player = playerOperations.createPlayer();
        assertNull(player.paymentOperations().getPaymentTransaction("TicTacToe", "-1"));
    }

}
