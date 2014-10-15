package com.clemble.casino.server.payment;

import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.casino.payment.PendingOperation;
import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.server.payment.repository.PlayerAccountRepository;
import com.clemble.casino.server.payment.repository.PlayerAccountTemplate;
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
    public PlayerAccountTemplate playerAccountTemplate;

    @Test
    public void testVersionUpdate() {
        String player = RandomStringUtils.randomAlphabetic(5);
        Map<Currency, Money> money = Collections.singletonMap(Currency.FakeMoney, Money.create(Currency.FakeMoney, 500));
        List<PendingOperation> pendingOperations = Collections.emptyList();
        PlayerAccount account = new PlayerAccount(player, money, pendingOperations, null);

        PlayerAccount saved = accountRepository.save(account);

        Assert.assertEquals(saved.getVersion(), Integer.valueOf(0));
    }

    @Test
    public void testUpdateInParallel() throws InterruptedException, ExecutionException {
        String player = RandomStringUtils.randomAlphabetic(5);
        Map<Currency, Money> money = Collections.singletonMap(Currency.FakeMoney, Money.create(Currency.FakeMoney, 500));
        List<PendingOperation> pendingOperations = Collections.emptyList();
        PlayerAccount account = new PlayerAccount(player, money, pendingOperations, null);

        PlayerAccount saved = accountRepository.save(account);

        // Executing changes in parallel
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        int summary = 0;
        List<Callable<PlayerAccount>> tasks = new ArrayList<Callable<PlayerAccount>>();
        for(int i = 0; i < 50; i++) {
            int amount = 1 + RANDOM.nextInt(20);
            summary += amount;
            Money debit = Money.create(Currency.FakeMoney, amount);
            tasks.add(new UpdateTask(player, debit, playerAccountTemplate));
        }
        List<Future<PlayerAccount>> futureTasks = executorService.invokeAll(tasks);
        for(Future<PlayerAccount> futureTask: futureTasks) {
            futureTask.get();
        }
        executorService.shutdown();

        Assert.assertEquals(playerAccountTemplate.findOne(player).getMoney(Currency.FakeMoney), Money.create(Currency.FakeMoney, 500 + summary));
    }

    @Test
    public void testFreezing() {
        // Step 1. Creating player account
        String player = RandomStringUtils.randomAlphabetic(5);
        Map<Currency, Money> money = Collections.singletonMap(Currency.FakeMoney, Money.create(Currency.FakeMoney, 500));
        List<PendingOperation> pendingOperations = Collections.emptyList();
        PlayerAccount account = new PlayerAccount(player, money, pendingOperations, null);
        PlayerAccount saved = accountRepository.save(account);
        Assert.assertEquals(saved.getVersion(), Integer.valueOf(0));
        // Step 2. Freezing some amount on the account
        PendingOperation pendingOperation = new PendingOperation(player, "test", Money.create(Currency.FakeMoney, 50));
        playerAccountTemplate.freeze(Collections.singletonList(player), "test", Money.create(Currency.FakeMoney, 50));
        // Step 3. Checking amount changed
        PlayerAccount another = playerAccountTemplate.findOne(player);
        Assert.assertEquals(another.getMoney(Currency.FakeMoney), Money.create(Currency.FakeMoney, 450));
        Assert.assertEquals(another.getPendingOperations().iterator().next(), pendingOperation);
    }

    public static class UpdateTask implements Callable<PlayerAccount> {

        final private String player;
        final private Money debit;
        final private PlayerAccountTemplate accountTemplate;

        public UpdateTask(String player, Money debit, PlayerAccountTemplate accountTemplate) {
            this.player = player;
            this.debit = debit;
            this.accountTemplate = accountTemplate;
        }

        @Override
        public PlayerAccount call() {
            accountTemplate.debit(player, debit);
            return accountTemplate.findOne(player);
        }
    }
}
