package com.gogomaya.server.game.rule;

import java.nio.ByteBuffer;

import com.gogomaya.server.buffer.ByteBufferStream;
import com.gogomaya.server.game.rule.bet.BetRule;
import com.gogomaya.server.game.rule.bet.BetRuleFormat;
import com.gogomaya.server.game.rule.giveup.GiveUpRule;
import com.gogomaya.server.game.rule.giveup.GiveUpRuleFormat;
import com.gogomaya.server.game.rule.time.TimeRule;
import com.gogomaya.server.game.rule.time.TimeRuleFormat;
import com.gogomaya.server.hibernate.EnumStringHibernateType;
import com.gogomaya.server.hibernate.ImmutableHibernateType;
import com.gogomaya.server.hibernate.CombinableImmutableUserType;
import com.gogomaya.server.player.wallet.CashType;

public class GameRuleSpecificationFormat {

    public static class GameRuleSpecificatinHybernateType extends CombinableImmutableUserType<GameRuleSpecification> {

        final private static ImmutableHibernateType<CashType> HIBERNATE_CASH_TYPE = new EnumStringHibernateType<CashType>(CashType.class);
        final private static ImmutableHibernateType<BetRule> HIBERNATE_BET_RULE = new BetRuleFormat.BetRuleHibernateType<BetRule>();
        final private static ImmutableHibernateType<GiveUpRule> HIBERNATE_GIVE_UP_RULE = new GiveUpRuleFormat.GiveUpRuleHibernateType();
        final private static ImmutableHibernateType<TimeRule> HIBERNATE_TIME_RULE = new TimeRuleFormat.TimeRuleHibernateType();

        public GameRuleSpecificatinHybernateType() {
            super(HIBERNATE_CASH_TYPE, HIBERNATE_BET_RULE, HIBERNATE_GIVE_UP_RULE, HIBERNATE_TIME_RULE);
        }

        @Override
        public GameRuleSpecification construct(Object[] readValues) {
            return GameRuleSpecification.create((CashType) readValues[0], (BetRule) readValues[1], (GiveUpRule) readValues[2], (TimeRule) readValues[3]);
        }

        @Override
        public Object[] deConstruct(GameRuleSpecification writeValue) {
            return new Object[] { writeValue.getCashType(), writeValue.getBetRule(), writeValue.getGiveUpRule(), writeValue.getTimeRule() };
        }

    }

    public static class GameRuleSpecificationByteBufferStream implements ByteBufferStream<GameRuleSpecification> {

        private ByteBufferStream<BetRule> betBufferStream = new BetRuleFormat.CustomBetRuleByteBufferStream();
        private ByteBufferStream<GiveUpRule> giveUpBufferStream = new GiveUpRuleFormat.CustomGiveUpRuleByteBufferStream();
        private ByteBufferStream<TimeRule> timeRuleBufferStream = new TimeRuleFormat.CustomTimeRuleByteBufferStream();

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
            TimeRule timeRule = timeRuleBufferStream.read(readBuffer);

            return GameRuleSpecification.create(cashType, betRule, giveUpRule, timeRule);
        }

    }
}
