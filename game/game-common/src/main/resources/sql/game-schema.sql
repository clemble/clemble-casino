
    alter table GAME_SESSION 
        drop 
        foreign key FK8E8AE7291DA56B0A;

    alter table GAME_TABLE 
        drop 
        foreign key FK8D61AD21FBDC66F6;

    alter table GAME_TABLE 
        drop 
        foreign key FK8D61AD211DA56B0A;

    alter table TIC_TAC_TOE_SESSION_MOVES 
        drop 
        foreign key FK641227AAFBDC66F6;

    alter table TIC_TAC_TOE_SESSION_PLAYERS 
        drop 
        foreign key FK409C0C9AFBDC66F6;

    alter table TIC_TAC_TOE_TABLE_PLAYERS 
        drop 
        foreign key FK18594A12E4244AF6;

    drop table if exists GAME_SESSION;

    drop table if exists GAME_TABLE;

    drop table if exists TIC_TAC_TOE_SESSION_MOVES;

    drop table if exists TIC_TAC_TOE_SESSION_PLAYERS;

    drop table if exists TIC_TAC_TOE_SPECIFICATION;

    drop table if exists TIC_TAC_TOE_TABLE_PLAYERS;

    create table GAME_SESSION (
        SESSION_ID bigint not null auto_increment,
        SESSION_STATE integer,
        SPECIFICATION_GROUP varchar(255),
        SPECIFICATION_NAME varchar(255),
        primary key (SESSION_ID)
    ) ENGINE=InnoDB;

    create table GAME_TABLE (
        TABLE_ID bigint not null auto_increment,
        NOTIFICATION_URL varchar(255),
        PUBLISH_URL varchar(255),
        GAME_STATE varchar(4096),
        SESSION_ID bigint,
        SPECIFICATION_GROUP varchar(255),
        SPECIFICATION_NAME varchar(255),
        primary key (TABLE_ID)
    ) ENGINE=InnoDB;

    create table TIC_TAC_TOE_SESSION_MOVES (
        SESSION_ID bigint not null,
        madeMoves tinyblob
    ) ENGINE=InnoDB;

    create table TIC_TAC_TOE_SESSION_PLAYERS (
        SESSION_ID bigint not null,
        players bigint
    ) ENGINE=InnoDB;

    create table TIC_TAC_TOE_SPECIFICATION (
        SPECIFICATION_GROUP varchar(255) not null,
        SPECIFICATION_NAME varchar(255) not null,
        PRICE integer,
        CURRENCY varchar(255),
        GIVE_UP varchar(255),
        MATCH_RULE varchar(255),
        MOVE_TIME_LIMIT integer,
        MOVE_TIME_PUNISHMENT varchar(255),
        PLAYER_NUMBER varchar(255),
        PRIVACY_RULE varchar(255),
        TOTAL_TIME_LIMIT integer,
        TOTAL_TIME_PUNISHMENT varchar(255),
        primary key (SPECIFICATION_GROUP, SPECIFICATION_NAME)
    ) ENGINE=InnoDB;

    create table TIC_TAC_TOE_TABLE_PLAYERS (
        TABLE_ID bigint not null,
        players bigint
    ) ENGINE=InnoDB;

    alter table GAME_SESSION 
        add index FK8E8AE7291DA56B0A (SPECIFICATION_GROUP, SPECIFICATION_NAME), 
        add constraint FK8E8AE7291DA56B0A 
        foreign key (SPECIFICATION_GROUP, SPECIFICATION_NAME) 
        references TIC_TAC_TOE_SPECIFICATION (SPECIFICATION_GROUP, SPECIFICATION_NAME);

    alter table GAME_TABLE 
        add index FK8D61AD21FBDC66F6 (SESSION_ID), 
        add constraint FK8D61AD21FBDC66F6 
        foreign key (SESSION_ID) 
        references GAME_SESSION (SESSION_ID);

    alter table GAME_TABLE 
        add index FK8D61AD211DA56B0A (SPECIFICATION_GROUP, SPECIFICATION_NAME), 
        add constraint FK8D61AD211DA56B0A 
        foreign key (SPECIFICATION_GROUP, SPECIFICATION_NAME) 
        references TIC_TAC_TOE_SPECIFICATION (SPECIFICATION_GROUP, SPECIFICATION_NAME);

    alter table TIC_TAC_TOE_SESSION_MOVES 
        add index FK641227AAFBDC66F6 (SESSION_ID), 
        add constraint FK641227AAFBDC66F6 
        foreign key (SESSION_ID) 
        references GAME_SESSION (SESSION_ID);

    alter table TIC_TAC_TOE_SESSION_PLAYERS 
        add index FK409C0C9AFBDC66F6 (SESSION_ID), 
        add constraint FK409C0C9AFBDC66F6 
        foreign key (SESSION_ID) 
        references GAME_SESSION (SESSION_ID);

    alter table TIC_TAC_TOE_TABLE_PLAYERS 
        add index FK18594A12E4244AF6 (TABLE_ID), 
        add constraint FK18594A12E4244AF6 
        foreign key (TABLE_ID) 
        references GAME_TABLE (TABLE_ID);
