
    alter table GAME_SESSION_PLAYERS 
        drop 
        foreign key FK7D99A27C96971DCA;

    drop table if exists GAME_SESSION;

    drop table if exists GAME_SESSION_PLAYERS;

    create table GAME_SESSION (
        SESSION_ID bigint not null auto_increment,
        betRule tinyblob,
        cashType integer,
        giveUpRule tinyblob,
        participationRule tinyblob,
        timeRule tinyblob,
        SESSION_STATE integer,
        primary key (SESSION_ID)
    ) ENGINE=InnoDB;

    create table GAME_SESSION_PLAYERS (
        SESSION_ID bigint not null,
        players bigint
    ) ENGINE=InnoDB;

    alter table GAME_SESSION_PLAYERS 
        add index FK7D99A27C96971DCA (SESSION_ID), 
        add constraint FK7D99A27C96971DCA 
        foreign key (SESSION_ID) 
        references GAME_SESSION (SESSION_ID);
