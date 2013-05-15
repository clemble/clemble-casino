package com.gogomaya.server.player.wallet;

import org.hibernate.cfg.Environment;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gogomaya.server.spring.common.CommonModuleSpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonModuleSpringConfiguration.class })
public class WalletSchemaGenerator {

    @Test
    public void generateGameSchema(){
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();

        configuration
        .addAnnotatedClass(PlayerWallet.class)
        .addAnnotatedClass(WalletTransaction.class)
        .setProperty(Environment.DIALECT, "org.hibernate.dialect.MySQL5InnoDBDialect")
        .setProperty(Environment.DRIVER, "com.mysql.jdbc.Driver");

        new SchemaExport(configuration).setDelimiter(";").setFormat(true).setOutputFile("../../sql/wallet-schema.sql").create(true, false);
    }
}
