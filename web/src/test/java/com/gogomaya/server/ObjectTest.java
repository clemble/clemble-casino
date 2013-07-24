package com.gogomaya.server;

import java.util.Collections;
import java.util.Date;

import org.apache.commons.lang.RandomStringUtils;

import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.Game;
import com.gogomaya.server.game.configuration.GameRuleOptions;
import com.gogomaya.server.game.configuration.SelectRuleOptions;
import com.gogomaya.server.game.construct.AutomaticGameRequest;
import com.gogomaya.server.game.construct.GameConstruction;
import com.gogomaya.server.game.construct.GameConstructionState;
import com.gogomaya.server.game.event.client.BetEvent;
import com.gogomaya.server.game.event.client.surrender.GiveUpEvent;
import com.gogomaya.server.game.event.server.GameStartedEvent;
import com.gogomaya.server.game.rule.GameRule;
import com.gogomaya.server.game.rule.bet.BetRule;
import com.gogomaya.server.game.rule.bet.FixedBetRule;
import com.gogomaya.server.game.rule.bet.LimitedBetRule;
import com.gogomaya.server.game.rule.bet.UnlimitedBetRule;
import com.gogomaya.server.game.rule.construction.PlayerNumberRule;
import com.gogomaya.server.game.rule.construction.PrivacyRule;
import com.gogomaya.server.game.rule.giveup.GiveUpRule;
import com.gogomaya.server.game.rule.time.MoveTimeRule;
import com.gogomaya.server.game.rule.time.TotalTimeRule;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.tictactoe.TicTacToeState;
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
        ObjectGenerator.register(BetEvent.class, new AbstractValueGenerator<BetEvent>() {

            @Override
            public BetEvent generate() {
                return new BetEvent(-1L, 100);
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
        ObjectGenerator.register(GameRule.class, new AbstractValueGenerator<GameRule>() {

            @Override
            public GameRule generate() {
                return UnlimitedBetRule.INSTANCE;
            }
        });
        ObjectGenerator.register(GameConstruction.class, new AbstractValueGenerator<GameConstruction>() {

            @Override
            public GameConstruction generate() {
                return new GameConstruction().setRequest(new AutomaticGameRequest(1, GameSpecification.DEFAULT))
                        .setResponses(new ActionLatch(ImmutableList.<Long> of(1L, 2L), "response")).setState(GameConstructionState.pending);
            }
        });

        ObjectGenerator.register(SelectRuleOptions.class, new AbstractValueGenerator<SelectRuleOptions>() {

            @Override
            public SelectRuleOptions generate() {
                return new SelectRuleOptions(Game.pic, Collections.singleton(Money.create(Currency.FakeMoney, 50)), new GameRuleOptions<BetRule>(
                        new FixedBetRule(50), new FixedBetRule(100), new FixedBetRule(200)), GiveUpRule.DEFAULT_OPTIONS, PlayerNumberRule.DEFAULT_OPTIONS,
                        PrivacyRule.DEFAULT_OPTIONS, MoveTimeRule.DEFAULT_OPTIONS, TotalTimeRule.DEFAULT_OPTIONS);
            }
        });
        ObjectGenerator.register(LimitedBetRule.class, new AbstractValueGenerator<LimitedBetRule>() {

            @Override
            public LimitedBetRule generate() {
                return LimitedBetRule.create(10, 200);
            }
        });
        ObjectGenerator.register(GameSpecification.class, new AbstractValueGenerator<GameSpecification>() {

            @Override
            public GameSpecification generate() {
                return GameSpecification.DEFAULT;
            }

        });
    }

}
