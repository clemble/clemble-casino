
    alter table TIC_TAC_TOE_SESSION 
        drop 
        foreign key FKD9704F47F894E9E;

    alter table TIC_TAC_TOE_SESSION_PLAYERS 
        drop 
        foreign key FK409C0C9A25CB574A;

    alter table TIC_TAC_TOE_TABLE 
        drop 
        foreign key FK941E84BF25CB574A;

    alter table TIC_TAC_TOE_TABLE 
        drop 
        foreign key FK941E84BFB406A2DD;

    alter table TIC_TAC_TOE_TABLE_PLAYERS 
        drop 
        foreign key FK18594A12F894E9E;

    drop table if exists TIC_TAC_TOE_SESSION;

    drop table if exists TIC_TAC_TOE_SESSION_PLAYERS;

    drop table if exists TIC_TAC_TOE_SPECIFICATION;

    drop table if exists TIC_TAC_TOE_TABLE;

    drop table if exists TIC_TAC_TOE_TABLE_PLAYERS;

    create table TIC_TAC_TOE_SESSION (
        SESSION_ID bigint not null auto_increment,
        SESSION_STATE integer,
        GAME_STATE varchar(4096),
        TABLE_ID bigint not null,
        primary key (SESSION_ID)
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

    create table TIC_TAC_TOE_TABLE (
        TABLE_ID bigint not null auto_increment,
        NOTIFICATION_URL varchar(255),
        PUBLISH_URL varchar(255),
        SESSION_ID bigint,
        SPECIFICATION_GROUP varchar(255),
        SPECIFICATION_NAME varchar(255),
        primary key (TABLE_ID)
    ) ENGINE=InnoDB;

    create table TIC_TAC_TOE_TABLE_PLAYERS (
        TABLE_ID bigint not null,
        players bigint
    ) ENGINE=InnoDB;

    alter table TIC_TAC_TOE_SESSION 
        add constraint UK_D9704F47CAA0FB2C unique (TABLE_ID);

    alter table TIC_TAC_TOE_SESSION 
        add index FKD9704F47F894E9E (TABLE_ID), 
        add constraint FKD9704F47F894E9E 
        foreign key (TABLE_ID) 
        references TIC_TAC_TOE_TABLE (TABLE_ID);

    alter table TIC_TAC_TOE_SESSION_PLAYERS 
        add index FK409C0C9A25CB574A (SESSION_ID), 
        add constraint FK409C0C9A25CB574A 
        foreign key (SESSION_ID) 
        references TIC_TAC_TOE_SESSION (SESSION_ID);

    alter table TIC_TAC_TOE_TABLE 
        add index FK941E84BF25CB574A (SESSION_ID), 
        add constraint FK941E84BF25CB574A 
        foreign key (SESSION_ID) 
        references TIC_TAC_TOE_SESSION (TABLE_ID);

    alter table TIC_TAC_TOE_TABLE 
        add index FK941E84BFB406A2DD (SPECIFICATION_GROUP, SPECIFICATION_NAME), 
        add constraint FK941E84BFB406A2DD 
        foreign key (SPECIFICATION_GROUP, SPECIFICATION_NAME) 
        references TIC_TAC_TOE_SPECIFICATION (SPECIFICATION_GROUP, SPECIFICATION_NAME);

    alter table TIC_TAC_TOE_TABLE_PLAYERS 
        add index FK18594A12F894E9E (TABLE_ID), 
        add constraint FK18594A12F894E9E 
        foreign key (TABLE_ID) 
        references TIC_TAC_TOE_TABLE (TABLE_ID);
