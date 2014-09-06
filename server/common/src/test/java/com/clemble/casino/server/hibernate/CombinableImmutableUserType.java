package com.clemble.casino.server.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

abstract public class CombinableImmutableUserType<T extends Serializable> extends ImmutableHibernateType<T> {

    final private ImmutableHibernateType<?>[] immutableTypes;
    final private int[][] allSqlTypes;
    final private int[] sqlTypes;

    protected CombinableImmutableUserType(ImmutableHibernateType<?>... immutableTypes) {
        this.immutableTypes = immutableTypes;
        this.allSqlTypes = new int[immutableTypes.length][];
        List<Integer> sqlTypeCollection = new ArrayList<Integer>();
        for (int i = 0; i < immutableTypes.length; i++) {
            allSqlTypes[i] = immutableTypes[i].sqlTypes();
            for (int type : immutableTypes[i].sqlTypes()) {
                sqlTypeCollection.add(type);
            }
        }
        sqlTypes = new int[sqlTypeCollection.size()];
        for (int i = 0; i < sqlTypeCollection.size(); i++)
            sqlTypes[i] = sqlTypeCollection.get(i);
    }

    @Override
    public int[] sqlTypes() {
        return sqlTypes;
    }

    @Override
    public Class<?> returnedClass() {
        return null;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        Object[] readValues = new Object[immutableTypes.length];

        int length = 0;
        for (int i = 0; i < immutableTypes.length; i++) {
            readValues[i] = immutableTypes[i].nullSafeGet(rs, Arrays.copyOfRange(names, length, length + allSqlTypes[i].length), session, owner);
            length += allSqlTypes[i].length;
        }

        return construct(readValues);
    }
    
    abstract public T construct(Object[] readValues);
    
    abstract public Object[] deConstruct(T writeValue);

    @Override
    @SuppressWarnings("unchecked")
    public void nullSafeSet(PreparedStatement st, Object writeValue, int index, SessionImplementor session) throws HibernateException, SQLException {
        Object[] readValues = deConstruct((T) writeValue);

        for (int i = 0; i < immutableTypes.length; i++) {
            immutableTypes[i].nullSafeSet(st, readValues[i], index, session);
            index += allSqlTypes[i].length;
        }
    }

}
