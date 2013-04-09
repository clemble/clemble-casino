package com.gogomaya.server.game.bet.rule;

import java.nio.ByteBuffer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

import com.gogomaya.server.buffer.ByteBufferStream;
import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.hibernate.ImmutableHibernateType;

public class BetRuleFormat {

    public static enum BetType {

        Fixed,
        Limited,
        Unlimited;

        public static BetType valueOf(Class<? extends BetRule> betRuleClass) {
            if (betRuleClass.equals(BetFixedRule.class)) {
                return Fixed;
            } else if (betRuleClass.equals(BetUnlimitedRule.class)) {
                return Unlimited;
            } else if (betRuleClass.equals(BetLimitedRule.class)) {
                return Limited;
            }
            return null;
        }

    }

    public static class BetRuleHibernateType extends ImmutableHibernateType<BetRule> {
        final private int[] TYPES = { Types.VARCHAR, Types.INTEGER, Types.INTEGER };

        @Override
        public int[] sqlTypes() {
            return TYPES;
        }

        @Override
        public Class<?> returnedClass() {
            return BetRule.class;
        }

        @Override
        public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
            BetType betType = BetType.valueOf(rs.getString(names[0]));
            switch (betType) {
            case Fixed:
                return BetFixedRule.create(rs.getInt(names[1]));
            case Limited:
                return BetLimitedRule.create(rs.getInt(names[1]), rs.getInt(names[2]));
            case Unlimited:
                return BetUnlimitedRule.INSTANCE;
            }
            throw GogomayaException.create(GogomayaError.ServerCriticalError);
        }

        @Override
        public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
            BetRule betRule = value != null ? (BetRule) value : BetRule.DEFAULT;
            BetType betType = BetType.valueOf(betRule.getClass());
            st.setString(index++, betType.name());
            switch (betType) {
            case Fixed:
                st.setLong(index++, ((BetFixedRule) value).getPrice());
                st.setLong(index++, ((BetFixedRule) value).getPrice());
                break;
            case Limited:
                st.setLong(index++, ((BetLimitedRule) value).getMinBet());
                st.setLong(index++, ((BetLimitedRule) value).getMaxBet());
                break;
            case Unlimited:
                st.setLong(index++, 0);
                st.setLong(index++, 0);
                break;
            default:
                throw GogomayaException.create(GogomayaError.ServerCriticalError);
            }
        }

    }

    public static class CustomBetRuleByteBufferStream implements ByteBufferStream<BetRule> {

        @Override
        public ByteBuffer write(BetRule betRule, ByteBuffer buffer) {
            BetType betType = BetType.valueOf(betRule.getClass());

            buffer.put((byte) betType.ordinal());

            switch (betType) {
            case Fixed:
                buffer.putInt(((BetFixedRule) betRule).getPrice());
                break;
            case Limited:
                buffer.putInt(((BetLimitedRule) betRule).getMinBet()).putInt(((BetLimitedRule) betRule).getMaxBet());
                break;
            case Unlimited:
                break;
            }
            return buffer;
        }

        @Override
        public BetRule read(ByteBuffer buffer) {
            int betType = buffer.get();

            if (betType == BetType.Fixed.ordinal()) {
                return BetFixedRule.create(buffer.getInt());
            } else if (betType == BetType.Limited.ordinal()) {
                return BetLimitedRule.create(buffer.getInt(), buffer.getInt());
            } else if (betType == BetType.Unlimited.ordinal()) {
                return BetUnlimitedRule.INSTANCE;
            }

            throw GogomayaException.create(GogomayaError.ServerCriticalError);
        }
    }

}
