
    drop table if exists GAME_SESSION;

    create table GAME_SESSION (
        SESSION_ID varchar(255) not null,
        CASH_TYPE varchar(255),
        STATE integer,
        primary key (SESSION_ID)
    ) ENGINE=InnoDB;
