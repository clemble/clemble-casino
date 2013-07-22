package com.gogomaya.server;

import java.util.Date;

import org.apache.commons.lang.RandomStringUtils;

import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.construct.AutomaticGameRequest;
import com.gogomaya.server.game.construct.GameConstruction;
import com.gogomaya.server.game.construct.GameConstructionState;
import com.gogomaya.server.game.event.client.GiveUpEvent;
import com.gogomaya.server.game.event.server.GameStartedEvent;
import com.gogomaya.server.game.rule.bet.FixedBetRule;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.tictactoe.TicTacToeState;
import com.gogomaya.server.game.tictactoe.event.client.TicTacToeBetOnCellEvent;
import com.gogomaya.server.money.Currency;
import com.gogomaya.server.money.Money;
import com.gogomaya.server.money.MoneySource;
import com.gogomaya.server.money.Operation;
import com.gogomaya.server.payment.PaymentOperation;
import com.gogomaya.server.payment.PaymentTransaction;
import com.gogomaya.server.payment.PaymentTransactionId;
import com.gogomaya.server.player.PlayerCategory;
import com.gogomaya.server.player.PlayerGender;
import com.gogomaya.server.player.PlayerProfile;
import com.gogomaya.server.player.account.PlayerAccount;
import com.gogomaya.server.player.security.PlayerCredential;
import com.google.common.collect.ImmutableList;
import com.stresstest.random.AbstractValueGenerator;
import com.stresstest.random.ObjectGenerator;

public class ObjectTest {

    static {
        ObjectGenerator.register(FixedBetRule.class, new AbstractValueGenerator<FixedBetRule>() {
            @Override
            public FixedBetRule generate() {
                return new FixedBetRule(100);
            }

        });
        ObjectGenerator.register(TicTacToeState.class, new AbstractValueGenerator<TicTacToeState>() {
            public TicTacToeState generate() {
                return new TicTacToeState();
            }

        });
        ObjectGenerator.register(ClientEvent.class, new AbstractValueGenerator<ClientEvent>() {

            @Override
            public ClientEvent generate() {
                return new GiveUpEvent(-1L);
            }
        });
        ObjectGenerator.register(TicTacToeBetOnCellEvent.class, new AbstractValueGenerator<TicTacToeBetOnCellEvent>() {

            @Override
            public TicTacToeBetOnCellEvent generate() {
                return new TicTacToeBetOnCellEvent(-1L, 100);
            }

        });
        ObjectGenerator.register(GameStartedEvent.class, new AbstractValueGenerator<GameStartedEvent>() {

            @Override
            public GameStartedEvent generate() {
                GameStartedEvent<TicTacToeState> gameStartedEvent = new GameStartedEvent<>();
                gameStartedEvent.setSession(10L);
                gameStartedEvent.setState(new TicTacToeState());
                return gameStartedEvent;
            }

        });
        ObjectGenerator.register(PlayerAccount.class, new AbstractValueGenerator<PlayerAccount>() {

            @Override
            public PlayerAccount generate() {
                return new PlayerAccount().setPlayerId(0).add(Money.create(Currency.FakeMoney, 500));
            }

        });
        ObjectGenerator.register(PaymentTransaction.class, new AbstractValueGenerator<PaymentTransaction>() {

            @Override
            public PaymentTransaction generate() {
                return new PaymentTransaction()
                        .setTransactionId(new PaymentTransactionId().setSource(MoneySource.TicTacToe).setTransactionId(0))
                        .addPaymentOperation(
                                new PaymentOperation().setAmmount(Money.create(Currency.FakeMoney, 50)).setOperation(Operation.Credit).setPlayerId(0))
                        .addPaymentOperation(
                                new PaymentOperation().setAmmount(Money.create(Currency.FakeMoney, 50)).setOperation(Operation.Debit).setPlayerId(1));
            }

        });
        ObjectGenerator.register(PlayerCredential.class, new AbstractValueGenerator<PlayerCredential>() {

            @Override
            public PlayerCredential generate() {
                return new PlayerCredential().setEmail(RandomStringUtils.randomAlphabetic(10) + "@gmail.com").setPassword(RandomStringUtils.random(10));
            }

        });
        ObjectGenerator.register(PlayerProfile.class, new AbstractValueGenerator<PlayerProfile>() {

            @Override
            public PlayerProfile generate() {
                return new PlayerProfile().setBirthDate(new Date(0)).setCategory(PlayerCategory.Amateur).setFirstName(RandomStringUtils.randomAlphabetic(10))
                        .setGender(PlayerGender.M).setLastName(RandomStringUtils.randomAlphabetic(10)).setNickName(RandomStringUtils.randomAlphabetic(10));
            }

        });
        ObjectGenerator.register(GameConstruction.class, new AbstractValueGenerator<GameConstruction>() {

            @Override
            public GameConstruction generate() {
                return new GameConstruction()
                    .setRequest(new AutomaticGameRequest(1, GameSpecification.DEFAULT))
                    .setResponses(new ActionLatch(ImmutableList.<Long>of(1L, 2L),"response"))
                    .setState(GameConstructionState.pending);
            }
        });
    }

}
