
    drop table if exists GAME_SPECIFICATION;

    drop table if exists PLAYER_CREDENTIALS;

    drop table if exists PLAYER_IDENTITY;

    drop table if exists PLAYER_PROFILE;

    drop table if exists PLAYER_WALLET;

    create table GAME_SPECIFICATION (
        GAME_SPECIFICATION bigint not null,
        GAME_NAME varchar(255),
        MAX_BET bigint,
        MAX_PLAYERS integer,
        MAX_TURN_TIME bigint,
        MIN_BET bigint,
        MIN_PLAYERS integer,
        primary key (GAME_SPECIFICATION)
    ) ENGINE=InnoDB;

    create table PLAYER_CREDENTIALS (
        PLAYER_ID bigint not null,
        EMAIL varchar(128) unique,
        PASSWORD varchar(255),
        primary key (PLAYER_ID)
    ) ENGINE=InnoDB;

    create table PLAYER_IDENTITY (
        PLAYER_ID bigint not null,
        SECRET varchar(255),
        primary key (PLAYER_ID)
    ) ENGINE=InnoDB;

    create table PLAYER_PROFILE (
        PLAYER_ID bigint not null,
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
        MONEY bigint,
        primary key (PLAYER_ID)
    ) ENGINE=InnoDB;
