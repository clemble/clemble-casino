package com.gogomaya.server.game.rule;

import java.nio.ByteBuffer;

import com.gogomaya.server.buffer.ByteBufferStream;
import com.gogomaya.server.game.rule.bet.BetRule;
import com.gogomaya.server.game.rule.bet.BetRuleFormat;
import com.gogomaya.server.game.rule.giveup.GiveUpRule;
import com.gogomaya.server.game.rule.giveup.GiveUpRuleFormat;
import com.gogomaya.server.game.rule.time.TimeLimitRule;
import com.gogomaya.server.game.rule.time.TimeLimitRuleFormat;
import com.gogomaya.server.hibernate.EnumStringHibernateType;
import com.gogomaya.server.hibernate.ImmutableHibernateType;
import com.gogomaya.server.hibernate.CombinableImmutableUserType;
import com.gogomaya.server.player.wallet.CashType;

public class GameRuleSpecificationFormat {

    public static class GameRuleSpecificatinHybernateType extends CombinableImmutableUserType<GameRuleSpecification> {

        final private static ImmutableHibernateType<CashType> HIBERNATE_CASH_TYPE = new EnumStringHibernateType<CashType>(CashType.class);
        final private static ImmutableHibernateType<BetRule> HIBERNATE_BET_RULE = new BetRuleFormat.BetRuleHibernateType();
        final private static ImmutableHibernateType<GiveUpRule> HIBERNATE_GIVE_UP_RULE = new GiveUpRuleFormat.GiveUpRuleHibernateType();
        final private static ImmutableHibernateType<TimeLimitRule> HIBERNATE_TIME_RULE = new TimeLimitRuleFormat.TimeRuleHibernateType();

        public GameRuleSpecificatinHybernateType() {
            super(HIBERNATE_CASH_TYPE, HIBERNATE_BET_RULE, HIBERNATE_GIVE_UP_RULE, HIBERNATE_TIME_RULE);
        }

        @Override
        public GameRuleSpecification construct(Object[] readValues) {
            return GameRuleSpecification.create((CashType) readValues[0], (BetRule) readValues[1], (GiveUpRule) readValues[2], (TimeLimitRule) readValues[3]);
        }

        @Override
        public Object[] deConstruct(GameRuleSpecification writeValue) {
            return new Object[] { writeValue.getCashType(), writeValue.getBetRule(), writeValue.getGiveUpRule(), writeValue.getTimeRule() };
        }

    }

    public static class GameRuleSpecificationByteBufferStream implements ByteBufferStream<GameRuleSpecification> {

        private ByteBufferStream<BetRule> betBufferStream = new BetRuleFormat.CustomBetRuleByteBufferStream();
        private ByteBufferStream<GiveUpRule> giveUpBufferStream = new GiveUpRuleFormat.CustomGiveUpRuleByteBufferStream();
        private ByteBufferStream<TimeLimitRule> timeRuleBufferStream = new TimeLimitRuleFormat.CustomTimeRuleByteBufferStream();

        @Override
        public ByteBuffer write(GameRuleSpecification value, ByteBuffer writeBuffer) {
            writeBuffer.put((byte) value.getCashType().ordinal());

            betBufferStream.write(value.getBetRule(), writeBuffer);
            giveUpBufferStream.write(value.getGiveUpRule(), writeBuffer);
            timeRuleBufferStream.write(value.getTimeRule(), writeBuffer);

            return writeBuffer;
        }

        @Override
        public GameRuleSpecification read(ByteBuffer readBuffer) {
            byte cash = readBuffer.get();
            CashType cashType = cash == CashType.FakeMoney.ordinal() ? CashType.FakeMoney : null;

            BetRule betRule = betBufferStream.read(readBuffer);
            GiveUpRule giveUpRule = giveUpBufferStream.read(readBuffer);
            TimeLimitRule timeRule = timeRuleBufferStream.read(readBuffer);

            return GameRuleSpecification.create(cashType, betRule, giveUpRule, timeRule);
        }

    }
}
