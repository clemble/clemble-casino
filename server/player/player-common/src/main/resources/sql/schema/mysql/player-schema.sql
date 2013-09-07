
    drop table if exists PLAYER_PROFILE;

    drop table if exists PLAYER_SESSION;

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

    create table PLAYER_SESSION (
        SESSION_ID bigint not null auto_increment,
        EXPIRATION_TIME datetime,
        PLAYER_ID bigint,
        START_TIME datetime,
        primary key (SESSION_ID)
    ) ENGINE=InnoDB;
