
    alter table GAME_SESSION 
        drop 
        foreign key FK_euxcm3ovet7vx5omn4ovd7am5;

    alter table GAME_SESSION_MOVES 
        drop 
        foreign key FK_aedf85y3v0mkk356uwh8l0raq;

    alter table GAME_SESSION_PLAYERS 
        drop 
        foreign key FK_q9w5vqx8fjrruds2lr7b6uyc6;

    alter table GAME_TABLE 
        drop 
        foreign key FK_nqchqv3c0d5p0kgxarlm5eosj;

    alter table GAME_TABLE 
        drop 
        foreign key FK_nq44xp7iy46f50hbqlflg1eqp;

    alter table GAME_TABLE_PLAYERS 
        drop 
        foreign key FK_jwsm0vx1r0tespi4wfvpfse62;

    drop table if exists GAME_SCHEDULE;

    drop table if exists GAME_SESSION;

    drop table if exists GAME_SESSION_CONSTRUCTION;

    drop table if exists GAME_SESSION_MOVES;

    drop table if exists GAME_SESSION_PLAYERS;

    drop table if exists GAME_SPECIFICATION;

    drop table if exists GAME_TABLE;

    drop table if exists GAME_TABLE_PLAYERS;

    create table GAME_SCHEDULE (
        GAME integer not null,
        SESSION_ID bigint not null,
        START_TIME bigint,
        primary key (GAME, SESSION_ID)
    ) ENGINE=InnoDB;

    create table GAME_SESSION (
        GAME integer not null,
        SESSION_ID bigint not null,
        SESSION_STATE integer,
        GAME_STATE varchar(4096),
        VERSION integer,
        GAME_NAME varchar(255),
        SPECIFICATION_NAME varchar(255),
        primary key (GAME, SESSION_ID)
    ) ENGINE=InnoDB;

    create table GAME_SESSION_CONSTRUCTION (
        GAME integer not null,
        SESSION_ID bigint not null,
        REQUEST varchar(8192) not null,
        RESPONSES varchar(8192) not null,
        STATE varchar(255) not null,
        version integer not null,
        primary key (GAME, SESSION_ID)
    ) ENGINE=InnoDB;

    create table GAME_SESSION_MOVES (
        SESSION_ID integer not null,
        GAME bigint not null,
        GAME_MOVE varchar(512),
        MOVE_ID integer,
        MOVE_TIME bigint
    ) ENGINE=InnoDB;

    create table GAME_SESSION_PLAYERS (
        SESSION_ID integer not null,
        GAME bigint not null,
        players bigint,
        PLAYERS_ORDER integer not null,
        primary key (SESSION_ID, GAME, PLAYERS_ORDER)
    ) ENGINE=InnoDB;

    create table GAME_SPECIFICATION (
        GAME_NAME varchar(255) not null,
        SPECIFICATION_NAME varchar(255) not null,
        BET_RULE varchar(255),
        GIVE_UP varchar(255),
        MOVE_TIME_LIMIT bigint,
        MOVE_TIME_PUNISHMENT varchar(255),
        PLAYER_NUMBER varchar(255),
        CURRENCY integer,
        PRICE bigint,
        PRIVACY_RULE varchar(255),
        TOTAL_TIME_LIMIT bigint,
        TOTAL_TIME_PUNISHMENT varchar(255),
        VISIBILITY varchar(255),
        primary key (GAME_NAME, SPECIFICATION_NAME)
    ) ENGINE=InnoDB;

    create table GAME_TABLE (
        TABLE_ID bigint not null auto_increment,
        SESSION_ID integer,
        GAME bigint,
        GAME_NAME varchar(255),
        SPECIFICATION_NAME varchar(255),
        primary key (TABLE_ID)
    ) ENGINE=InnoDB;

    create table GAME_TABLE_PLAYERS (
        TABLE_ID bigint not null,
        players bigint,
        PLAYERS_ORDER integer not null,
        primary key (TABLE_ID, PLAYERS_ORDER)
    ) ENGINE=InnoDB;

    alter table GAME_SESSION 
        add index FK_euxcm3ovet7vx5omn4ovd7am5 (GAME_NAME, SPECIFICATION_NAME), 
        add constraint FK_euxcm3ovet7vx5omn4ovd7am5 
        foreign key (GAME_NAME, SPECIFICATION_NAME) 
        references GAME_SPECIFICATION (GAME_NAME, SPECIFICATION_NAME);

    alter table GAME_SESSION_MOVES 
        add index FK_aedf85y3v0mkk356uwh8l0raq (SESSION_ID, GAME), 
        add constraint FK_aedf85y3v0mkk356uwh8l0raq 
        foreign key (SESSION_ID, GAME) 
        references GAME_SESSION (GAME, SESSION_ID);

    alter table GAME_SESSION_PLAYERS 
        add index FK_q9w5vqx8fjrruds2lr7b6uyc6 (SESSION_ID, GAME), 
        add constraint FK_q9w5vqx8fjrruds2lr7b6uyc6 
        foreign key (SESSION_ID, GAME) 
        references GAME_SESSION (GAME, SESSION_ID);

    alter table GAME_TABLE 
        add index FK_nqchqv3c0d5p0kgxarlm5eosj (SESSION_ID, GAME), 
        add constraint FK_nqchqv3c0d5p0kgxarlm5eosj 
        foreign key (SESSION_ID, GAME) 
        references GAME_SESSION (GAME, SESSION_ID);

    alter table GAME_TABLE 
        add index FK_nq44xp7iy46f50hbqlflg1eqp (GAME_NAME, SPECIFICATION_NAME), 
        add constraint FK_nq44xp7iy46f50hbqlflg1eqp 
        foreign key (GAME_NAME, SPECIFICATION_NAME) 
        references GAME_SPECIFICATION (GAME_NAME, SPECIFICATION_NAME);

    alter table GAME_TABLE_PLAYERS 
        add index FK_jwsm0vx1r0tespi4wfvpfse62 (TABLE_ID), 
        add constraint FK_jwsm0vx1r0tespi4wfvpfse62 
        foreign key (TABLE_ID) 
        references GAME_TABLE (TABLE_ID);
