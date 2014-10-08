package com.clemble.casino.server.game.sql;

import com.clemble.casino.game.lifecycle.record.GameRecord;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaExport.Type;
import org.hibernate.tool.hbm2ddl.Target;
import org.junit.Ignore;
import org.junit.Test;

import com.clemble.casino.game.lifecycle.construction.GameConstruction;

public class GameSchemaGenerator {

    @Test @Ignore //TODO re enable
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
            .setProperty(Environment.DIALECT, "com.clemble.casino.server.spring.common.ImprovedH2Dialect")
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
                // Moved to separate project .addAnnotatedClass(ServerGameConfiguration.class)
                .addAnnotatedClass(GameRecord.class)
                .addAnnotatedClass(GameConstruction.class);
    }

}
