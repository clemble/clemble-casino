package com.clemble.casino.server.hibernate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.util.StringUtils;

public class ListHibernateType extends ImmutableHibernateType {

    final private static int[] TYPES = new int[] { Types.VARCHAR };

    @Override
    public int[] sqlTypes() {
        return TYPES;
    }

    @Override
    public Class returnedClass() {
        return List.class;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        return new ArrayList<String>(Arrays.asList(rs.getString(names[0]).split(",")));
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        if (value == null)
            st.setString(index, null);
        else
            st.setString(index, StringUtils.collectionToCommaDelimitedString((Collection<?>) value));
    }

}
