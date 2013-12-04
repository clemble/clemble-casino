package com.clemble.casino.integration;

import java.util.Collections;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;

import com.clemble.casino.VersionAware;
import com.clemble.casino.base.ActionLatch;
import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.account.GameAccount;
import com.clemble.casino.game.account.GameAccountFactory;
import com.clemble.casino.game.configuration.GameRuleOptions;
import com.clemble.casino.game.configuration.SelectRuleOptions;
import com.clemble.casino.game.construct.AutomaticGameRequest;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.construct.GameConstructionState;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.event.client.BetEvent;
import com.clemble.casino.game.event.client.surrender.GiveUpEvent;
import com.clemble.casino.game.outcome.GameOutcome;
import com.clemble.casino.game.outcome.NoOutcome;
import com.clemble.casino.game.rule.GameRule;
import com.clemble.casino.game.rule.bet.BetRule;
import com.clemble.casino.game.rule.bet.FixedBetRule;
import com.clemble.casino.game.rule.bet.LimitedBetRule;
import com.clemble.casino.game.rule.bet.UnlimitedBetRule;
import com.clemble.casino.game.rule.construct.PlayerNumberRule;
import com.clemble.casino.game.rule.construct.PrivacyRule;
import com.clemble.casino.game.rule.giveup.GiveUpRule;
import com.clemble.casino.game.rule.time.MoveTimeRule;
import com.clemble.casino.game.rule.time.TotalTimeRule;
import com.clemble.casino.game.specification.GameSpecification;
import com.clemble.casino.integration.game.NumberState;
import com.clemble.casino.money.MoneySource;
import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PaymentTransactionKey;
import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.payment.money.Currency;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.payment.money.Operation;
import com.clemble.casino.player.NativePlayerProfile;
import com.clemble.casino.player.PlayerCategory;
import com.clemble.casino.player.PlayerGender;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.security.PlayerCredential;
import com.clemble.test.random.AbstractValueGenerator;
import com.clemble.test.random.ObjectGenerator;
import com.clemble.test.random.ValueGenerator;
import com.google.common.collect.ImmutableList;

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
                return new GiveUpEvent(RandomStringUtils.random(5));
            }
        });
        ObjectGenerator.register(BetEvent.class, new AbstractValueGenerator<BetEvent>() {

            @Override
            public BetEvent generate() {
                return new BetEvent(RandomStringUtils.random(5), 100);
            }

        });
        ObjectGenerator.register(PlayerAccount.class, new AbstractValueGenerator<PlayerAccount>() {

            @Override
            public PlayerAccount generate() {
                return new PlayerAccount().setPlayer(RandomStringUtils.random(5)).add(Money.create(Currency.FakeMoney, 500));
            }

        });
        ObjectGenerator.register(PaymentTransaction.class, new AbstractValueGenerator<PaymentTransaction>() {

            @Override
            public PaymentTransaction generate() {
                return new PaymentTransaction()
                        .setTransactionKey(new PaymentTransactionKey().setSource(MoneySource.TicTacToe).setTransaction(RandomStringUtils.random(5)))
                        .addPaymentOperation(
                                new PaymentOperation().setAmount(Money.create(Currency.FakeMoney, 50)).setOperation(Operation.Credit).setPlayer(RandomStringUtils.random(5)))
                        .addPaymentOperation(
                                new PaymentOperation().setAmount(Money.create(Currency.FakeMoney, 50)).setOperation(Operation.Debit).setPlayer(RandomStringUtils.random(5)));
            }

        });
        ObjectGenerator.register(PlayerCredential.class, new AbstractValueGenerator<PlayerCredential>() {

            @Override
            public PlayerCredential generate() {
                return new PlayerCredential()
                    .setEmail(RandomStringUtils.randomAlphabetic(10) + "@gmail.com")
                    .setPassword(RandomStringUtils.random(10))
                    .setPlayer(RandomStringUtils.random(5));
            }

        });
        ObjectGenerator.register(PlayerProfile.class, new AbstractValueGenerator<PlayerProfile>() {

            @Override
            public PlayerProfile generate() {
                return new NativePlayerProfile().setBirthDate(new Date(0)).setCategory(PlayerCategory.Amateur).setFirstName(RandomStringUtils.randomAlphabetic(10))
                        .setGender(PlayerGender.M).setLastName(RandomStringUtils.randomAlphabetic(10)).setNickName(RandomStringUtils.randomAlphabetic(10)).setPlayer(RandomStringUtils.random(5));
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
                    .setSession(new GameSessionKey(Game.pic, "0"))
                    .setRequest(new AutomaticGameRequest(RandomStringUtils.random(5), GameSpecification.DEFAULT))
                    .setResponses(new ActionLatch(ImmutableList.<String> of(RandomStringUtils.random(5), RandomStringUtils.random(5)), "response"))
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
        ObjectGenerator.register(NumberState.class, new AbstractValueGenerator<NumberState>() {

            @Override
            public NumberState generate() {
                return new NumberState(null, null, null);
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
