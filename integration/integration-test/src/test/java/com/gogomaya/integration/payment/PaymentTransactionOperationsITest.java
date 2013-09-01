package com.gogomaya.integration.payment;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gogomaya.error.GogomayaError;
import com.gogomaya.money.Currency;
import com.gogomaya.money.Money;
import com.gogomaya.money.MoneySource;
import com.gogomaya.money.Operation;
import com.gogomaya.payment.PaymentOperation;
import com.gogomaya.payment.PaymentTransaction;
import com.gogomaya.payment.PaymentTransactionId;
import com.gogomaya.server.integration.payment.PaymentTransactionOperations;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.integration.player.PlayerOperations;
import com.gogomaya.server.integration.util.GogomayaExceptionMatcherFactory;
import com.gogomaya.server.spring.integration.TestConfiguration;
import com.gogomaya.server.test.RedisCleaner;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { TestConfiguration.class })
@TestExecutionListeners(listeners = { RedisCleaner.class, DependencyInjectionTestExecutionListener.class })
public class PaymentTransactionOperationsITest {

    @Inject
    public PaymentTransactionOperations paymentTransactionOperations;

    @Inject
    public PlayerOperations playerOperations;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testFakePaymentTransaction() {
        PaymentTransaction paymentTransaction = new PaymentTransaction().setTransactionId(new PaymentTransactionId(MoneySource.TicTacToe.name(), 2432))
                .addPaymentOperation(new PaymentOperation().setOperation(Operation.Credit).setPlayerId(-1).setAmount(Money.create(Currency.FakeMoney, 50)))
                .addPaymentOperation(new PaymentOperation().setOperation(Operation.Debit).setPlayerId(-2).setAmount(Money.create(Currency.FakeMoney, 50)));

        expectedException.expect(GogomayaExceptionMatcherFactory.fromErrors(GogomayaError.PaymentTransactionUnknownPlayers));

        paymentTransactionOperations.perform(paymentTransaction);
    }

    @Test
    public void testInValidPaymentTransaction() {
        Player player = playerOperations.createPlayer();
        Player anotherPlayer = playerOperations.createPlayer();

        PaymentTransaction paymentTransaction = new PaymentTransaction()
                .setTransactionId(new PaymentTransactionId(MoneySource.TicTacToe.name(), 2432))
                .addPaymentOperation(
                        new PaymentOperation().setOperation(Operation.Credit).setPlayerId(player.getPlayerId())
                                .setAmount(Money.create(Currency.FakeMoney, 60)))
                .addPaymentOperation(
                        new PaymentOperation().setOperation(Operation.Debit).setPlayerId(anotherPlayer.getPlayerId())
                                .setAmount(Money.create(Currency.FakeMoney, 50)));

        expectedException.expect(GogomayaExceptionMatcherFactory.fromErrors(GogomayaError.PaymentTransactionInvalid));

        paymentTransactionOperations.perform(paymentTransaction);
    }

    @Test
    public void testValidTransaction() {
        Player player = playerOperations.createPlayer();
        Player anotherPlayer = playerOperations.createPlayer();

        PaymentTransaction paymentTransaction = new PaymentTransaction()
                .setTransactionId(new PaymentTransactionId(MoneySource.TicTacToe.name(), 2432))
                .addPaymentOperation(
                        new PaymentOperation().setOperation(Operation.Credit).setPlayerId(player.getPlayerId())
                                .setAmount(Money.create(Currency.FakeMoney, 50)))
                .addPaymentOperation(
                        new PaymentOperation().setOperation(Operation.Debit).setPlayerId(anotherPlayer.getPlayerId())
                                .setAmount(Money.create(Currency.FakeMoney, 50)));

        PaymentTransaction savedPaymentTransaction = paymentTransactionOperations.perform(paymentTransaction);

        assertEquals(savedPaymentTransaction, paymentTransaction);
    }

    @Test
    public void testValidTransactionAccess() {
        String source = MoneySource.TicTacToe.name();
        long transactionId = 2323;

        Player player = playerOperations.createPlayer();
        Player anotherPlayer = playerOperations.createPlayer();

        PaymentTransaction paymentTransaction = new PaymentTransaction()
                .setTransactionId(new PaymentTransactionId(source, transactionId))
                .addPaymentOperation(
                        new PaymentOperation().setOperation(Operation.Credit).setPlayerId(player.getPlayerId())
                                .setAmount(Money.create(Currency.FakeMoney, 50)))
                .addPaymentOperation(
                        new PaymentOperation().setOperation(Operation.Debit).setPlayerId(anotherPlayer.getPlayerId())
                                .setAmount(Money.create(Currency.FakeMoney, 50)));

        PaymentTransaction savedPaymentTransaction = paymentTransactionOperations.perform(paymentTransaction);

        assertEquals(savedPaymentTransaction, paymentTransaction);
        assertEquals(paymentTransaction, paymentTransactionOperations.get(player, source, transactionId));
        assertEquals(paymentTransaction, paymentTransactionOperations.get(anotherPlayer, source, transactionId));
    }

    @Test
    public void testInValidTransactionAccess() {
        String source = MoneySource.TicTacToe.name();
        long transactionId = 2323;

        Player player = playerOperations.createPlayer();
        Player anotherPlayer = playerOperations.createPlayer();
        Player therdPlayer = playerOperations.createPlayer();

        PaymentTransaction paymentTransaction = new PaymentTransaction()
                .setTransactionId(new PaymentTransactionId(source, transactionId))
                .addPaymentOperation(
                        new PaymentOperation().setOperation(Operation.Credit).setPlayerId(player.getPlayerId())
                                .setAmount(Money.create(Currency.FakeMoney, 50)))
                .addPaymentOperation(
                        new PaymentOperation().setOperation(Operation.Debit).setPlayerId(anotherPlayer.getPlayerId())
                                .setAmount(Money.create(Currency.FakeMoney, 50)));

        PaymentTransaction savedPaymentTransaction = paymentTransactionOperations.perform(paymentTransaction);

        assertEquals(savedPaymentTransaction, paymentTransaction);
        assertEquals(paymentTransaction, paymentTransactionOperations.get(player, source, transactionId));
        assertEquals(paymentTransaction, paymentTransactionOperations.get(anotherPlayer, source, transactionId));

        expectedException.expect(GogomayaExceptionMatcherFactory.fromErrors(GogomayaError.PaymentTransactionAccessDenied));
        assertEquals(paymentTransaction, paymentTransactionOperations.get(therdPlayer, source, transactionId));
    }

    @Test
    public void testRegistrationTransaction() {
        // Step 1. Creating player
        Player player = playerOperations.createPlayer();
        // Step 2. Checking account exists
        PaymentTransaction paymentTransaction = paymentTransactionOperations.get(player, MoneySource.Registration, player.getPlayerId());
        Collection<PaymentOperation> associatedOperation = new ArrayList<>();
        for (PaymentOperation paymentOperation : paymentTransaction.getPaymentOperations()) {
            if (paymentOperation.getPlayerId() == player.getPlayerId()) {
                associatedOperation.add(paymentOperation);
            }
        }
        // Step 3. Checking there is at list one operation available
        Assert.assertTrue(associatedOperation.size() > 0);
        for (PaymentOperation operation : associatedOperation)
            assertEquals(player.getWalletOperations().getAccount().getMoney(operation.getAmount().getCurrency()), operation.getAmount());
    }

}
