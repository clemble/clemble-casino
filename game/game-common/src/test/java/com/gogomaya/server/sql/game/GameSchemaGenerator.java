package com.gogomaya.server.sql.game;

import org.hibernate.cfg.Environment;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaExport.Type;
import org.hibernate.tool.hbm2ddl.Target;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameTable;
import com.gogomaya.server.game.construct.GameConstruction;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.spring.common.CommonSpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonSpringConfiguration.class })
public class GameSchemaGenerator {

    @Test
    public void generateSchemaMySQL() {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();

        configuration
            .addAnnotatedClass(GameSpecification.class)
            .addAnnotatedClass(GameSession.class)
            .addAnnotatedClass(GameTable.class)
            .addAnnotatedClass(GameConstruction.class)
            .setProperty(Environment.DIALECT, "org.hibernate.dialect.MySQL5InnoDBDialect")
            .setProperty(Environment.DRIVER, "com.mysql.jdbc.Driver");

        new SchemaExport(configuration)
            .setDelimiter(";")
            .setFormat(true)
            .setOutputFile("../../sql/schema/mysql/game-schema.sql")
            .create(true, false);
    }

    @Test
    public void generateSchemaH2() {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();

        configuration
            .addAnnotatedClass(GameSpecification.class)
            .addAnnotatedClass(GameSession.class)
            .addAnnotatedClass(GameTable.class)
            .addAnnotatedClass(GameConstruction.class)
            .setProperty(Environment.DIALECT, "org.hibernate.dialect.H2Dialect")
            .setProperty(Environment.DRIVER, "org.h2.Driver")
            ;

        new SchemaExport(configuration)
            .setDelimiter(";")
            .setFormat(true)
            .setOutputFile("../../sql/schema/h2/game-schema.sql")
            .execute(Target.interpret(true, false), Type.CREATE);
    }

}
