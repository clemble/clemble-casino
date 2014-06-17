package com.clemble.casino.server.sql.management;

import org.hibernate.cfg.Environment;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaExport.Type;
import org.hibernate.tool.hbm2ddl.Target;
import org.junit.Test;

import com.clemble.casino.player.security.PlayerCredential;
import com.clemble.casino.player.security.PlayerToken;
import com.clemble.casino.player.security.PlayerSession;

public class ManagementSchemaGenerator {

    @Test
    public void generateSchemaMySQL() {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();

        configuration
            .addAnnotatedClass(PlayerCredential.class)
            .addAnnotatedClass(PlayerToken.class)
            .addAnnotatedClass(PlayerSession.class)
            .setProperty(Environment.DIALECT, "org.hibernate.dialect.MySQL5InnoDBDialect")
            .setProperty(Environment.DRIVER, "com.mysql.jdbc.Driver");

        new SchemaExport(configuration)
            .setDelimiter(";")
            .setFormat(true)
            .setOutputFile("src/main/resources/sql/schema/mysql/management-schema.sql")
            .create(true, false);
    }

    @Test
    public void generateSchemaH2() {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();

        configuration
            .addAnnotatedClass(PlayerCredential.class)
            .addAnnotatedClass(PlayerToken.class)
            .addAnnotatedClass(PlayerSession.class)
            //.setProperty(Environment.DIALECT, "org.hibernate.dialect.H2Dialect")
            .setProperty(Environment.DIALECT, "com.clemble.casino.server.spring.common.ImprovedH2Dialect")
            .setProperty(Environment.DRIVER, "org.h2.Driver")
            ;

        new SchemaExport(configuration)
            .setDelimiter(";")
            .setFormat(true)
            .setOutputFile("src/main/resources/sql/schema/h2/management-schema.sql")
            .execute(Target.interpret(true, false), Type.BOTH);
    }


}
