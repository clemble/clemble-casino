package com.clemble.casino.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.BulkInsertionCapableIdentifierGenerator;
import org.hibernate.id.Configurable;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.id.SequenceGenerator;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;

public class GameSessionKeyGenerator implements PersistentIdentifierGenerator, BulkInsertionCapableIdentifierGenerator, Configurable {

    final private EnumMap<Game, SequenceGenerator> delegateGenerators = new EnumMap<>(Game.class);

    {
        for (Game game : Game.values())
            delegateGenerators.put(game, new SequenceGenerator());
    }

    @Override
    public void configure(Type type, Properties params, Dialect dialect) throws MappingException {
        for (Game game: Game.values()) {
            params.setProperty("sequence", game.name());
            delegateGenerators.get(game).configure(LongType.INSTANCE, params, dialect);
        }
    }

    @Override
    public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
        if (object instanceof SessionAware) {
            GameSessionKey sessionKey = ((SessionAware) object).getSession();
            Long nextSession = (Long) delegateGenerators.get(sessionKey.getGame()).generate(session, sessionKey);
            sessionKey.setSession(nextSession);
            return sessionKey;
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean supportsBulkInsertionIdentifierGeneration() {
        return true;
    }

    @Override
    public String determineBulkInsertionIdentifierGenerationSelectFragment(Dialect dialect) {
        return "gameSessionKey";
    }

    @Override
    public String[] sqlCreateStrings(Dialect dialect) throws HibernateException {
        Collection<String> scripts = new ArrayList<>(Game.values().length);
        for (SequenceGenerator sequenceGenerator : delegateGenerators.values()) {
            scripts.addAll(Arrays.asList(sequenceGenerator.sqlCreateStrings(dialect)));
        }
        return scripts.toArray(new String[0]);
    }

    @Override
    public String[] sqlDropStrings(Dialect dialect) throws HibernateException {
        Collection<String> scripts = new ArrayList<>(Game.values().length);
        for (SequenceGenerator sequenceGenerator : delegateGenerators.values()) {
            scripts.addAll(Arrays.asList(sequenceGenerator.sqlDropStrings(dialect)));
        }
        return scripts.toArray(new String[0]);
    }

    @Override
    public Object generatorKey() {
        return "gameSessionKey";
    }

}
