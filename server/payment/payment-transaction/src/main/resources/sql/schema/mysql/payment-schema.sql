
    alter table PAYMENT_TRANSACTION_OPERATION 
        drop 
        foreign key FK_jys2g64i2unkwh385gs3x7k1;

    alter table PLAYER_ACCOUNT_AMOUNT 
        drop 
        foreign key FK_fbj51f8hoakoj2ark4h34igxj;

    drop table if exists PAYMENT_TRANSACTION;

    drop table if exists PAYMENT_TRANSACTION_OPERATION;

    drop table if exists PLAYER_ACCOUNT;

    drop table if exists PLAYER_ACCOUNT_AMOUNT;

    create table PAYMENT_TRANSACTION (
        TRANSACTION_SOURCE varchar(255) not null,
        TRANSACTION_ID varchar(255) not null,
        TRANSACTION_PROCESSING_DATE datetime,
        TRANSACTION_DATE datetime,
        primary key (TRANSACTION_SOURCE, TRANSACTION_ID)
    ) ENGINE=InnoDB;

    create table PAYMENT_TRANSACTION_OPERATION (
        TRANSACTION_ID varchar(255) not null,
        MONEY_SOURCE varchar(255) not null,
        CURRENCY integer,
        AMOUNT bigint,
        OPERATION varchar(255),
        PLAYER_ID varchar(255)
    ) ENGINE=InnoDB;

    create table PLAYER_ACCOUNT (
        PLAYER_ID varchar(255) not null,
        primary key (PLAYER_ID)
    ) ENGINE=InnoDB;

    create table PLAYER_ACCOUNT_AMOUNT (
        PLAYER_ID varchar(255) not null,
        CURRENCY integer,
        AMOUNT bigint
    ) ENGINE=InnoDB;

    alter table PAYMENT_TRANSACTION_OPERATION 
        add index FK_jys2g64i2unkwh385gs3x7k1 (TRANSACTION_ID, MONEY_SOURCE), 
        add constraint FK_jys2g64i2unkwh385gs3x7k1 
        foreign key (TRANSACTION_ID, MONEY_SOURCE) 
        references PAYMENT_TRANSACTION (TRANSACTION_SOURCE, TRANSACTION_ID);

    alter table PLAYER_ACCOUNT_AMOUNT 
        add index FK_fbj51f8hoakoj2ark4h34igxj (PLAYER_ID), 
        add constraint FK_fbj51f8hoakoj2ark4h34igxj 
        foreign key (PLAYER_ID) 
        references PLAYER_ACCOUNT (PLAYER_ID);
