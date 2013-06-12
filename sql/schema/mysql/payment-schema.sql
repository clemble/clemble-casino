
    alter table PLAYER_WALLET_MONEY 
        drop 
        foreign key FK927D05788C841CF1;

    alter table PLAYER_WALLET_OPERATION 
        drop 
        foreign key FK2263961FB6ED86FD;

    drop table if exists PLAYER_WALLET;

    drop table if exists PLAYER_WALLET_MONEY;

    drop table if exists PLAYER_WALLET_OPERATION;

    drop table if exists PLAYER_WALLET_TRANSACTION;

    create table PLAYER_WALLET (
        PLAYER_ID bigint not null,
        primary key (PLAYER_ID)
    ) ENGINE=InnoDB;

    create table PLAYER_WALLET_MONEY (
        PLAYER_ID bigint not null,
        CURRENCY integer,
        AMOUNT bigint
    ) ENGINE=InnoDB;

    create table PLAYER_WALLET_OPERATION (
        TRANSACTION_ID varchar(255) not null,
        MONEY_SOURCE bigint not null,
        CURRENCY integer,
        AMOUNT bigint,
        OPERATION varchar(255),
        PLAYER_ID bigint
    ) ENGINE=InnoDB;

    create table PLAYER_WALLET_TRANSACTION (
        MONEY_SOURCE varchar(255) not null,
        TRANSACTION_ID bigint not null,
        TRANSACTION_DATE datetime,
        primary key (MONEY_SOURCE, TRANSACTION_ID)
    ) ENGINE=InnoDB;

    alter table PLAYER_WALLET_MONEY 
        add index FK927D05788C841CF1 (PLAYER_ID), 
        add constraint FK927D05788C841CF1 
        foreign key (PLAYER_ID) 
        references PLAYER_WALLET (PLAYER_ID);

    alter table PLAYER_WALLET_OPERATION 
        add index FK2263961FB6ED86FD (TRANSACTION_ID, MONEY_SOURCE), 
        add constraint FK2263961FB6ED86FD 
        foreign key (TRANSACTION_ID, MONEY_SOURCE) 
        references PLAYER_WALLET_TRANSACTION (MONEY_SOURCE, TRANSACTION_ID);
