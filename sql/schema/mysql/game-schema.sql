
    alter table GAME_SESSION 
        drop 
        foreign key FK8E8AE7291DA56B0A;

    alter table GAME_SESSION_MOVES 
        drop 
        foreign key FKDDDFCE0C575A4F22;

    alter table GAME_SESSION_PLAYERS 
        drop 
        foreign key FK7D99A27C575A4F22;

    alter table GAME_TABLE 
        drop 
        foreign key FK8D61AD21575A4F22;

    alter table GAME_TABLE 
        drop 
        foreign key FK8D61AD211DA56B0A;

    alter table GAME_TABLE_PLAYERS 
        drop 
        foreign key FK9226F07474164E22;

    drop table if exists GAME_CONSTRUCTION;

    drop table if exists GAME_SESSION;

    drop table if exists GAME_SESSION_MOVES;

    drop table if exists GAME_SESSION_PLAYERS;

    drop table if exists GAME_SPECIFICATION;

    drop table if exists GAME_TABLE;

    drop table if exists GAME_TABLE_PLAYERS;

    create table GAME_CONSTRUCTION (
        CONSTRUCTION_ID bigint not null auto_increment,
        REQUEST varchar(4096),
        RESPONSES varchar(4096),
        SESSION_ID bigint,
        state varchar(255),
        primary key (CONSTRUCTION_ID)
    ) ENGINE=InnoDB;

    create table GAME_SESSION (
        SESSION_ID bigint not null auto_increment,
        NUM_MADE_MOVES integer,
        SESSION_STATE integer,
        GAME_STATE varchar(4096),
        GAME_NAME varchar(255),
        SPECIFICATION_NAME varchar(255),
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
        PRICE integer,
        CURRENCY varchar(255),
        GIVE_UP varchar(255),
        MOVE_TIME_LIMIT integer,
        MOVE_TIME_PUNISHMENT varchar(255),
        PLAYER_NUMBER varchar(255),
        PRIVACY_RULE varchar(255),
        TOTAL_TIME_LIMIT integer,
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
        add index FK8E8AE7291DA56B0A (GAME_NAME, SPECIFICATION_NAME), 
        add constraint FK8E8AE7291DA56B0A 
        foreign key (GAME_NAME, SPECIFICATION_NAME) 
        references GAME_SPECIFICATION (GAME_NAME, SPECIFICATION_NAME);

    alter table GAME_SESSION_MOVES 
        add index FKDDDFCE0C575A4F22 (SESSION_ID), 
        add constraint FKDDDFCE0C575A4F22 
        foreign key (SESSION_ID) 
        references GAME_SESSION (SESSION_ID);

    alter table GAME_SESSION_PLAYERS 
        add index FK7D99A27C575A4F22 (SESSION_ID), 
        add constraint FK7D99A27C575A4F22 
        foreign key (SESSION_ID) 
        references GAME_SESSION (SESSION_ID);

    alter table GAME_TABLE 
        add index FK8D61AD21575A4F22 (SESSION_ID), 
        add constraint FK8D61AD21575A4F22 
        foreign key (SESSION_ID) 
        references GAME_SESSION (SESSION_ID);

    alter table GAME_TABLE 
        add index FK8D61AD211DA56B0A (GAME_NAME, SPECIFICATION_NAME), 
        add constraint FK8D61AD211DA56B0A 
        foreign key (GAME_NAME, SPECIFICATION_NAME) 
        references GAME_SPECIFICATION (GAME_NAME, SPECIFICATION_NAME);

    alter table GAME_TABLE_PLAYERS 
        add index FK9226F07474164E22 (TABLE_ID), 
        add constraint FK9226F07474164E22 
        foreign key (TABLE_ID) 
        references GAME_TABLE (TABLE_ID);
