
    drop table GAME_CONFIGURATION if exists;

    drop table GAME_SCHEDULE if exists;

    drop table GAME_SESSION if exists;

    drop table GAME_SESSION_CONSTRUCTION if exists;

    drop table GAME_SESSION_MOVES if exists;

    drop table GAME_SESSION_PLAYERS if exists;

    create table GAME_CONFIGURATION (
        GAME_NAME varchar(255) not null,
        SPECIFICATION_NAME varchar(255) not null,
        CONFIGURATION varchar(4096),
        primary key (GAME_NAME, SPECIFICATION_NAME)
    );

    create table GAME_SCHEDULE (
        GAME integer not null,
        SESSION_ID varchar(255) not null,
        START_TIME bigint,
        primary key (GAME, SESSION_ID)
    );

    create table GAME_SESSION (
        GAME integer not null,
        SESSION_ID varchar(255) not null,
        GAME_NAME varchar(255),
        SPECIFICATION_NAME varchar(255),
        SESSION_STATE integer,
        GAME_STATE varchar(4096),
        VERSION integer,
        primary key (GAME, SESSION_ID)
    );

    create table GAME_SESSION_CONSTRUCTION (
        GAME integer not null,
        SESSION_ID varchar(255) not null,
        REQUEST varchar(8192) not null,
        RESPONSES varchar(8192) not null,
        STATE varchar(255) not null,
        version integer not null,
        primary key (GAME, SESSION_ID)
    );

    create table GAME_SESSION_MOVES (
        SESSION_ID integer not null,
        GAME varchar(255) not null,
        GAME_MOVE varchar(512),
        MOVE_ID integer,
        MOVE_TIME bigint
    );

    create table GAME_SESSION_PLAYERS (
        SESSION_ID integer not null,
        GAME varchar(255) not null,
        players varchar(255),
        PLAYERS_ORDER integer not null,
        primary key (SESSION_ID, GAME, PLAYERS_ORDER)
    );

    alter table GAME_SESSION_MOVES 
        add constraint FK_aedf85y3v0mkk356uwh8l0raq 
        foreign key (SESSION_ID, GAME) 
        references GAME_SESSION;

    alter table GAME_SESSION_PLAYERS 
        add constraint FK_q9w5vqx8fjrruds2lr7b6uyc6 
        foreign key (SESSION_ID, GAME) 
        references GAME_SESSION;
