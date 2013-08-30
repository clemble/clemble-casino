package com.gogomaya.server.sql.game;

import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaExport.Type;
import org.hibernate.tool.hbm2ddl.Target;
import org.junit.Test;

import com.gogomaya.game.GameSession;
import com.gogomaya.game.GameTable;
import com.gogomaya.game.construct.GameConstruction;
import com.gogomaya.game.construct.ScheduledGame;
import com.gogomaya.game.specification.GameSpecification;

public class GameSchemaGenerator {

    @Test
    public void generateSchemaMySQL() {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();

        addAnnotatedClasses(configuration)
            .setProperty(Environment.DIALECT, "org.hibernate.dialect.MySQL5InnoDBDialect")
            .setProperty(Environment.DRIVER, "com.mysql.jdbc.Driver");

        new SchemaExport(configuration)
            .setDelimiter(";")
            .setFormat(true)
            .setOutputFile("src/main/resources/sql/schema/mysql/game-schema.sql")
            .create(true, false);
    }

    @Test
    public void generateSchemaH2() {
        Configuration configuration = new org.hibernate.cfg.Configuration();

        addAnnotatedClasses(configuration)
            .setProperty(Environment.DIALECT, "com.gogomaya.server.spring.common.ImprovedH2Dialect")
            .setProperty(Environment.DRIVER, "org.h2.Driver")
            ;

        new SchemaExport(configuration)
            .setDelimiter(";")
            .setFormat(true)
            .setOutputFile("src/main/resources/sql/schema/h2/game-schema.sql")
            .execute(Target.interpret(true, false), Type.BOTH);
    }

    private Configuration addAnnotatedClasses(Configuration configuration){
        return configuration
                .addAnnotatedClass(GameSpecification.class)
                .addAnnotatedClass(GameSession.class)
                .addAnnotatedClass(GameTable.class)
                .addAnnotatedClass(GameConstruction.class)
                .addAnnotatedClass(ScheduledGame.class);
    }

}
