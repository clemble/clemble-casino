
    drop table if exists GAMER_PROFILE;

    create table GAMER_PROFILE (
        USER_ID varchar(255) not null,
        BIRTH_DATE datetime,
        CATEGORY integer,
        EMAIL varchar(255),
        FIRST_NAME varchar(255),
        GENDER integer,
        IMAGE_URL varchar(255),
        LAST_NAME varchar(255),
        NICK_NAME varchar(255),
        primary key (USER_ID)
    ) ENGINE=InnoDB;
