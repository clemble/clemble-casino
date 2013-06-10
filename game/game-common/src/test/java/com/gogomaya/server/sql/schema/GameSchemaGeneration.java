package com.gogomaya.server.sql.schema;

import org.hibernate.cfg.Environment;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameTable;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.spring.common.CommonSpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonSpringConfiguration.class })
public class GameSchemaGeneration {

    @Test
    public void generateGameSchema(){
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();

        configuration
        .addAnnotatedClass(GameSpecification.class)
        .addAnnotatedClass(GameSession.class)
        .addAnnotatedClass(GameTable.class)
        .setProperty(Environment.DIALECT, "org.hibernate.dialect.MySQL5InnoDBDialect")
        .setProperty(Environment.DRIVER, "com.mysql.jdbc.Driver");

        new SchemaExport(configuration).setDelimiter(";").setFormat(true).setOutputFile("../../sql/game-schema.sql").create(true, false);
    }

}
