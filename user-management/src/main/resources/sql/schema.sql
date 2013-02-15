
    drop table if exists GAMER_PROFILE;

    create table GAMER_PROFILE (
        USER_ID bigint not null,
        BIRTH_DATE date,
        CATEGORY integer,
        FIRST_NAME varchar(64),
        GENDER integer,
        IMAGE_URL varchar(255),
        LAST_NAME varchar(64),
        NICK_NAME varchar(64),
        primary key (USER_ID)
    ) ENGINE=InnoDB;
