
    drop table if exists PLAYER_PROFILE;

    create table PLAYER_PROFILE (
        PLAYER_ID varchar(255) not null,
        BIRTH_DATE date,
        CATEGORY integer,
        FIRST_NAME varchar(64),
        GENDER integer,
        IMAGE_URL varchar(255),
        LAST_NAME varchar(64),
        NICK_NAME varchar(64),
        VERSION integer,
        primary key (PLAYER_ID)
    ) ENGINE=InnoDB;
