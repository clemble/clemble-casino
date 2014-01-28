package com.clemble.casino.integration;

import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;

import com.clemble.casino.VersionAware;
import com.clemble.casino.base.ActionLatch;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.action.BetAction;
import com.clemble.casino.game.action.GameAction;
import com.clemble.casino.game.action.surrender.GiveUpAction;
import com.clemble.casino.game.cell.Cell;
import com.clemble.casino.game.cell.CellState;
import com.clemble.casino.game.construct.AutomaticGameRequest;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.construct.GameConstructionState;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.rule.MatchRule;
import com.clemble.casino.game.rule.bet.FixedBetRule;
import com.clemble.casino.game.rule.bet.LimitedBetRule;
import com.clemble.casino.game.rule.bet.UnlimitedBetRule;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.game.unit.GameUnit;
import com.clemble.casino.integration.game.NumberState;
import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PaymentTransactionKey;
import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.payment.money.Currency;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.payment.money.Operation;
import com.clemble.casino.player.PlayerCategory;
import com.clemble.casino.player.PlayerGender;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.security.PlayerCredential;
import com.clemble.test.random.AbstractValueGenerator;
import com.clemble.test.random.ObjectGenerator;
import com.clemble.test.random.ValueGenerator;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class ObjectTest {

    static {
        ObjectGenerator.register(GameSessionKey.class, new AbstractValueGenerator<GameSessionKey>() {
            @Override
            public GameSessionKey generate() {
                return new GameSessionKey(ObjectGenerator.generate(Game.class), ObjectGenerator.generate(String.class));
            }
        });
        ObjectGenerator.register(CellState.class, new AbstractValueGenerator<CellState>() {
            @Override
            public CellState generate() {
                return CellState.DEFAULT;
            }
        });
        ObjectGenerator.register(GameUnit.class, new AbstractValueGenerator<GameUnit>() {
            @Override
            public GameUnit generate() {
                return Cell.create(0, 0);
            }
        });
        ObjectGenerator.register(FixedBetRule.class, new AbstractValueGenerator<FixedBetRule>() {
            @Override
            public FixedBetRule generate() {
                return FixedBetRule.create(new long[] { 10 });
            }
        });
        ObjectGenerator.register(GameAction.class, new AbstractValueGenerator<GameAction>() {
            @Override
            public GameAction generate() {
                return new GiveUpAction(RandomStringUtils.random(5));
            }
        });
        ObjectGenerator.register(BetAction.class, new AbstractValueGenerator<BetAction>() {
            @Override
            public BetAction generate() {
                return new BetAction(RandomStringUtils.random(5), 100);
            }
        });
        ObjectGenerator.register(PlayerAccount.class, new AbstractValueGenerator<PlayerAccount>() {
            @Override
            public PlayerAccount generate() {
                return new PlayerAccount(RandomStringUtils.random(5), ImmutableSet.of(Money.create(Currency.FakeMoney, 500)));
            }
        });
        ObjectGenerator.register(PaymentTransaction.class, new AbstractValueGenerator<PaymentTransaction>() {
            @Override
            public PaymentTransaction generate() {
                return new PaymentTransaction()
                        .setTransactionKey(new PaymentTransactionKey().setSource("TicTacToe").setTransaction(RandomStringUtils.random(5)))
                        .setTransactionDate(new Date())
                        .setProcessingDate(new Date())
                        .addPaymentOperation(
                                new PaymentOperation().setAmount(Money.create(Currency.FakeMoney, 50)).setOperation(Operation.Credit)
                                        .setPlayer(RandomStringUtils.random(5)))
                        .addPaymentOperation(
                                new PaymentOperation().setAmount(Money.create(Currency.FakeMoney, 50)).setOperation(Operation.Debit)
                                        .setPlayer(RandomStringUtils.random(5)));
            }
        });
        ObjectGenerator.register(PlayerCredential.class, new AbstractValueGenerator<PlayerCredential>() {
            @Override
            public PlayerCredential generate() {
                return new PlayerCredential().setEmail(RandomStringUtils.randomAlphabetic(10) + "@gmail.com").setPassword(RandomStringUtils.random(10))
                        .setPlayer(RandomStringUtils.random(5));
            }
        });
        ObjectGenerator.register(PlayerProfile.class, new AbstractValueGenerator<PlayerProfile>() {
            @Override
            public PlayerProfile generate() {
                return new PlayerProfile().setBirthDate(new Date(0)).setCategory(PlayerCategory.Amateur).setFirstName(RandomStringUtils.randomAlphabetic(10))
                        .setGender(PlayerGender.M).setLastName(RandomStringUtils.randomAlphabetic(10)).setNickName(RandomStringUtils.randomAlphabetic(10))
                        .setPlayer(RandomStringUtils.random(5));
            }
        });
        ObjectGenerator.register(MatchRule.class, new AbstractValueGenerator<MatchRule>() {
            @Override
            public MatchRule generate() {
                return UnlimitedBetRule.INSTANCE;
            }
        });
        ObjectGenerator.register(GameConstruction.class, new AbstractValueGenerator<GameConstruction>() {
            @Override
            public GameConstruction generate() {
                return new GameConstruction().setSession(new GameSessionKey(Game.pic, "0"))
                        .setRequest(new AutomaticGameRequest(RandomStringUtils.random(5), MatchGameConfiguration.DEFAULT))
                        .setResponses(new ActionLatch().expectNext(ImmutableList.<String>of(RandomStringUtils.random(5), RandomStringUtils.random(5)), "response"))
                        .setState(GameConstructionState.pending);
            }
        });
        ObjectGenerator.register(LimitedBetRule.class, new AbstractValueGenerator<LimitedBetRule>() {
            @Override
            public LimitedBetRule generate() {
                return LimitedBetRule.create(10, 200);
            }
        });
        ObjectGenerator.register(MatchGameConfiguration.class, new AbstractValueGenerator<MatchGameConfiguration>() {
            @Override
            public MatchGameConfiguration generate() {
                return MatchGameConfiguration.DEFAULT;
            }
        });
        ObjectGenerator.register(NumberState.class, new AbstractValueGenerator<NumberState>() {
            @Override
            public NumberState generate() {
                GameInitiation initiation = new GameInitiation(GameSessionKey.DEFAULT_SESSION, ImmutableList.of("A", "B"), MatchGameConfiguration.DEFAULT);
                GameContext context = new GameContext(initiation, MatchGameConfiguration.DEFAULT);
                return new NumberState(context, null, 0);
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
