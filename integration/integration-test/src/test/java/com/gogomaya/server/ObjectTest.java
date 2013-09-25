package com.gogomaya.server;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import com.gogomaya.VersionAware;
import com.gogomaya.base.ActionLatch;
import com.gogomaya.event.ClientEvent;
import com.gogomaya.game.Game;
import com.gogomaya.game.GameSessionKey;
import com.gogomaya.game.account.GameAccount;
import com.gogomaya.game.account.GameAccountFactory;
import com.gogomaya.game.configuration.GameRuleOptions;
import com.gogomaya.game.configuration.SelectRuleOptions;
import com.gogomaya.game.construct.AutomaticGameRequest;
import com.gogomaya.game.construct.GameConstruction;
import com.gogomaya.game.construct.GameConstructionState;
import com.gogomaya.game.construct.GameInitiation;
import com.gogomaya.game.event.client.BetEvent;
import com.gogomaya.game.event.client.surrender.GiveUpEvent;
import com.gogomaya.game.outcome.GameOutcome;
import com.gogomaya.game.outcome.NoOutcome;
import com.gogomaya.game.rule.GameRule;
import com.gogomaya.game.rule.bet.BetRule;
import com.gogomaya.game.rule.bet.FixedBetRule;
import com.gogomaya.game.rule.bet.LimitedBetRule;
import com.gogomaya.game.rule.bet.UnlimitedBetRule;
import com.gogomaya.game.rule.construct.PlayerNumberRule;
import com.gogomaya.game.rule.construct.PrivacyRule;
import com.gogomaya.game.rule.giveup.GiveUpRule;
import com.gogomaya.game.rule.time.MoveTimeRule;
import com.gogomaya.game.rule.time.TotalTimeRule;
import com.gogomaya.game.specification.GameSpecification;
import com.gogomaya.money.Currency;
import com.gogomaya.money.Money;
import com.gogomaya.money.MoneySource;
import com.gogomaya.money.Operation;
import com.gogomaya.payment.PaymentOperation;
import com.gogomaya.payment.PaymentTransaction;
import com.gogomaya.payment.PaymentTransactionKey;
import com.gogomaya.payment.PlayerAccount;
import com.gogomaya.player.PlayerCategory;
import com.gogomaya.player.PlayerGender;
import com.gogomaya.player.PlayerProfile;
import com.gogomaya.player.security.PlayerCredential;
import com.gogomaya.server.integration.NumberState;
import com.gogomaya.server.player.notification.SimplePlayerNotificationRegistry;
import com.google.common.collect.ImmutableList;
import com.stresstest.random.AbstractValueGenerator;
import com.stresstest.random.ObjectGenerator;
import com.stresstest.random.ValueGenerator;

public class ObjectTest {

    static {
        ObjectGenerator.register(FixedBetRule.class, new AbstractValueGenerator<FixedBetRule>() {
            @Override
            public FixedBetRule generate() {
                return FixedBetRule.create(new long[] { 10 });
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
                        .setTransactionKey(new PaymentTransactionKey().setSource(MoneySource.TicTacToe).setTransactionId(0))
                        .addPaymentOperation(
                                new PaymentOperation().setAmount(Money.create(Currency.FakeMoney, 50)).setOperation(Operation.Credit).setPlayerId(0))
                        .addPaymentOperation(
                                new PaymentOperation().setAmount(Money.create(Currency.FakeMoney, 50)).setOperation(Operation.Debit).setPlayerId(1));
            }

        });
        ObjectGenerator.register(PlayerCredential.class, new AbstractValueGenerator<PlayerCredential>() {

            @Override
            public PlayerCredential generate() {
                return new PlayerCredential().setEmail(org.apache.commons.lang3.RandomStringUtils.randomAlphabetic(10) + "@gmail.com").setPassword(
                        RandomStringUtils.random(10));
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
                return new GameConstruction()
                    .setSession(new GameSessionKey(Game.pic, 0))
                    .setRequest(new AutomaticGameRequest(1, GameSpecification.DEFAULT))
                    .setResponses(new ActionLatch(ImmutableList.<Long> of(1L, 2L), "response"))
                    .setState(GameConstructionState.pending);
            }
        });

        ObjectGenerator.register(SelectRuleOptions.class, new AbstractValueGenerator<SelectRuleOptions>() {

            @Override
            public SelectRuleOptions generate() {
                return new SelectRuleOptions(Game.pic, Collections.singleton(Money.create(Currency.FakeMoney, 50)), new GameRuleOptions<BetRule>(
                        FixedBetRule.DEFAULT), GiveUpRule.DEFAULT_OPTIONS, PlayerNumberRule.DEFAULT_OPTIONS, PrivacyRule.DEFAULT_OPTIONS,
                        MoveTimeRule.DEFAULT_OPTIONS, TotalTimeRule.DEFAULT_OPTIONS);
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
        ObjectGenerator.register(StubGameState.class, new AbstractValueGenerator<StubGameState>() {

            @Override
            public StubGameState generate() {
                GameAccount account = GameAccountFactory.create(new GameInitiation(new GameSessionKey(Game.pac, 1L), ImmutableList.<Long> of(1L, 2L), GameSpecification.DEFAULT));
                ActionLatch actionLatch = new ActionLatch(ImmutableList.<Long> of(1L, 2L), "stub");
                GameOutcome outcome = new NoOutcome();
                return new StubGameState(account, actionLatch, outcome, 1);
            }

        });
        ObjectGenerator.register(NumberState.class, new AbstractValueGenerator<NumberState>() {

            @Override
            public NumberState generate() {
                return new NumberState(null, null, null);
            }

        });
        ObjectGenerator.register(ServerRegistry.class, new AbstractValueGenerator<ServerRegistry>() {

            @Override
            public ServerRegistry generate() {
                return new ServerRegistry(ImmutableList.<Map.Entry<String, String>> of(new ImmutablePair<String, String>("1000000", "host.me")));
            }
        });
        ObjectGenerator.register(SimplePlayerNotificationRegistry.class, new AbstractValueGenerator<SimplePlayerNotificationRegistry>() {

            @Override
            public SimplePlayerNotificationRegistry generate() {
                return new SimplePlayerNotificationRegistry(ObjectGenerator.generate(ServerRegistry.class));
            }
        });
        ObjectGenerator.register(VersionAware.class, "version", new ValueGenerator<Integer>() {
            @Override
            public Integer generate() {
                return 0;
            }

            @Override
            public int scope() {
                return 1;
            }
            
            public ValueGenerator<Integer> clone() {
                return this;
            }
        });
    }

}
