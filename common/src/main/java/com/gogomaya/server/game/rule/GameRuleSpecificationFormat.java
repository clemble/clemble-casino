package com.gogomaya.server.game.rule;

import java.nio.ByteBuffer;

import com.gogomaya.server.buffer.ByteBufferStream;
import com.gogomaya.server.buffer.EnumByteBufferStream;
import com.gogomaya.server.game.bet.rule.BetRule;
import com.gogomaya.server.game.bet.rule.BetRuleFormat;
import com.gogomaya.server.game.giveup.rule.GiveUpRule;
import com.gogomaya.server.game.time.rule.TimeLimitRule;
import com.gogomaya.server.game.time.rule.TimeLimitRuleFormat;
import com.gogomaya.server.hibernate.CombinableImmutableUserType;
import com.gogomaya.server.hibernate.EnumStringHibernateType;
import com.gogomaya.server.hibernate.ImmutableHibernateType;
import com.gogomaya.server.money.Currency;

public class GameRuleSpecificationFormat {

    public static class GameRuleSpecificatinHybernateType extends CombinableImmutableUserType<GameRuleSpecification> {

        final private static ImmutableHibernateType<Currency> HIBERNATE_CASH_TYPE = new EnumStringHibernateType<Currency>(Currency.class);
        final private static ImmutableHibernateType<BetRule> HIBERNATE_BET_RULE = new BetRuleFormat.BetRuleHibernateType();
        final private static ImmutableHibernateType<TimeLimitRule> HIBERNATE_TIME_RULE = new TimeLimitRuleFormat.TimeRuleHibernateType();

        public GameRuleSpecificatinHybernateType() {
            super(HIBERNATE_CASH_TYPE, HIBERNATE_BET_RULE, HIBERNATE_TIME_RULE);
        }

        @Override
        public GameRuleSpecification construct(Object[] readValues) {
            return GameRuleSpecification.create((Currency) readValues[0], (BetRule) readValues[1], (GiveUpRule) readValues[2], (TimeLimitRule) readValues[3]);
        }

        @Override
        public Object[] deConstruct(GameRuleSpecification writeValue) {
            return new Object[] { writeValue.getCurrency(), writeValue.getBetRule(), writeValue.getGiveUpRule(), writeValue.getTimeRule() };
        }

    }

    public static class GameRuleSpecificationByteBufferStream implements ByteBufferStream<GameRuleSpecification> {

        private ByteBufferStream<BetRule> betBufferStream = new BetRuleFormat.CustomBetRuleByteBufferStream();
        private ByteBufferStream<GiveUpRule> giveUpBufferStream = new EnumByteBufferStream<GiveUpRule>(GiveUpRule.class);
        private ByteBufferStream<TimeLimitRule> timeRuleBufferStream = new TimeLimitRuleFormat.CustomTimeRuleByteBufferStream();

        @Override
        public ByteBuffer write(GameRuleSpecification value, ByteBuffer writeBuffer) {
            writeBuffer.put((byte) value.getCurrency().ordinal());

            betBufferStream.write(value.getBetRule(), writeBuffer);
            giveUpBufferStream.write(value.getGiveUpRule(), writeBuffer);
            timeRuleBufferStream.write(value.getTimeRule(), writeBuffer);

            return writeBuffer;
        }

        @Override
        public GameRuleSpecification read(ByteBuffer readBuffer) {
            byte cash = readBuffer.get();
            Currency cashType = cash == Currency.FakeMoney.ordinal() ? Currency.FakeMoney : null;

            BetRule betRule = betBufferStream.read(readBuffer);
            GiveUpRule giveUpRule = giveUpBufferStream.read(readBuffer);
            TimeLimitRule timeRule = timeRuleBufferStream.read(readBuffer);

            return GameRuleSpecification.create(cashType, betRule, giveUpRule, timeRule);
        }

    }
}
