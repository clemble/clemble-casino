package com.clemble.casino.server.payment;

import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.casino.money.Operation;
import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PendingTransaction;
import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.payment.repository.PlayerAccountRepository;
import com.clemble.casino.server.payment.repository.ServerAccountService;
import com.clemble.casino.server.payment.spring.PaymentSpringConfiguration;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;
import java.util.concurrent.*;

/**
 * Created by mavarazy on 15/10/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PaymentSpringConfiguration.class)
public class PlayerAccountRepositoryTest {

    final private Random RANDOM = new Random();

    @Autowired
    public PlayerAccountRepository accountRepository;

    @Autowired
    public ServerAccountService serverAccountService;

    @Test
    public void testVersionUpdate() {
        String player = RandomStringUtils.randomAlphabetic(5);
        Map<Currency, Money> money = Collections.singletonMap(Currency.FakeMoney, Money.create(Currency.FakeMoney, 500));
        PlayerAccount account = new PlayerAccount(player, money, null);

        PlayerAccount saved = accountRepository.save(account);

        Assert.assertEquals(saved.getVersion(), Integer.valueOf(0));
    }

    @Test
    public void testUpdateInParallel() throws InterruptedException, ExecutionException {
        String player = RandomStringUtils.randomAlphabetic(5);
        Map<Currency, Money> money = Collections.singletonMap(Currency.FakeMoney, Money.create(Currency.FakeMoney, 500));
        PlayerAccount account = new PlayerAccount(player, money, null);

        PlayerAccount saved = accountRepository.save(account);

        // Executing changes in parallel
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        int summary = 0;
        List<Callable<PlayerAccount>> tasks = new ArrayList<Callable<PlayerAccount>>();
        for(int i = 0; i < 50; i++) {
            int amount = 1 + RANDOM.nextInt(20);
            summary += amount;
            Money debit = Money.create(Currency.FakeMoney, amount);
            tasks.add(new UpdateTask(player, debit, serverAccountService));
        }
        List<Future<PlayerAccount>> futureTasks = executorService.invokeAll(tasks);
        for(Future<PlayerAccount> futureTask: futureTasks) {
            futureTask.get();
        }
        executorService.shutdown();

        Assert.assertEquals(serverAccountService.findOne(player).getMoney(Currency.FakeMoney), Money.create(Currency.FakeMoney, 500 + summary));
    }

    @Test
    public void testFreezing() {
        // Step 1. Creating player account
        String transactionKey = RandomStringUtils.randomAlphabetic(5);
        String player = RandomStringUtils.randomAlphabetic(5);
        Map<Currency, Money> money = Collections.singletonMap(Currency.FakeMoney, Money.create(Currency.FakeMoney, 500));
        PlayerAccount account = new PlayerAccount(player, money, null);
        PlayerAccount saved = accountRepository.save(account);
        Assert.assertEquals(saved.getVersion(), Integer.valueOf(0));
        // Step 2. Freezing some amount on the account
        serverAccountService.freeze(new PendingTransaction(transactionKey, Collections.singleton(new PaymentOperation(player, Money.create(Currency.FakeMoney, 50), Operation.Credit)), null));
        // Step 3. Checking amount changed
        PlayerAccount another = serverAccountService.findOne(player);
        Assert.assertEquals(another.getMoney(Currency.FakeMoney), Money.create(Currency.FakeMoney, 450));
    }

    @Test
    public void testDebitAfterFreezing() {
        // Step 1. Creating player account
        String transactionKey = RandomStringUtils.randomAlphabetic(5);
        String player = RandomStringUtils.randomAlphabetic(5);
        Map<Currency, Money> money = Collections.singletonMap(Currency.FakeMoney, Money.create(Currency.FakeMoney, 500));
        PlayerAccount account = new PlayerAccount(player, money, null);
        PlayerAccount saved = accountRepository.save(account);
        Assert.assertEquals(saved.getVersion(), Integer.valueOf(0));
        // Step 2. Freezing some amount on the account
        serverAccountService.freeze(new PendingTransaction(transactionKey, Collections.singleton(new PaymentOperation(player, Money.create(Currency.FakeMoney, 50), Operation.Credit)), null));
        // Step 3. Checking amount changed
        PlayerAccount another = serverAccountService.findOne(player);
        Assert.assertEquals(another.getMoney(Currency.FakeMoney), Money.create(Currency.FakeMoney, 450));
        // Step 4. Processing Debit for this operation
        PaymentTransaction transaction = new PaymentTransaction().setTransactionKey(transactionKey).
            addOperation(new PaymentOperation(player, Money.create(Currency.FakeMoney, 50), Operation.Debit)).
            addOperation(new PaymentOperation(PlayerAware.DEFAULT_PLAYER, Money.create(Currency.FakeMoney, 50), Operation.Credit));
        serverAccountService.process(transaction);
        PlayerAccount afterProcessing = serverAccountService.findOne(player);
        // Step 5. Checking final amount return to the original
        Assert.assertEquals(afterProcessing.getMoney(Currency.FakeMoney), Money.create(Currency.FakeMoney, 550));
    }

    @Test
    public void testCreditAfterFreezing() {
        // Step 1. Creating player account
        String transactionKey = RandomStringUtils.randomAlphabetic(5);
        String player = RandomStringUtils.randomAlphabetic(5);
        Map<Currency, Money> money = Collections.singletonMap(Currency.FakeMoney, Money.create(Currency.FakeMoney, 500));
        PlayerAccount account = new PlayerAccount(player, money, null);
        PlayerAccount saved = accountRepository.save(account);
        Assert.assertEquals(saved.getVersion(), Integer.valueOf(0));
        // Step 2. Freezing some amount on the account
        serverAccountService.freeze(new PendingTransaction(transactionKey, Collections.singleton(new PaymentOperation(player, Money.create(Currency.FakeMoney, 50), Operation.Credit)), null));
        // Step 3. Checking amount changed
        PlayerAccount another = serverAccountService.findOne(player);
        Assert.assertEquals(another.getMoney(Currency.FakeMoney), Money.create(Currency.FakeMoney, 450));
        // Step 4. Processing Debit for this operation
        PaymentTransaction transaction = new PaymentTransaction().setTransactionKey(transactionKey).
            addOperation(new PaymentOperation(player, Money.create(Currency.FakeMoney, 50), Operation.Credit)).
            addOperation(new PaymentOperation(PlayerAware.DEFAULT_PLAYER, Money.create(Currency.FakeMoney, 50), Operation.Debit));
        serverAccountService.process(transaction);
        PlayerAccount afterProcessing = serverAccountService.findOne(player);
        // Step 5. Checking final amount return to the original
        Assert.assertEquals(afterProcessing.getMoney(Currency.FakeMoney), Money.create(Currency.FakeMoney, 450));
    }

    public static class UpdateTask implements Callable<PlayerAccount> {

        final private PaymentOperation operation;
        final private ServerAccountService accountTemplate;

        public UpdateTask(String player, Money debit, ServerAccountService accountTemplate) {
            this.operation = new PaymentOperation(player, debit, Operation.Debit);
            this.accountTemplate = accountTemplate;
        }

        @Override
        public PlayerAccount call() {
            PaymentOperation anothetOperation = operation.toOpposite();
            // Step 1. Processing account operation
            PaymentTransaction transaction = new PaymentTransaction().setTransactionKey(RandomStringUtils.randomAlphabetic(10)).
                addOperation(operation).
                addOperation(new PaymentOperation(PlayerAware.DEFAULT_PLAYER, anothetOperation.getAmount(), anothetOperation.getOperation()));
            accountTemplate.process(transaction);
            // Step 2. Returning player account
            return accountTemplate.findOne(operation.getPlayer());
        }
    }
}
