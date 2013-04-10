
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
        GAME_STATE varchar(4096),
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
        SPEC_CURRENCY varchar(255),
        SPEC_BET_TYPE varchar(255),
        SPEC_BET_MIN integer,
        SPEC_BET_MAX integer,
        SPEC_GIVE_UP varchar(255),
        SPEC_TIME_TYPE varchar(255),
        SPEC_TIME_LIMIT varchar(255),
        SPEC_TIME_BREACH integer,
        SPEC_MATCH varchar(255),
        SPEC_PRIVACY varchar(255),
        SPEC_PLAYERS_MIN integer,
        SPEC_PLAYERS_MAX integer,
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
