
    drop table GAME_CONFIGURATION if exists;

    drop table GAME_CONSTRUCTION if exists;

    drop table GAME_RECORD if exists;

    drop table GAME_RECORD_MOVE if exists;

    drop table GAME_RECORD_PLAYER if exists;

    drop table GAME_SCHEDULE if exists;

    create table GAME_CONFIGURATION (
        GAME_NAME varchar(255) not null,
        SPECIFICATION_NAME varchar(255) not null,
        CONFIGURATION varchar(40960),
        primary key (GAME_NAME, SPECIFICATION_NAME)
    );

    create table GAME_CONSTRUCTION (
        GAME varchar(255) not null,
        SESSION_ID varchar(255) not null,
        REQUEST varchar(8192) not null,
        RESPONSES varchar(8192) not null,
        STATE varchar(255) not null,
        version integer not null,
        primary key (GAME, SESSION_ID)
    );

    create table GAME_RECORD (
        GAME varchar(255) not null,
        SESSION_ID varchar(255) not null,
        GAME_NAME varchar(255),
        SPECIFICATION_NAME varchar(255),
        RECORD_STATE integer,
        VERSION integer,
        primary key (GAME, SESSION_ID)
    );

    create table GAME_RECORD_MOVE (
        SESSION_ID varchar(255) not null,
        GAME varchar(255) not null,
        CREATED timestamp,
        REQUEST varchar(2048),
        RESPONSE varchar(2048)
    );

    create table GAME_RECORD_PLAYER (
        SESSION_ID varchar(255) not null,
        GAME varchar(255) not null,
        players varchar(255),
        PLAYERS_ORDER integer not null,
        primary key (SESSION_ID, GAME, PLAYERS_ORDER)
    );

    create table GAME_SCHEDULE (
        GAME varchar(255) not null,
        SESSION_ID varchar(255) not null,
        START_TIME bigint,
        primary key (GAME, SESSION_ID)
    );

    alter table GAME_RECORD_MOVE 
        add constraint UK_mk649a334d2dk6piogbph289b unique (SESSION_ID, GAME, CREATED);

    alter table GAME_RECORD_MOVE 
        add constraint FK_9bmcfqwt0jir6j84xaoe0hycb 
        foreign key (SESSION_ID, GAME) 
        references GAME_RECORD;

    alter table GAME_RECORD_PLAYER 
        add constraint FK_dpm4o6odcd19jxohhh6id8jjp 
        foreign key (SESSION_ID, GAME) 
        references GAME_RECORD;
