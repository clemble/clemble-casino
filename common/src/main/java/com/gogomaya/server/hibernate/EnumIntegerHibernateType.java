package com.gogomaya.server.hibernate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

import com.google.common.collect.ImmutableMap;

public class EnumIntegerHibernateType extends ImmutableHibernateType<Enum<?>> {

    final private int[] TYPES = new int[] { Types.INTEGER };
    final private Class<Enum<?>> ENUM_CLASS;
    final private ImmutableMap<Integer, Enum<?>> ENUM_MAP;

    public EnumIntegerHibernateType(Class<Enum<?>> enumClass) {
        this.ENUM_CLASS = enumClass;

        Map<Integer, Enum<?>> mapValues = new HashMap<Integer, Enum<?>>();
        for (Enum<?> enumValue : enumClass.getEnumConstants()) {
            mapValues.put(enumValue.ordinal(), enumValue);
        }
        this.ENUM_MAP = ImmutableMap.<Integer, Enum<?>> copyOf(mapValues);
    }

    @Override
    public int[] sqlTypes() {
        return this.TYPES;
    }

    @Override
    public Class<?> returnedClass() {
        return this.ENUM_CLASS;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        return ENUM_MAP.get(rs.getInt(names[0]));
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        st.setInt(index, ((Enum<?>) value).ordinal());
    }

}
