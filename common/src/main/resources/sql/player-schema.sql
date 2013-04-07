
    drop table if exists PLAYER_CREDENTIALS;

    drop table if exists PLAYER_IDENTITY;

    drop table if exists PLAYER_PROFILE;

    drop table if exists PLAYER_WALLET;

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
        playerId bigint not null,
        CURRENCY integer,
        AMOUNT integer,
        primary key (playerId)
    ) ENGINE=InnoDB;
