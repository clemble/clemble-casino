package com.gogomaya.server.game;

import java.nio.ByteBuffer;

import com.gogomaya.server.buffer.ByteBufferStream;
import com.gogomaya.server.buffer.EnumByteBufferStream;
import com.gogomaya.server.game.bet.rule.BetRule;
import com.gogomaya.server.game.bet.rule.BetRuleFormat;
import com.gogomaya.server.game.giveup.rule.GiveUpRule;
import com.gogomaya.server.game.table.rule.GameTableMatchRule;
import com.gogomaya.server.game.table.rule.GameTablePlayerNumberRule;
import com.gogomaya.server.game.table.rule.GameTablePrivacyRule;
import com.gogomaya.server.game.table.rule.PlayerNumberHibernateType;
import com.gogomaya.server.game.time.rule.TimeLimitRule;
import com.gogomaya.server.game.time.rule.TimeLimitRuleFormat;
import com.gogomaya.server.hibernate.CombinableImmutableUserType;
import com.gogomaya.server.hibernate.EnumStringHibernateType;
import com.gogomaya.server.hibernate.ImmutableHibernateType;
import com.gogomaya.server.money.Currency;

public class GameSpecificationFormats {

    final private static ByteBufferStream<GameSpecification> GAME_SPECIFICATION_STREAM = new GameSpecificationByteBufferStream();

    public static GameSpecification fromByteArray(byte[] buffer) {
        ByteBuffer readBuffer = ByteBuffer.wrap(buffer);
        return GAME_SPECIFICATION_STREAM.read(readBuffer);
    }

    public static byte[] toByteArray(GameSpecification gameSpecification) {
        ByteBuffer writeBuffer = ByteBuffer.allocate(32);
        GAME_SPECIFICATION_STREAM.write(gameSpecification, writeBuffer);
        return writeBuffer.array();
    }

    public static class GameSpecificationHibernateType extends CombinableImmutableUserType<GameSpecification> {

        final private static ImmutableHibernateType<Currency> HIBERNATE_CURRENCY_TYPE = new EnumStringHibernateType<Currency>(Currency.class);
        final private static ImmutableHibernateType<BetRule> HIBERNATE_BET_RULE = new BetRuleFormat.BetRuleHibernateType();
        final private static ImmutableHibernateType<GiveUpRule> HIBERNATE_GIVE_UP_RULE = new EnumStringHibernateType<GiveUpRule>(GiveUpRule.class);
        final private static ImmutableHibernateType<TimeLimitRule> HIBERNATE_TIME_RULE = new TimeLimitRuleFormat.TimeRuleHibernateType();
        final private static ImmutableHibernateType<GameTableMatchRule> HIBERNATE_MATCH_RULE = new EnumStringHibernateType<GameTableMatchRule>(
                GameTableMatchRule.class);
        final private static ImmutableHibernateType<GameTablePrivacyRule> HIBERNATE_PRIVACY_RULE = new EnumStringHibernateType<GameTablePrivacyRule>(
                GameTablePrivacyRule.class);
        final private static ImmutableHibernateType<GameTablePlayerNumberRule> HIBERNATE_NUMBER_RULE = new PlayerNumberHibernateType();

        public GameSpecificationHibernateType() {
            super(HIBERNATE_CURRENCY_TYPE, HIBERNATE_BET_RULE, HIBERNATE_GIVE_UP_RULE, HIBERNATE_TIME_RULE, HIBERNATE_MATCH_RULE, HIBERNATE_PRIVACY_RULE, HIBERNATE_NUMBER_RULE);
        }

        @Override
        public GameSpecification construct(Object[] readValues) {
            return GameSpecification.create(
                    (Currency) readValues[0],
                    (BetRule) readValues[1], 
                    (GiveUpRule) readValues[2],
                    (TimeLimitRule) readValues[3],
                    (GameTableMatchRule) readValues[4],
                    (GameTablePrivacyRule) readValues[5],
                    (GameTablePlayerNumberRule) readValues[6]);
        }

        @Override
        public Object[] deConstruct(GameSpecification writeValue) {
            return new Object[] {
                    writeValue.getCurrency(),
                    writeValue.getBetRule(),
                    writeValue.getGiveUpRule(),
                    writeValue.getTimeRule(),
                    writeValue.getMatchRule(),
                    writeValue.getPrivacyRule(),
                    writeValue.getNumberRule()};
        }

    }

    public static class GameSpecificationByteBufferStream implements ByteBufferStream<GameSpecification> {

        final private ByteBufferStream<BetRule> betBufferStream = new BetRuleFormat.CustomBetRuleByteBufferStream();
        final private ByteBufferStream<GiveUpRule> giveUpBufferStream = new EnumByteBufferStream<GiveUpRule>(GiveUpRule.class);
        final private ByteBufferStream<TimeLimitRule> timeRuleBufferStream = new TimeLimitRuleFormat.CustomTimeRuleByteBufferStream();

        @Override
        public ByteBuffer write(GameSpecification value, ByteBuffer writeBuffer) {
            writeBuffer.put((byte) value.getCurrency().ordinal());

            betBufferStream.write(value.getBetRule(), writeBuffer);
            giveUpBufferStream.write(value.getGiveUpRule(), writeBuffer);
            timeRuleBufferStream.write(value.getTimeRule(), writeBuffer);

            writeBuffer.put((byte) value.getMatchRule().ordinal());
            writeBuffer.put((byte) value.getPrivacyRule().ordinal());
            writeBuffer.putInt(value.getNumberRule().getMinPlayers());
            writeBuffer.putInt(value.getNumberRule().getMaxPlayers());

            return writeBuffer;
        }

        @Override
        public GameSpecification read(ByteBuffer readBuffer) {
            byte cash = readBuffer.get();
            Currency currency = cash == Currency.FakeMoney.ordinal() ? Currency.FakeMoney : null;

            BetRule betRule = betBufferStream.read(readBuffer);
            GiveUpRule giveUpRule = giveUpBufferStream.read(readBuffer);
            TimeLimitRule timeRule = timeRuleBufferStream.read(readBuffer);

            byte match = readBuffer.get();
            GameTableMatchRule matchType = match == GameTableMatchRule.automatic.ordinal() ? GameTableMatchRule.automatic : match == GameTableMatchRule.manual
                    .ordinal() ? GameTableMatchRule.manual : null;
            byte privacy = readBuffer.get();
            GameTablePrivacyRule privacyType = privacy == GameTablePrivacyRule.players.ordinal() ? GameTablePrivacyRule.players
                    : privacy == GameTablePrivacyRule.all.ordinal() ? GameTablePrivacyRule.all : null;

            GameTablePlayerNumberRule numberRule = GameTablePlayerNumberRule.create(readBuffer.getInt(), readBuffer.getInt());

            return GameSpecification.create(currency, betRule, giveUpRule, timeRule, matchType, privacyType, numberRule);
        }

    }

}
