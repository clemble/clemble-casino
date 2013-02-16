
    drop table if exists PLAYER_PROFILE;

    drop table if exists USER_CREDENTIALS;

    drop table if exists USER_IDENTITY;

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

    create table USER_CREDENTIALS (
        PROFILE_ID bigint not null,
        CREDENTIAL_TYPE integer,
        EMAIL varchar(255),
        PASSWORD varchar(255),
        primary key (PROFILE_ID)
    ) ENGINE=InnoDB;

    create table USER_IDENTITY (
        PROFILE_ID bigint not null,
        SECRET varchar(255),
        primary key (PROFILE_ID)
    ) ENGINE=InnoDB;
