package com.gogomaya.server.player.wallet;

import javax.inject.Inject;

import junit.framework.Assert;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gogomaya.server.money.Currency;
import com.gogomaya.server.money.Money;
import com.gogomaya.server.money.MoneySource;
import com.gogomaya.server.money.Operation;
import com.gogomaya.server.spring.player.wallet.PlayerWalletManagementSpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = PlayerWalletManagementSpringConfiguration.class)
public class PlayerWalletManagerTest {

    @Inject
    public PlayerWalletRepository walletRepository;

    @Inject
    public WalletTransactionManager walletManager;

    private long playerFrom = RandomUtils.nextLong();
    private long playerTo = RandomUtils.nextLong();

    @Before
    public void initialize() {
        PlayerWallet walletFrom = new PlayerWallet().add(Money.create(Currency.FakeMoney, 100)).setPlayerId(playerFrom);

        PlayerWallet walletTo = new PlayerWallet().add(Money.create(Currency.FakeMoney, 50)).setPlayerId(playerTo);

        walletRepository.saveAndFlush(walletFrom);
        walletRepository.saveAndFlush(walletTo);
    }

    @Test
    public void testWalletUpdate() {
        Money ammount = Money.create(Currency.FakeMoney, RandomUtils.nextInt(100));

        WalletTransactionId transactionId = new WalletTransactionId().setSource(MoneySource.TicTacToe).setTransactionId(1L);

        WalletTransaction walletTransaction = new WalletTransaction().setTransactionId(transactionId)
                .addWalletOperation(new WalletOperation().setOperation(Operation.Credit).setPlayerId(playerFrom).setAmmount(ammount))
                .addWalletOperation(new WalletOperation().setOperation(Operation.Debit).setPlayerId(playerTo).setAmmount(ammount));

        walletManager.process(walletTransaction);

        Assert.assertEquals(walletRepository.findOne(playerTo).getMoney(Currency.FakeMoney).getAmount(), 50 + ammount.getAmount());
        Assert.assertEquals(walletRepository.findOne(playerFrom).getMoney(Currency.FakeMoney).getAmount(), 100 - ammount.getAmount());
    }

}
