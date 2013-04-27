
    alter table PLAYER_WALLET_MONEY 
        drop 
        foreign key FK927D05788C841CF1;

    alter table PLAYER_WALLET_TRANSACTION 
        drop 
        foreign key FK8FBC41968C841CF1;

    drop table if exists PLAYER_CREDENTIALS;

    drop table if exists PLAYER_IDENTITY;

    drop table if exists PLAYER_PROFILE;

    drop table if exists PLAYER_WALLET;

    drop table if exists PLAYER_WALLET_MONEY;

    drop table if exists PLAYER_WALLET_TRANSACTION;

    create table PLAYER_CREDENTIALS (
        PLAYER_ID bigint not null,
        EMAIL varchar(128),
        PASSWORD varchar(255),
        primary key (PLAYER_ID)
    ) ENGINE=InnoDB;

    create table PLAYER_IDENTITY (
        PLAYER_ID bigint not null,
        SECRET varchar(255),
        primary key (PLAYER_ID)
    ) ENGINE=InnoDB;

    create table PLAYER_PROFILE (
        PLAYER_ID bigint not null auto_increment,
        BIRTH_DATE date,
        CATEGORY integer,
        FIRST_NAME varchar(64),
        GENDER integer,
        IMAGE_URL varchar(255),
        LAST_NAME varchar(64),
        NICK_NAME varchar(64),
        primary key (PLAYER_ID)
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

    create table PLAYER_WALLET_TRANSACTION (
        TRANSACTION_ID bigint not null,
        PLAYER_ID bigint not null,
        CURRENCY integer,
        AMOUNT bigint,
        TRANSACTION_DATE datetime,
        OPERATION varchar(255),
        TRANSACTION_TYPE varchar(255),
        primary key (TRANSACTION_ID, PLAYER_ID)
    ) ENGINE=InnoDB;

    alter table PLAYER_CREDENTIALS 
        add constraint uc_PLAYER_CREDENTIALS_1 unique (EMAIL);

    alter table PLAYER_WALLET_MONEY 
        add index FK927D05788C841CF1 (PLAYER_ID), 
        add constraint FK927D05788C841CF1 
        foreign key (PLAYER_ID) 
        references PLAYER_WALLET (PLAYER_ID);

    alter table PLAYER_WALLET_TRANSACTION 
        add index FK8FBC41968C841CF1 (PLAYER_ID), 
        add constraint FK8FBC41968C841CF1 
        foreign key (PLAYER_ID) 
        references PLAYER_WALLET (PLAYER_ID);
