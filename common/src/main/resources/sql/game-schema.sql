
    alter table GAME_SESSION 
        drop 
        foreign key FK8E8AE729955BF882;

    alter table GAME_SESSION_PLAYERS 
        drop 
        foreign key FK7D99A27C96971DCA;

    alter table GAME_TABLE 
        drop 
        foreign key FK8D61AD2196971DCA;

    alter table GAME_TABLE_PLAYERS 
        drop 
        foreign key FK9226F074955BF882;

    drop table if exists GAME_SESSION;

    drop table if exists GAME_SESSION_PLAYERS;

    drop table if exists GAME_TABLE;

    drop table if exists GAME_TABLE_PLAYERS;

    create table GAME_SESSION (
        SESSION_ID bigint not null auto_increment,
        SESSION_STATE integer,
        TABLE_ID bigint not null,
        primary key (SESSION_ID),
        unique (TABLE_ID)
    ) ENGINE=InnoDB;

    create table GAME_SESSION_PLAYERS (
        SESSION_ID bigint not null,
        players bigint
    ) ENGINE=InnoDB;

    create table GAME_TABLE (
        TABLE_ID bigint not null auto_increment,
        NOTIFICATION_URL varchar(255),
        PUBLISH_URL varchar(255),
        BET_TYPE varchar(255),
        BET_MIN_PRICE integer,
        BET_MAX_PRICE integer,
        BET_CASH_TYPE integer,
        LOOSE_TYPE varchar(255),
        LOOSE_MIN_PART integer,
        TIME_TYPE varchar(255),
        TIME_BREACH_TYPE varchar(255),
        TIME_LIMIT integer,
        TABLE_MATCH_RULE integer,
        TABLE_PLAYERS_MAX integer,
        TABLE_PLAYERS_MIN integer,
        TABLE_PRIVACY_RULE integer,
        SESSION_ID bigint,
        primary key (TABLE_ID)
    ) ENGINE=InnoDB;

    create table GAME_TABLE_PLAYERS (
        TABLE_ID bigint not null,
        players bigint
    ) ENGINE=InnoDB;

    alter table GAME_SESSION 
        add index FK8E8AE729955BF882 (TABLE_ID), 
        add constraint FK8E8AE729955BF882 
        foreign key (TABLE_ID) 
        references GAME_TABLE (TABLE_ID);

    alter table GAME_SESSION_PLAYERS 
        add index FK7D99A27C96971DCA (SESSION_ID), 
        add constraint FK7D99A27C96971DCA 
        foreign key (SESSION_ID) 
        references GAME_SESSION (SESSION_ID);

    alter table GAME_TABLE 
        add index FK8D61AD2196971DCA (SESSION_ID), 
        add constraint FK8D61AD2196971DCA 
        foreign key (SESSION_ID) 
        references GAME_SESSION (TABLE_ID);

    alter table GAME_TABLE_PLAYERS 
        add index FK9226F074955BF882 (TABLE_ID), 
        add constraint FK9226F074955BF882 
        foreign key (TABLE_ID) 
        references GAME_TABLE (TABLE_ID);
