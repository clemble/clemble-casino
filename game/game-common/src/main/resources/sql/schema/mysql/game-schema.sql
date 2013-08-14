
    alter table GAME_SESSION 
        drop 
        foreign key FK_euxcm3ovet7vx5omn4ovd7am5;

    alter table GAME_SESSION_MOVES 
        drop 
        foreign key FK_bq49v0ydocrflq3xsbrjo2pgp;

    alter table GAME_SESSION_PLAYERS 
        drop 
        foreign key FK_crckpasqd60bigdxpcb6v1f4c;

    alter table GAME_TABLE 
        drop 
        foreign key FK_fpvd7oni58y8s0ckfh5dhd74p;

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
        CONSTRUCTION_ID bigint not null,
        START_TIME bigint,
        primary key (CONSTRUCTION_ID)
    ) ENGINE=InnoDB;

    create table GAME_SESSION (
        SESSION_ID bigint not null,
        NUM_MADE_MOVES integer,
        SESSION_STATE integer,
        GAME_STATE varchar(4096),
        GAME_NAME varchar(255),
        SPECIFICATION_NAME varchar(255),
        primary key (SESSION_ID)
    ) ENGINE=InnoDB;

    create table GAME_SESSION_CONSTRUCTION (
        SESSION_ID bigint not null auto_increment,
        REQUEST varchar(4096) not null,
        RESPONSES varchar(4096) not null,
        STATE varchar(255) not null,
        version integer not null,
        primary key (SESSION_ID)
    ) ENGINE=InnoDB;

    create table GAME_SESSION_MOVES (
        SESSION_ID bigint not null,
        GAME_MOVE varchar(512),
        MOVE_ID integer,
        MOVE_TIME bigint
    ) ENGINE=InnoDB;

    create table GAME_SESSION_PLAYERS (
        SESSION_ID bigint not null,
        players bigint,
        PLAYERS_ORDER integer not null,
        primary key (SESSION_ID, PLAYERS_ORDER)
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
        primary key (GAME_NAME, SPECIFICATION_NAME)
    ) ENGINE=InnoDB;

    create table GAME_TABLE (
        TABLE_ID bigint not null auto_increment,
        SESSION_ID bigint,
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
        add index FK_bq49v0ydocrflq3xsbrjo2pgp (SESSION_ID), 
        add constraint FK_bq49v0ydocrflq3xsbrjo2pgp 
        foreign key (SESSION_ID) 
        references GAME_SESSION (SESSION_ID);

    alter table GAME_SESSION_PLAYERS 
        add index FK_crckpasqd60bigdxpcb6v1f4c (SESSION_ID), 
        add constraint FK_crckpasqd60bigdxpcb6v1f4c 
        foreign key (SESSION_ID) 
        references GAME_SESSION (SESSION_ID);

    alter table GAME_TABLE 
        add index FK_fpvd7oni58y8s0ckfh5dhd74p (SESSION_ID), 
        add constraint FK_fpvd7oni58y8s0ckfh5dhd74p 
        foreign key (SESSION_ID) 
        references GAME_SESSION (SESSION_ID);

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
