package com.gogomaya.server.hibernate;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

abstract public class ImmutableHibernateType<T extends Serializable> implements UserType {

    @Override
    final public boolean equals(Object x, Object y) throws HibernateException {
        return x == y;
    }

    @Override
    final public int hashCode(Object x) throws HibernateException {
        return x != null ? x.hashCode() : 0;
    }

    @Override
    final public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    final public boolean isMutable() {
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    final public Serializable disassemble(Object value) throws HibernateException {
        return (T) value;
    }

    @Override
    final public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    final public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

}
