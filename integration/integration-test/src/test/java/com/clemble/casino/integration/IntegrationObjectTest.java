package com.clemble.casino.integration;

import static com.clemble.test.random.ObjectGenerator.register;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.Date;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import com.clemble.casino.event.ExpectedEvent;
import com.clemble.casino.event.Event;
import com.clemble.casino.event.surrender.GiveUpEvent;
import com.clemble.casino.game.*;
import com.clemble.casino.event.bet.BetEvent;
import com.clemble.casino.game.construction.event.InvitationAcceptedEvent;
import com.clemble.casino.game.construction.event.InvitationResponseEvent;
import com.clemble.casino.game.configuration.RoundGameConfiguration;
import com.clemble.casino.game.rule.outcome.WonRule;
import com.clemble.casino.game.unit.GameUnit;

import com.clemble.casino.rule.ConfigurationRule;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Ignore;
import org.springframework.security.oauth.common.signature.RSAKeySecret;

import com.clemble.casino.VersionAware;
import com.clemble.casino.game.construction.AutomaticGameRequest;
import com.clemble.casino.game.construction.GameConstruction;
import com.clemble.casino.game.construction.GameInitiation;
import com.clemble.casino.game.outcome.GameOutcome;
import com.clemble.casino.rule.bet.FixedBetRule;
import com.clemble.casino.rule.bet.LimitedBetRule;
import com.clemble.casino.rule.bet.UnlimitedBetRule;
import com.clemble.casino.game.rule.construct.PlayerNumberRule;
import com.clemble.casino.rule.privacy.PrivacyRule;
import com.clemble.casino.game.configuration.GameConfiguration;
import com.clemble.casino.game.configuration.TournamentGameConfiguration;
import com.clemble.casino.integration.game.NumberState;
import com.clemble.casino.integration.game.NumberUnit;
import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.casino.money.Operation;
import com.clemble.casino.player.PlayerGender;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.registration.PlayerCredential;
import com.clemble.casino.utils.ClembleConsumerDetailUtils;
import com.clemble.test.random.AbstractValueGenerator;
import com.clemble.test.random.ObjectGenerator;
import com.clemble.test.random.ValueGenerator;
import com.google.common.collect.ImmutableList;

@Ignore
public class IntegrationObjectTest {

    static {
        register(InvitationResponseEvent.class, new AbstractValueGenerator<InvitationResponseEvent>() {
            @Override
            public InvitationResponseEvent generate() {
            return new InvitationAcceptedEvent("d", "b");
            }
        });
        register(ExpectedEvent.class, new AbstractValueGenerator<ExpectedEvent>() {
            @Override
            public ExpectedEvent generate() {
            return ExpectedEvent.fromClass("S", InvitationAcceptedEvent.class);
            }
        });
        register(NumberState.class, new AbstractValueGenerator<NumberState>() {
            @Override
            public NumberState generate() {
            GameInitiation initiation = new GameInitiation(GameSessionAware.DEFAULT_SESSION, ImmutableList.of("A", "B"), RoundGameConfiguration.DEFAULT);
            return new NumberState(RoundGameContext.fromInitiation(initiation, null), null, 0);
            }
        });
        register(RoundGameContext.class, new AbstractValueGenerator<RoundGameContext>(){
            @Override
            public RoundGameContext generate() {
            GameInitiation initiation = new GameInitiation(GameSessionAware.DEFAULT_SESSION, ImmutableList.of("A", "B"), RoundGameConfiguration.DEFAULT);
            return RoundGameContext.fromInitiation(initiation, null);
            }
            
        });
        register(FixedBetRule.class, new AbstractValueGenerator<FixedBetRule>() {
            @Override
            public FixedBetRule generate() {
            return FixedBetRule.create(new long[] { 10 });
            }
        });
        register(Event.class, new AbstractValueGenerator<Event>() {
            @Override
            public Event generate() {
            return new GiveUpEvent(RandomStringUtils.random(5));
            }
        });
        register(BetEvent.class, new AbstractValueGenerator<BetEvent>() {
            @Override
            public BetEvent generate() {
            return new BetEvent(RandomStringUtils.random(5), 100);
            }
        });
        register(PlayerAccount.class, new AbstractValueGenerator<PlayerAccount>() {
            @Override
            public PlayerAccount generate() {
            return new PlayerAccount(RandomStringUtils.random(5), ImmutableMap.of(Currency.FakeMoney, Money.create(Currency.FakeMoney, 500)));
            }
        });
        register(GameUnit.class, new AbstractValueGenerator<GameUnit>() {
            @Override
            public GameUnit generate() {
            return new NumberUnit(Collections.<GameUnit>emptyList());
            }
        });
        register(GameState.class, new AbstractValueGenerator<GameState>() {
            @Override
            public GameState generate() {
            return new NumberState(ObjectGenerator.generate(RoundGameContext.class), null, 0);
            }
        });
        register(PaymentTransaction.class, new AbstractValueGenerator<PaymentTransaction>() {
            @Override
            public PaymentTransaction generate() {
                return new PaymentTransaction()
                        .setTransactionKey(RandomStringUtils.random(5))
                        .setTransactionDate(new Date())
                        .setProcessingDate(new Date())
                        .addPaymentOperation(
                                new PaymentOperation(RandomStringUtils.random(5), Money.create(Currency.FakeMoney, 50), Operation.Credit))
                        .addPaymentOperation(
                                new PaymentOperation(RandomStringUtils.random(5), Money.create(Currency.FakeMoney, 50), Operation.Debit));
            }
        });
        register(PlayerCredential.class, new AbstractValueGenerator<PlayerCredential>() {
            @Override
            public PlayerCredential generate() {
                return new PlayerCredential().setEmail(RandomStringUtils.randomAlphabetic(10) + "@gmail.com").setPassword(RandomStringUtils.random(10))
                        .setPlayer(RandomStringUtils.random(5));
            }
        });
        register(PlayerProfile.class, new AbstractValueGenerator<PlayerProfile>() {
            @Override
            public PlayerProfile generate() {
                return new PlayerProfile().setBirthDate(new Date(0)).setFirstName(RandomStringUtils.randomAlphabetic(10))
                        .setGender(PlayerGender.M).setLastName(RandomStringUtils.randomAlphabetic(10)).setNickName(RandomStringUtils.randomAlphabetic(10))
                        .setPlayer(RandomStringUtils.random(5));
            }
        });
        register(ConfigurationRule.class, new AbstractValueGenerator<ConfigurationRule>() {
            @Override
            public ConfigurationRule generate() {
                return UnlimitedBetRule.INSTANCE;
            }
        });
        register(GameConstruction.class, new AbstractValueGenerator<GameConstruction>() {
            @Override
            public GameConstruction generate() {
                return new AutomaticGameRequest(RandomStringUtils.random(5), RoundGameConfiguration.DEFAULT).toConstruction(RandomStringUtils.random(5));
            }
        });
        register(LimitedBetRule.class, new AbstractValueGenerator<LimitedBetRule>() {
            @Override
            public LimitedBetRule generate() {
                return LimitedBetRule.create(10, 200);
            }
        });
        register(RoundGameConfiguration.class, new AbstractValueGenerator<RoundGameConfiguration>() {
            @Override
            public RoundGameConfiguration generate() {
                return RoundGameConfiguration.DEFAULT;
            }
        });
        register(VersionAware.class, "version", new ValueGenerator<Integer>() {
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
        register(WonRule.class, new AbstractValueGenerator<WonRule>() {
            @Override
            public WonRule generate() {
                return WonRule.owned;
            }
        });
        register(GameConfiguration.class, new AbstractValueGenerator<GameConfiguration>() {
            @Override
            public GameConfiguration generate() {
                return RoundGameConfiguration.DEFAULT;
            }
        });
        register(TournamentGameConfiguration.class, new AbstractValueGenerator<TournamentGameConfiguration>() {
            @Override
            public TournamentGameConfiguration generate() {
                return new TournamentGameConfiguration(Game.pic, "AAA", new Money(Currency.FakeMoney, 50), PrivacyRule.players, PlayerNumberRule.two, RoundGameConfiguration.DEFAULT, null, null, null, null, null);
            }
        });

        final RSAKeySecret rsaKey = ClembleConsumerDetailUtils.randomKey();
        register(PrivateKey.class, new AbstractValueGenerator<PrivateKey>() {
            @Override
            public PrivateKey generate() {
                return rsaKey.getPrivateKey();
            }
        });
        register(PublicKey.class, new AbstractValueGenerator<PublicKey>() {
            @Override
            public PublicKey generate() {
                return rsaKey.getPublicKey();
            }
        });
        register(MatchGameContext.class, new AbstractValueGenerator<MatchGameContext>() {
            @Override
            public MatchGameContext generate() {
                return new MatchGameContext(GameSessionAware.DEFAULT_SESSION, null, Collections.<MatchGamePlayerContext>emptyList(), null, 0, Collections.<GameOutcome>emptyList());
            }
        });
        register(TournamentGameContext.class, new AbstractValueGenerator<TournamentGameContext>() {
            @Override
            public TournamentGameContext generate() {
                return new TournamentGameContext(GameSessionAware.DEFAULT_SESSION, null, null, null);
            }
        });
        try {
            final KeyGenerator AES = KeyGenerator.getInstance("AES");
            AES.init(256, new SecureRandom());
            register(SecretKey.class, new AbstractValueGenerator<SecretKey>() {
                @Override
                public SecretKey generate() {
                    return AES.generateKey();
                }
            });
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

}
