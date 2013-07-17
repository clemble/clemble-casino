
    alter table PAYMENT_TRANSACTION_OPERATION 
        drop 
        foreign key FK27595F6D8230C2AC;

    alter table PLAYER_WALLET_MONEY 
        drop 
        foreign key FK927D05788C841CF1;

    drop table if exists PAYMENT_TRANSACTION;

    drop table if exists PAYMENT_TRANSACTION_OPERATION;

    drop table if exists PLAYER_WALLET;

    drop table if exists PLAYER_WALLET_MONEY;

    create table PAYMENT_TRANSACTION (
        MONEY_SOURCE varchar(255) not null,
        TRANSACTION_ID bigint not null,
        TRANSACTION_DATE datetime,
        primary key (MONEY_SOURCE, TRANSACTION_ID)
    ) ENGINE=InnoDB;

    create table PAYMENT_TRANSACTION_OPERATION (
        TRANSACTION_ID varchar(255) not null,
        MONEY_SOURCE bigint not null,
        CURRENCY integer,
        AMOUNT bigint,
        OPERATION varchar(255),
        PLAYER_ID bigint
    ) ENGINE=InnoDB;

    create table PLAYER_WALLET (
        PLAYER_ID bigint not null,
        primary key (PLAYER_ID)
    ) ENGINE=InnoDB;

    create table PLAYER_WALLET_MONEY (
        PLAYER_ID bigint not null,
        CURRENCY integer,
        AMOUNT bigint
    ) ENGINE=InnoDB;

    alter table PAYMENT_TRANSACTION_OPERATION 
        add index FK27595F6D8230C2AC (TRANSACTION_ID, MONEY_SOURCE), 
        add constraint FK27595F6D8230C2AC 
        foreign key (TRANSACTION_ID, MONEY_SOURCE) 
        references PAYMENT_TRANSACTION (MONEY_SOURCE, TRANSACTION_ID);

    alter table PLAYER_WALLET_MONEY 
        add index FK927D05788C841CF1 (PLAYER_ID), 
        add constraint FK927D05788C841CF1 
        foreign key (PLAYER_ID) 
        references PLAYER_WALLET (PLAYER_ID);
