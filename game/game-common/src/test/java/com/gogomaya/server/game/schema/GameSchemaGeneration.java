package com.gogomaya.server.game.schema;

import org.hibernate.cfg.Environment;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.Test;

import com.gogomaya.server.game.tictactoe.TicTacToeSession;
import com.gogomaya.server.game.tictactoe.TicTacToeSpecification;
import com.gogomaya.server.game.tictactoe.action.TicTacToeTable;
import com.gogomaya.server.user.AbstractCommonTest;

public class GameSchemaGeneration extends AbstractCommonTest {

    @Test
    public void generateGameSchema(){
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();

        configuration
        .addAnnotatedClass(TicTacToeSpecification.class)
        .addAnnotatedClass(TicTacToeSession.class)
        .addAnnotatedClass(TicTacToeTable.class)
        .setProperty(Environment.DIALECT, "org.hibernate.dialect.MySQL5InnoDBDialect")
        .setProperty(Environment.DRIVER, "com.mysql.jdbc.Driver");

        new SchemaExport(configuration).setDelimiter(";").setFormat(true).setOutputFile("src/main/resources/sql/game-schema.sql").create(true, false);
    }

}
