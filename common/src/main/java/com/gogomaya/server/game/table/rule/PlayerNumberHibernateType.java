package com.gogomaya.server.game.table.rule;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

import com.gogomaya.server.hibernate.ImmutableHibernateType;

public class PlayerNumberHibernateType extends ImmutableHibernateType<GameTablePlayerNumberRule> {

    final int[] TYPES = new int[] { Types.INTEGER, Types.INTEGER };

    @Override
    public int[] sqlTypes() {
        return TYPES;
    }

    @Override
    public Class<GameTablePlayerNumberRule> returnedClass() {
        return GameTablePlayerNumberRule.class;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        return GameTablePlayerNumberRule.create(rs.getInt(names[0]), rs.getInt(names[1]));
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        st.setInt(index++, ((GameTablePlayerNumberRule) value).getMinPlayers());
        st.setInt(index++, ((GameTablePlayerNumberRule) value).getMaxPlayers());
    }

}
